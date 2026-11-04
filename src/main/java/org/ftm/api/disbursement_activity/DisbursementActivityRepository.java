package org.ftm.api.disbursement_activity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DisbursementActivityRepository extends JpaRepository<DisbursementActivity, UUID> {
    List<DisbursementActivity> findByDisbursementScheduleIdOrderByIdAsc(UUID disbursementScheduleId);
    boolean existsByDisbursementScheduleIdAndNameIgnoreCase(UUID disbursementScheduleId, String name);

    List<DisbursementActivity> findByDisbursementScheduleId(UUID disbursementScheduleId);
}
