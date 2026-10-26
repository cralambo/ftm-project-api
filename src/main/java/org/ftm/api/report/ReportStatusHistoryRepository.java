package org.ftm.api.report;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReportStatusHistoryRepository extends JpaRepository<ReportStatusHistory, UUID> {

    List<ReportStatusHistory> findByPartnerReportIdOrderByActionAtDesc(UUID partnerReportId);

    List<ReportStatusHistory> findByPartnerReportIdOrderByActionAtAsc(UUID partnerReportId);

    long countByPartnerReportId(UUID partnerReportId);
}
