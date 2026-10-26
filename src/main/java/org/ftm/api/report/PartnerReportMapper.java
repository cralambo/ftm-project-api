package org.ftm.api.report;

public class PartnerReportMapper {

    public static PartnerReportResponse mapToPartnerReportResponse(PartnerReport report) {
        return PartnerReportResponse.builder()
                .id(report.getId().toString())
                .partner_contact_id(report.getPartnerContact().getId().toString())
                .disbursement_schedule_id(report.getDisbursementSchedule().getId().toString())
                .schedule_reference(report.getDisbursementSchedule().getReference())
                .status(report.getStatus().toString())
                .assigned_at(
                        report.getAssignedAt() != null
                                ? report.getAssignedAt().toString()
                                : null
                )
                .assigned_to(
                        report.getAssignedTo() == null
                                ? null
                                : report.getAssignedTo().getId().toString()
                )
                .general_comments(report.getGeneralComments())
                .submitted_at(
                        report.getSubmittedAt() != null
                                ? report.getSubmittedAt().toString()
                                : null
                )
                .build();
    }
}
