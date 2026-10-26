package org.ftm.api.report;

import jakarta.persistence.*;
import lombok.*;
import org.ftm.api.disbursement_activity.DisbursementActivity;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "technical_report_items",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_technical_report_activity",
                        columnNames = {
                                "partner_report_id",
                                "disbursement_activity_id"
                        }
                )
        },
        indexes = {
                @Index(name = "idx_technical_items_report",
                        columnList = "partner_report_id"),
                @Index(name = "idx_technical_items_activity",
                        columnList = "disbursement_activity_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class TechnicalReportItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "partner_report_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_technical_item_report")
    )
    private PartnerReport partnerReport;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "disbursement_activity_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_technical_item_activity")
    )
    private DisbursementActivity disbursementActivity;

    @Column(name = "achieved_quantity", precision = 19, scale = 2)
    private BigDecimal achievedQuantity;

    @Column(name = "achieved_unit", length = 50)
    private String achievedUnit;

    @Column(name = "result_description", columnDefinition = "TEXT")
    private String resultDescription;

    @Column(columnDefinition = "TEXT")
    private String observations;

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "last_modified_date", insertable = false)
    private LocalDateTime lastModifiedDate;
}
