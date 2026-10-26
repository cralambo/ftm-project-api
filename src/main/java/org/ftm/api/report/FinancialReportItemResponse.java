package org.ftm.api.report;

import lombok.Builder;

@Builder
public record FinancialReportItemResponse(
        String id,
        String designation,
        String quantity,
        String unit_amount,
        String total_amount,
        String observations
) {
}
