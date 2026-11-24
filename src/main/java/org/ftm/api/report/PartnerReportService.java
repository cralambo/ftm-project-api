package org.ftm.api.report;

import lombok.RequiredArgsConstructor;
import org.ftm.api.common.PageResponse;
import org.ftm.api.disbursement_activity.DisbursementActivity;
import org.ftm.api.disbursement_activity.DisbursementActivityRepository;
import org.ftm.api.disbursement_schedule.DisbursementSchedule;
import org.ftm.api.disbursement_schedule.DisbursementScheduleMapper;
import org.ftm.api.disbursement_schedule.DisbursementScheduleRepository;
import org.ftm.api.partner_contact.PartnerContact;
import org.ftm.api.partner_contact.PartnerContactMapper;
import org.ftm.api.security.CustomUserDetails;
import org.ftm.api.user.User;
import org.ftm.api.user.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PartnerReportService {

    private final PartnerReportRepository partnerReportRepository;
    private final DisbursementScheduleRepository disbursementScheduleRepository;
    private final ReportStatusHistoryRepository reportStatusHistoryRepository;
    private final DisbursementActivityRepository disbursementActivityRepository;
    private final TechnicalReportItemRepository technicalReportItemRepository;
    private final FinancialReportItemRepository financialReportItemRepository;
    private final ReportAttachmentRepository reportAttachmentRepository;
    private final UserRepository userRepository;

    private final ReportCommonService reportCommonService;
    private final FinancialSummaryService financialSummaryService;
    private final ReportAttachmentService reportAttachmentService;

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

    @Transactional
    public PartnerReportResponse initializeReport(
            CreatePartnerReportRequest request,
            CustomUserDetails userDetails
    )
    {
        User actor = reportCommonService.getCurrentUser(userDetails);
        PartnerContact contact = reportCommonService.getPartnerContact(userDetails);

        UUID scheduleId;
        try {
            scheduleId = UUID.fromString(request.disbursement_schedule_id());
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Identifiant du calendrier invalide."
            );
        }

        DisbursementSchedule schedule = disbursementScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Calendrier de décaissement introuvable."
                ));

        var existing = partnerReportRepository.findByDisbursementScheduleId(scheduleId);

        if (existing.isPresent()) {
            PartnerReport report = existing.get();

            reportCommonService.ensureCanRead(report, actor);

            return PartnerReportMapper.mapToPartnerReportResponse(report);
        }

        PartnerReport report = PartnerReport.builder()
                .disbursementSchedule(schedule)
                .partnerContact(contact)
                .status(ReportStatus.DRAFT)
                .build();

        PartnerReport saved = partnerReportRepository.saveAndFlush(report);

        saveHistory(
                saved,
                null,
                ReportStatus.DRAFT,
                actor,
                "Création automatique du brouillon depuis le calendrier."
        );

        return PartnerReportMapper.mapToPartnerReportResponse(saved);
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

    @Transactional
    public PartnerReportResponse updateReport(
            UUID reportId,
            UpdatePartnerReportRequest request,
            CustomUserDetails userDetails
    ) {
        User actor = reportCommonService.getCurrentUser(userDetails);
        PartnerReport report = reportCommonService.getReportForUpdate(reportId);
        reportCommonService.ensureOwner(report, actor);
        reportCommonService.ensureEditable(report);

        report.setGeneralComments(request.general_comments());

        PartnerReport savedReport = partnerReportRepository.save(report);

        return PartnerReportMapper.mapToPartnerReportResponse(savedReport);
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

    @Transactional
    public PartnerReportResponse submitReport(
            UUID reportId,
            SubmitPartnerReportRequest request,
            CustomUserDetails userDetails
    ) {
        try {
            User actor = reportCommonService.getCurrentUser(userDetails);

            PartnerReport report = reportCommonService.getReportForUpdate(reportId);
            reportCommonService.ensureOwner(report, actor);
            reportCommonService.ensureEditable(report);

            if (request.general_comments() != null) {
                report.setGeneralComments(
                        request.general_comments().trim()
                );
            }

            validateTechnicalReport(report);

            reportAttachmentService.validateRequiredGeneralAttachments(reportId);

            financialSummaryService.validateBudgetForSubmission(reportId);

            ReportStatus previousStatus = report.getStatus();

            report.setStatus(ReportStatus.SUBMITTED);
            report.setSubmittedAt(LocalDateTime.now());

            PartnerReport savedReport = partnerReportRepository.saveAndFlush(report);

            saveHistory(
                    savedReport,
                    previousStatus,
                    ReportStatus.SUBMITTED,
                    actor,
                    "Soumission du rapport par le partenaire."
            );

            return PartnerReportMapper.mapToPartnerReportResponse(savedReport);

        } catch (Exception e) {
            System.err.println("========== SUBMIT REPORT ERROR ==========");
            System.err.println("Exception: " + e.getClass().getName());
            System.err.println("Message: " + e.getMessage());
            e.printStackTrace();

            throw e;
        }
    }

    private void validateTechnicalReport(PartnerReport report) {
        UUID scheduleId = report.getDisbursementSchedule().getId();

        List<DisbursementActivity> activities = disbursementActivityRepository
                .findByDisbursementScheduleId(scheduleId);

        if (activities.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_CONTENT,
                    "Aucune activité n'est définie dans ce calendrier de décaissement."
            );
        }

        List<TechnicalReportItem> reportItems = technicalReportItemRepository
                .findByPartnerReportId(report.getId());

        for (DisbursementActivity activity: activities) {

            TechnicalReportItem item = reportItems.stream()
                    .filter(i -> i.getDisbursementActivity().getId()
                            .equals(activity.getId()))
                    .findFirst()
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.UNPROCESSABLE_CONTENT,
                            "Le rapport technique est incomplet : certaines activités ne sont pas renseignées."
                    ));

            boolean hasQuantity = item.getAchievedQuantity() != null;
            boolean hasDescription = item.getResultDescription() != null
                    && !item.getResultDescription().isBlank();

            if (!hasQuantity && !hasDescription) {
                throw new ResponseStatusException(
                        HttpStatus.UNPROCESSABLE_CONTENT,
                        "Chaque activité doit contenir une quantité atteinte ou une description du résultat."
                );
            }
        }
    }

    @Transactional(readOnly = true)
    public PageResponse<PartnerReportResponse> getPartnerReportSubmittedAndAssignedToIsNull(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("submittedAt").ascending());
        Page<PartnerReport> reports = partnerReportRepository.findByStatusAndAssignedToIsNull(
                ReportStatus.SUBMITTED,
                pageable
        );
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

    @Transactional
    public PartnerReportResponse assignReportToMe(UUID reportId, CustomUserDetails currentUser) {
        PartnerReport report = partnerReportRepository.findByIdForUpdate(reportId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNPROCESSABLE_CONTENT,
                        "Rapport introuvable"
                ));
        if (report.getStatus() != ReportStatus.SUBMITTED) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_CONTENT,
                    "Seuls les rapports soumis peuvent être assignés."
            );
        }
        if (report.getAssignedTo() != null) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_CONTENT,
                    "Ce rapport est déjà assigné à un utilisateur."
            );
        }

        User actor = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNPROCESSABLE_CONTENT,
                        "Rapport introuvable"
                ));

        report.setAssignedTo(actor);
        report.setAssignedAt(LocalDateTime.now());
        report.setStatus(ReportStatus.UNDER_REVIEW);

        PartnerReport savedReport = partnerReportRepository.save(report);
        return PartnerReportMapper.mapToPartnerReportResponse(savedReport);
    }

    @Transactional(readOnly = true)
    public PageResponse<PartnerReportResponse> getReportsToMe(int page, int size, CustomUserDetails currentUser) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("assignedAt").ascending());
        Page<PartnerReport> reports = partnerReportRepository.findByAssignedToIdAndStatus(
                currentUser.getId(),
                ReportStatus.UNDER_REVIEW,
                pageable
        );
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

    @Transactional(readOnly = true)
    public PartnerReportUnderReviewResponse getAssignedReport(UUID reportId, CustomUserDetails currentUser) {
        PartnerReport report = partnerReportRepository.findByIdAndAssignedToIdAndStatus(
                reportId,
                currentUser.getId(),
                ReportStatus.UNDER_REVIEW
        ).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.UNPROCESSABLE_CONTENT,
                "Rapport introuvable ou non assigné à cet utilisateur"
        ));

        List<TechnicalReportItem> technicalReportItems = technicalReportItemRepository.findByPartnerReportId(reportId);
        List<FinancialReportItem> financialReportItems = financialReportItemRepository.findByPartnerReportId(reportId);

        List<ReportAttachment> attachments = reportAttachmentRepository.findByPartnerReportId(reportId);

        List<ReportAttachmentResponse> generalDocuments = attachments.stream()
                .filter(attachment -> attachment.getTechnicalReportItem() == null
                        && attachment.getFinancialReportItem() == null
                )
                .map(ReportAttachmentMapper::toAttachmentResponse)
                .toList();

        // Construire le rapport technique
        List<TechnicalReportItemWithAttachmentResponse> technicalItems = technicalReportItems
                .stream()
                .map(item ->{
                    List<ReportAttachmentResponse> documents = attachments
                            .stream()
                            .filter(attachment -> attachment.getTechnicalReportItem() != null
                                    && attachment.getTechnicalReportItem()
                                    .getId().equals(item.getId())
                            )
                            .map(ReportAttachmentMapper::toAttachmentResponse)
                            .toList();
                    return TechnicalReportItemMapper.mapToTechnicalReportItemWithAttachmentResponse(item, documents);
                })
                .toList();

        // Construire le rapport financier
        List<FinancialReportItemWithAttachmentResponse> financialItems = financialReportItems
                .stream()
                .map(item -> {
                    List<ReportAttachmentResponse> documents = attachments.stream()
                            .filter(attachment -> attachment.getFinancialReportItem() != null
                                    && attachment.getFinancialReportItem()
                                    .getId().equals(item.getId())
                            )
                            .map(ReportAttachmentMapper::toAttachmentResponse)
                            .toList();
                    return FinancialReportItemMapper.mapToFinancialReportItemWithAttachmentResponse(item, documents);
                })
                .toList();

        return PartnerReportUnderReviewResponse.builder()
                .id(report.getId().toString())
                .disbursement_schedule(DisbursementScheduleMapper.mapToShortDisbursementScheduleResponse(report.getDisbursementSchedule()))
                .partner_contact(PartnerContactMapper.mapToPartnerContactResponse(report.getPartnerContact()))
                .status(report.getStatus().name())
                .general_comments(report.getGeneralComments())
                .assigned_at(report.getAssignedAt() == null ? null : report.getAssignedAt().toString())
                .assigned_to(report.getAssignedTo().getFirstname() + " " + report.getAssignedTo().getLastname())
                .submitted_at(report.getSubmittedAt() == null ? null : report.getSubmittedAt().toString())
                .technical_report(technicalItems)
                .financial_report(financialItems)
                .documents(generalDocuments)
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
