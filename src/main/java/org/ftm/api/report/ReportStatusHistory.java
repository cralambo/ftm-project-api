package org.ftm.api.report;

import jakarta.persistence.*;
import lombok.*;
import org.ftm.api.user.User;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "report_status_histories",
        indexes = {
                @Index(name = "idx_report_history_report_date",
                        columnList = "partner_report_id,action_at"),
                @Index(name = "idx_report_history_action_by",
                        columnList = "action_by")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class ReportStatusHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "partner_report_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_report_history_report")
    )
    private PartnerReport partnerReport;

    @Enumerated(EnumType.STRING)
    @Column(name = "previous_status", length = 30)
    private ReportStatus previousStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "new_status", nullable = false, length = 30)
    private ReportStatus newStatus;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "action_by",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_report_history_user")
    )
    private User actionBy;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @CreatedDate
    @Column(name = "action_at", nullable = false, updatable = false)
    private LocalDateTime actionAt;
}
