package org.ftm.api.report;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface PartnerReportRepository extends JpaRepository<PartnerReport, UUID> {
    boolean existsByDisbursementScheduleId(UUID scheduleId);
    Optional<PartnerReport> findByDisbursementScheduleId(UUID scheduleId);

    Page<PartnerReport> findByPartnerContactId(UUID partnerContactId, Pageable pageable);
    Page<PartnerReport> findByStatus(ReportStatus status, Pageable pageable);
    Page<PartnerReport> findByAssignedToId(UUID assignedToId, Pageable pageable);
    Page<PartnerReport> findByStatusAndAssignedToId(
            ReportStatus status,
            UUID assignedToId,
            Pageable pageable
    );

    @EntityGraph(attributePaths = {
            "disbursementSchedule",
            "partnerContact",
            "assignedTo"
    })
    Optional<PartnerReport> findWithDetailsById(UUID id);

    boolean existsByIdAndPartnerContactId(UUID reportId, UUID partnerContactId);

    boolean existsByIdAndStatus(
            UUID reportId,
            ReportStatus status
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM PartnerReport r WHERE r.id = :id")
    Optional<PartnerReport> findByIdForUpdate(@Param("id") UUID id);
}
