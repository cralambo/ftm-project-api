package org.ftm.api.strategic_plan;


import lombok.Builder;

@Builder
public record StrategicPlanResponse(
        String  id,
        String name,
        String code,
        String start_date,
        String end_date,
        String description
) {
}
