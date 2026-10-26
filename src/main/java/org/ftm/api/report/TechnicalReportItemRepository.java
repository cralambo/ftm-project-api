package org.ftm.api.report;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TechnicalReportItemRepository extends JpaRepository<TechnicalReportItem, UUID> {
    List<TechnicalReportItem> findByPartnerReportId(UUID partnerContactId);

    @EntityGraph(attributePaths = {"disbursementActivity"})
    List<TechnicalReportItem> findWithActivityByPartnerReportId(UUID partnerReportId);

    Optional<TechnicalReportItem> findByPartnerReportIdAndDisbursementActivityId(
            UUID partnerReportId,
            UUID activityId
    );

    boolean existsByPartnerReportIdAndDisbursementActivityId(
            UUID partnerReportId,
            UUID activityId
    );

    long deleteByPartnerReportIdAndDisbursementActivityId(
            UUID partnerReportId,
            UUID activityId
    );
}
