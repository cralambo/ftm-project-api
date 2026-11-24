package org.ftm.api.report;

import lombok.Builder;
import org.ftm.api.disbursement_schedule.ShortDisbursementScheduleResponse;
import org.ftm.api.partner_contact.PartnerContactResponse;

import java.util.List;

@Builder
public record PartnerReportUnderReviewResponse(
        String id,
        ShortDisbursementScheduleResponse disbursement_schedule,
        PartnerContactResponse partner_contact,
        String status,
        String general_comments,
        String assigned_to,
        String assigned_at,
        String submitted_at,
        List<TechnicalReportItemWithAttachmentResponse> technical_report,
        List<FinancialReportItemWithAttachmentResponse> financial_report,
        List<ReportAttachmentResponse> documents
) {

}
