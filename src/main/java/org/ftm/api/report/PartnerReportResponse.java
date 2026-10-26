package org.ftm.api.report;

import lombok.Builder;

@Builder
public record PartnerReportResponse(
        String id,
        String disbursement_schedule_id,
        String schedule_reference,
        String partner_contact_id,
        String status,
        String general_comments,
        String assigned_to,
        String assigned_at,
        String submitted_at
) {
}
