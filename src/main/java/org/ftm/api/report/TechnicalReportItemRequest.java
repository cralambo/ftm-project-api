package org.ftm.api.report;

import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record TechnicalReportItemRequest(
        String disbursement_activity_id,
        String achieve_quantity,
        @Size(max = 50)
        String achieve_unit,
        String result_description,
        String observations

) {
}
