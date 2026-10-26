package org.ftm.api.report;

import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UpsertTechnicalReportItemRequest(
        String achieved_quantity,
        @Size(max = 50)
        String achieved_unit,
        String result_description,
        String observations
) {
}
