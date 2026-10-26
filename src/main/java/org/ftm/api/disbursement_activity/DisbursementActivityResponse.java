package org.ftm.api.disbursement_activity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record DisbursementActivityResponse(
        String id,
        String schedule_id,
        String type,
        String name,
        String description,
        String target_quantity,
        String unit,
        String success_criteria
) {
}
