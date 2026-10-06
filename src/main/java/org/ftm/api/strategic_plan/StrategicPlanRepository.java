package org.ftm.api.strategic_plan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StrategicPlanRepository extends JpaRepository<StrategicPlan, UUID> {
}
