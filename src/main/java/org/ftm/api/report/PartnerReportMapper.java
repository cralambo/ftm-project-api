package org.ftm.api.report;

import org.ftm.api.disbursement_schedule.DisbursementScheduleMapper;
import org.ftm.api.partner_contact.PartnerContactMapper;

public class PartnerReportMapper {

    public static PartnerReportResponse mapToPartnerReportResponse(PartnerReport report) {
        return PartnerReportResponse.builder()
                .id(report.getId().toString())
                .partner_contact(PartnerContactMapper.mapToPartnerContactResponse(report.getPartnerContact()))
                .disbursement_schedule(DisbursementScheduleMapper.mapToShortDisbursementScheduleResponse(report.getDisbursementSchedule()))
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
