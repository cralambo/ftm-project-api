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
        name = "report_attachments",
        indexes = {
                @Index(name = "idx_attachments_report",
                        columnList = "partner_report_id"),
                @Index(name = "idx_attachments_technical_item",
                        columnList = "technical_report_item_id"),
                @Index(name = "idx_attachments_financial_item",
                        columnList = "financial_report_item_id"),
                @Index(name = "idx_attachments_uploaded_by",
                        columnList = "uploaded_by")
        }
)
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ReportAttachment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "partner_report_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_attachment_report")
    )
    private PartnerReport partnerReport;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "technical_report_item_id",
            foreignKey = @ForeignKey(name = "fk_attachment_technical_item")
    )
    private TechnicalReportItem technicalReportItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "financial_report_item_id",
            foreignKey = @ForeignKey(name = "fk_attachment_financial_item")
    )
    private FinancialReportItem financialReportItem;

    @Column(name = "original_filename", nullable = false, length = 255)
    private String originalFilename;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "storage_key", nullable = false, unique = true, columnDefinition = "TEXT")
    private String storageKey;

    @Column(name = "content_type", length = 150)
    private String contentType;

    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", length = 50)
    private ReportAttachmentType documentType;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "uploaded_by",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_attachment_uploaded_by")
    )
    private User uploadedBy;

    @CreatedDate
    @Column(name = "uploaded_at", nullable = false, updatable = false)
    private LocalDateTime uploadedAt;
}
