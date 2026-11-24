package org.ftm.api.report;

import lombok.Builder;
import org.ftm.api.disbursement_schedule.ShortDisbursementScheduleResponse;
import org.ftm.api.partner_contact.PartnerContactResponse;

@Builder
public record PartnerReportResponse(
        String id,
        ShortDisbursementScheduleResponse disbursement_schedule,
        PartnerContactResponse partner_contact,
        String status,
        String general_comments,
        String assigned_to,
        String assigned_at,
        String submitted_at
) {
}
