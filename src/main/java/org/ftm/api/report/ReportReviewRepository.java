package org.ftm.api.report;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReportReviewRepository extends JpaRepository<ReportReview, UUID> {
    List<ReportReview> findByPartnerReportIdOrderByReviewedAtDesc(UUID partnerReportId);

    List<ReportReview> findByPartnerReportIdAndReviewTypeOrderByReviewedAtDesc(
            UUID partnerReportId,
            ReportReviewType reviewType
    );

    List<ReportReview> findByIdOrderByReviewedAtDesc(UUID reviewId);

    Optional<ReportReview> findFirstByPartnerReportIdAndReviewTypeOrderByReviewedAtDesc(
            UUID partnerReportId,
            ReportReviewType reviewType
    );

    boolean existsByPartnerReportIdAndDecision(
            UUID partnerReportId,
            ReportReviewDecision decision
    );

    long countByPartnerReportId(UUID partnerReportId);
}
