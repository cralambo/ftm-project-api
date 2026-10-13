package org.ftm.api.strategic_plan;

import lombok.Builder;

@Builder
public record ShortStrategicPlanResponse(
        String id,
        String code
) {
}
