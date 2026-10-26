package org.ftm.api.disbursement_schedule;

import org.ftm.api.project.ProjectMapper;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DisbursementScheduleMapper {

    public static DisbursementSchedule mapToCreateRequest(DisbursementScheduleRequest request) {
        return DisbursementSchedule.builder()
                .reference(request.reference())
                .disbursementMonth(
                        request.disbursement_month() != null
                                ? LocalDate.parse(request.disbursement_month())
                                : null
                )
                .disbursementAmount(
                        request.disbursement_amount() != null
                                ? new BigDecimal(request.disbursement_amount())
                                : null
                )
                .percentage(
                        request.percentage() != null
                                ? new BigDecimal(request.percentage())
                                : null
                )
                .reportSubmissionDate(
                        request.report_submission_date() != null
                                ? LocalDate.parse(request.report_submission_date())
                                : null
                )
                .build();
    }

    public static  DisbursementScheduleResponse mapToDisbursementScheduleResponse(DisbursementSchedule schedule) {
        return DisbursementScheduleResponse.builder()
                .id(schedule.getId().toString())
                .reference(schedule.getReference())
                .disbursement_month(
                        schedule.getDisbursementMonth() != null
                                ? schedule.getDisbursementMonth().toString()
                                : null
                )
                .disbursement_amount(
                        schedule.getDisbursementAmount() != null
                                ? schedule.getDisbursementAmount().toPlainString()
                                : null
                )
                .percentage(
                        schedule.getPercentage() != null
                                ? schedule.getPercentage().toPlainString()
                                : null
                )
                .report_submission_date(
                        schedule.getReportSubmissionDate() != null
                                ? schedule.getReportSubmissionDate().toString()
                                : null
                )
                .project(ProjectMapper.mapToProjectResponse(schedule.getProject()))
                .build();
    }
}
