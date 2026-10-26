package org.ftm.api.report;

import jakarta.persistence.*;
import lombok.*;
import org.ftm.api.disbursement_schedule.DisbursementSchedule;
import org.ftm.api.partner_contact.PartnerContact;
import org.ftm.api.user.User;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "partner_reports",
        indexes = {
                @Index(name = "idx_partner_reports_contact",
                        columnList = "partner_contact_id"),
                @Index(name = "idx_partner_reports_status",
                        columnList = "status"),
                @Index(name = "idx_partner_reports_assigned_to",
                        columnList = "assigned_to"),
                @Index(name = "idx_partner_reports_schedule_status",
                        columnList = "disbursement_schedule_id,status")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class PartnerReport {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "disbursement_schedule_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_report_schedule")
    )
    private DisbursementSchedule disbursementSchedule;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "partner_contact_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_report_partner_contact")
    )
    private PartnerContact partnerContact;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ReportStatus status = ReportStatus.DRAFT;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "assigned_to",
            foreignKey = @ForeignKey(name = "fk_report_assigned_user")
    )
    private User assignedTo;

    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;

    @Column(name = "general_comments", columnDefinition = "TEXT")
    private String generalComments;

    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "last_modified_date", insertable = false)
    private LocalDateTime lastModifiedDate;

    @OneToMany(
            mappedBy = "partnerReport",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<TechnicalReportItem> technicalItems = new ArrayList<>();

    @OneToMany(
            mappedBy = "partnerReport",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<FinancialReportItem> financialItems = new ArrayList<>();

    @OneToMany(
            mappedBy = "partnerReport",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ReportAttachment> attachments = new ArrayList<>();

    @OneToMany(
            mappedBy = "partnerReport",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("actionAt DESC")
    private List<ReportStatusHistory> statusHistories = new ArrayList<>();

    @OneToMany(
            mappedBy = "partnerReport",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("reviewedAt DESC")
    private List<ReportReview> reviews = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (status == null) {
            status = ReportStatus.DRAFT;
        }
    }
}
