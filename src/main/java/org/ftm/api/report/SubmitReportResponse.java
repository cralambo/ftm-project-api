package org.ftm.api.report;

import lombok.Builder;

@Builder
public record SubmitReportResponse(
        String report_id,
        String status,
        String submitted_at,
        FinancialSummaryDto financial_summary,
        String message
) {
}
