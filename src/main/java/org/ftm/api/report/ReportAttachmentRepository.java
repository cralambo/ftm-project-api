package org.ftm.api.report;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReportAttachmentRepository extends JpaRepository<ReportAttachment, UUID> {

    List<ReportAttachment> findByPartnerReportId(UUID partnerReportId);

    List<ReportAttachment> findByTechnicalReportItemId(UUID technicalItemId);

    List<ReportAttachment> findByFinancialReportItemId(UUID financialItemId);

    Optional<ReportAttachment> findByIdAndPartnerReportId(UUID attachmentId, UUID partnerReportId);

    boolean existsByStorageKey(String storageKey);

    long countByPartnerReportId(UUID partnerReportId);

    long deleteByIdAndPartnerReportId(
            UUID attachmentId,
            UUID partnerReportId
    );

    long countByTechnicalReportItemId(UUID technicalItemId);
    long countByFinancialReportItemId(UUID financialItemId);
    long countByPartnerReportIdAndTechnicalReportItemId(
            UUID reportId,
            UUID technicalItemId
    );
    long countByPartnerReportIdAndFinancialReportItemId(
            UUID reportId,
            UUID financialItemId
    );
}
