package org.ftm.api.report;

import lombok.Builder;

@Builder
public record FinancialSummaryDto(
        String report_id,
        String disbursement_schedule_id,
        String schedule_reference,
        String budget_status,
        String budget_amount,
        String total_expenses,
        String remaining_amount,
        String overrun_amount,
        String consumption_rate
) {
}
