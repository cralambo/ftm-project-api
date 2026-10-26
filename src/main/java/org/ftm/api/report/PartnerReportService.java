package org.ftm.api.report;

import lombok.RequiredArgsConstructor;
import org.ftm.api.common.PageResponse;
import org.ftm.api.disbursement_schedule.DisbursementSchedule;
import org.ftm.api.disbursement_schedule.DisbursementScheduleRepository;
import org.ftm.api.partner_contact.PartnerContact;
import org.ftm.api.security.CustomUserDetails;
import org.ftm.api.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PartnerReportService {

    private final PartnerReportRepository partnerReportRepository;
    private final DisbursementScheduleRepository disbursementScheduleRepository;
    private final ReportStatusHistoryRepository reportStatusHistoryRepository;

    private final ReportCommonService reportCommonService;
    private final FinancialSummaryService financialSummaryService;

    @Transactional(readOnly = true)
    public PartnerReportResponse createReport(
            CreatePartnerReportRequest request,
            CustomUserDetails userDetails
    ) {

        final User actor = reportCommonService.getCurrentUser(userDetails);

        final PartnerContact contact = reportCommonService.getPartnerContact(userDetails);

        final DisbursementSchedule schedule = disbursementScheduleRepository.findById(
                UUID.fromString(request.disbursement_schedule_id())
        ).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Calendrier de décaissement introuvable."
        ));

        if (partnerReportRepository.existsByDisbursementScheduleId(schedule.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Un rapport existe déjà pour ce calendrier de décaissement."
            );
        }

        PartnerReport report = PartnerReport.builder()
                .disbursementSchedule(schedule)
                .partnerContact(contact)
                .status(ReportStatus.DRAFT)
                .build();

        partnerReportRepository.save(report);
        saveHistory(report, null, ReportStatus.DRAFT, actor, "Création du brouillon.");

        return PartnerReportMapper.mapToPartnerReportResponse(report);
    }

    @Transactional(readOnly = true)
    public PartnerReportResponse getReportById(
            UUID reportId,
            CustomUserDetails userDetails
    ) {
        final User actor = reportCommonService.getCurrentUser(userDetails);
        PartnerReport report = reportCommonService.getReport(reportId);
        reportCommonService.ensureCanRead(report, actor);
        return PartnerReportMapper.mapToPartnerReportResponse(report);
    }

    @Transactional(readOnly = true)
    public PageResponse<PartnerReportResponse> getMyReports(
            int page,
            int size,
            CustomUserDetails userDetails
    ) {
        Pageable pageable = PageRequest.of(page, size);
        final PartnerContact contact = reportCommonService.getPartnerContact(userDetails);
        Page<PartnerReport> reports = partnerReportRepository.findByPartnerContactId(contact.getId(), pageable);
        List<PartnerReportResponse> content = reports.stream()
                .map(PartnerReportMapper::mapToPartnerReportResponse)
                .toList();
        return PageResponse.<PartnerReportResponse>builder()
                .content(content)
                .number(reports.getNumber())
                .size(reports.getSize())
                .totalElements(reports.getTotalElements())
                .totalPages(reports.getTotalPages())
                .first(reports.isFirst())
                .last(reports.isLast())
                .build();
    }

    public PartnerReportResponse updateReport(
            UUID reportId,
            UpdatePartnerReportRequest request,
            CustomUserDetails userDetails
    ) {
        User actor = reportCommonService.getCurrentUser(userDetails);
        PartnerReport report = reportCommonService.getReportForUpdate(reportId);
        reportCommonService.ensureOwner(report, actor);
        reportCommonService.ensureEditable(report);

        report.setGeneralComments(report.getGeneralComments());

        return PartnerReportMapper.mapToPartnerReportResponse(
                partnerReportRepository.save(report)
        );
    }

    @Transactional
    public void deleteReport(UUID reportId, CustomUserDetails userDetails) {
        final User actor = reportCommonService.getCurrentUser(userDetails);
        PartnerReport report = reportCommonService.getReportForUpdate(reportId);
        reportCommonService.ensureOwner(report, actor);

        if (report.getStatus() != ReportStatus.DRAFT) {
            throw reportCommonService.conflict("Seul un brouillon peut être supprimé");
        }

        partnerReportRepository.delete(report);
    }

    @Transactional(readOnly = true)
    public PartnerReportDetailResponse getReportDetailById(
            UUID reportId,
            CustomUserDetails userDetails
    ) {
        final PartnerReportResponse report = getReportById(reportId, userDetails);
        final FinancialSummaryDto summary = financialSummaryService.calculate(reportId);
        return PartnerReportDetailResponse.builder()
                .report(report)
                .financial_summary(summary)
                .build();
    }

    // inside method
    private void saveHistory(
            PartnerReport report,
            ReportStatus previous,
            ReportStatus next,
            User actor,
            String comment
    ) {
        ReportStatusHistory history = ReportStatusHistory.builder()
                .partnerReport(report)
                .previousStatus(previous)
                .newStatus(next)
                .actionBy(actor)
                .comment(comment)
                .build();
        reportStatusHistoryRepository.save(history);
    }
}
