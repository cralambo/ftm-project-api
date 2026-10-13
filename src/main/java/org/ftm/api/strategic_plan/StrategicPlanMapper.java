package org.ftm.api.strategic_plan;

import java.time.LocalDate;

public class StrategicPlanMapper {

    public static StrategicPlan mapToCreateStrategicPlanRequest(StrategicPlanRequest request) {
        return StrategicPlan.builder()
                .name(request.name())
                .code(request.code())
                .startDate(LocalDate.parse(request.start_date()))
                .endDate(LocalDate.parse(request.end_date()))
                .description(request.description())
                .build();
    }

    public static StrategicPlanResponse mapToStrategicPlanResponse(StrategicPlan strategicPlan) {
        return StrategicPlanResponse.builder()
                .id(strategicPlan.getId().toString())
                .name(strategicPlan.getName())
                .code(strategicPlan.getCode())
                .start_date(
                        strategicPlan.getStartDate() != null
                                ? strategicPlan.getStartDate().toString()
                                : null
                )
                .end_date(
                        strategicPlan.getEndDate() != null
                                ? strategicPlan.getEndDate().toString()
                                : null
                )
                .description(strategicPlan.getDescription())
                .build();
    }

    public static ShortStrategicPlanResponse mapToShortStrategicPlanResponse(StrategicPlan strategicPlan) {
        return ShortStrategicPlanResponse.builder()
                .id(strategicPlan.getId().toString())
                .code(strategicPlan.getCode())
                .build();
    }
}
