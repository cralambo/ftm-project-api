package org.ftm.api.disbursement_schedule;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface DisbursementScheduleRepository extends JpaRepository<DisbursementSchedule, UUID> {
    Set<DisbursementSchedule> findByProjectId(UUID projectId);

    @EntityGraph(attributePaths = {
            "project",
            "project.supportPartner"
    })
    Optional<DisbursementSchedule> findWithDetailsById(UUID id);
}
