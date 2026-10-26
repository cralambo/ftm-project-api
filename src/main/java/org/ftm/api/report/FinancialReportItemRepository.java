package org.ftm.api.report;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FinancialReportItemRepository extends JpaRepository<FinancialReportItem, UUID> {
    List<FinancialReportItem> findByPartnerReportId(UUID partnerReportId);
    Optional<FinancialReportItem> findByIdAndPartnerReportId(
            UUID itemId,
            UUID partnerReportId
    );

    boolean existsByIdAndPartnerReportId(UUID itemId, UUID partnerReportId);

    long deleteByIdAndPartnerReportId(UUID itemId, UUID partnerReportId);

    @Query("""
        SELECT COALESCE(SUM(item.totalAmount), 0)
        FROM FinancialReportItem item
        WHERE item.partnerReport.id = :reportId
        """)
    BigDecimal sumTotalAmountByReportId(@Param("reportId") UUID reportId);

    long countByPartnerReportId(UUID partnerReportId);
}
