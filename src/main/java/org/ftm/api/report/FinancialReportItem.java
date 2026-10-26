package org.ftm.api.report;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "financial_report_items",
        indexes = {
                @Index(name = "idx_financial_items_report",
                        columnList = "partner_report_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class FinancialReportItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "partner_report_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_financial_item_report")
    )
    private PartnerReport partnerReport;

    @Column(nullable = false, length = 255)
    private String designation;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal quantity;

    @Column(name = "unit_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal unitAmount;

    @Column(
            name = "total_amount",
            precision = 19,
            scale = 2,
            insertable = false,
            updatable = false
    )
    private BigDecimal totalAmount;

    @Column(columnDefinition = "TEXT")
    private String observations;

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "last_modified_date", insertable = false)
    private LocalDateTime lastModifiedDate;
}
