package org.ftm.api.report;

import lombok.Builder;

@Builder
public record TechnicalReportItemResponse(
        String id,
        String disbursement_activity_id,
        String activity_name,
        String achieved_quantity,
        String achieved_unit,
        String result_description,
        String observations
) {
}
