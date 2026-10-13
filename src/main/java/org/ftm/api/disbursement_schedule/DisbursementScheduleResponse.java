package org.ftm.api.disbursement_schedule;

import lombok.Builder;

@Builder
public record DisbursementScheduleResponse(
        String id,
        String reference,
        String disbursement_month,
        String disbursement_amount,
        String percentage,
        String report_submission_date,
        String project_id
) {
}
