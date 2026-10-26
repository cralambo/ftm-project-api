package org.ftm.api.report;

import lombok.RequiredArgsConstructor;
import org.ftm.api.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PartnerReportSubmissionService {

    private final PartnerReportRepository reportRepository;
    private final TechnicalReportItemRepository technicalReportItemRepository;
    private final FinancialReportItemRepository financialReportItemRepository;
    private final ReportAttachmentRepository reportAttachmentRepository;
    private final ReportStatusHistoryRepository reportStatusHistoryRepository;
    private final FinancialSummaryService financialSummaryService;

    public SubmitReportResponse submit(
            UUID reportId,
            User actor
    ) {
        if (actor == null || actor.getId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Utilisateur non authentifié."
            );
        }

        PartnerReport report = reportRepository.findByIdForUpdate(reportId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Rapport introuvable."
                ));

        if (report.getPartnerContact().getUser() == null
                || !report.getPartnerContact().getUser().getId()
                .equals(actor.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Vous n'êtes pas autorisé à soumettre ce rapport."
            );
        }

        ReportStatus currentStatus = report.getStatus();

        if (currentStatus != ReportStatus.DRAFT && currentStatus != ReportStatus.RETURNED) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Seuls les rapports en brouillon ou retournés peuvent être soumis."
            );
        }

        validateTechnicalItems(report);
        validateFinancialItems(report);
        validateEvidence(report);

        // Calcule et vérifie le budget depuis la base de données.
        financialSummaryService.validateBudgetForSubmission(reportId);

        LocalDateTime now = LocalDateTime.now();

        ReportStatusHistory history = new ReportStatusHistory();
        history.setPartnerReport(report);
        history.setPreviousStatus(currentStatus);
        history.setNewStatus(ReportStatus.SUBMITTED);
        history.setActionBy(actor);
        history.setComment(
                currentStatus == ReportStatus.RETURNED
                        ? "Rapport corrigé et soumis à nouveau."
                        : "Rapport soumis par le partenaire."
        );

        report.setStatus(ReportStatus.SUBMITTED);
        report.setSubmittedAt(now);

        reportRepository.save(report);
        reportStatusHistoryRepository.save(history);

        // Le résumé est relu après les validations et avant la fin
        // de la transaction. Les montants sont calculés en base.
        FinancialSummaryDto summary =
                financialSummaryService.calculate(reportId);

        return new SubmitReportResponse(
                report.getId().toString(),
                report.getStatus().toString(),
                report.getSubmittedAt().toString(),
                summary,
                "Le rapport a été soumis avec succès."
        );
    }

    private void validateTechnicalItems(PartnerReport report) {
        List<TechnicalReportItem> items = technicalReportItemRepository.findByPartnerReportId(report.getId());

        if (items.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "Le rapport doit contenir au moins une ligne technique."
            );
        }

        for (TechnicalReportItem item: items) {
            boolean hasQuantity = item.getAchievedQuantity() != null;
            boolean hasDescription = item.getResultDescription() != null && !item.getResultDescription().isBlank();

            if (!hasQuantity && !hasDescription) {
                throw new ResponseStatusException(
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        "Chaque ligne technique doit contenir une quantité atteinte ou une description du résultat."
                );
            }

            if (item.getDisbursementActivity() == null
                    || item.getDisbursementActivity().getDisbursementSchedule() == null
                    || !item.getDisbursementActivity()
                        .getDisbursementSchedule().getId()
                        .equals(report.getDisbursementSchedule().getId())) {
                throw new ResponseStatusException(
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        "Une activité technique ne correspond pas au calendrier de décaissement du rapport."
                );
            }
        }
    }

    private void validateFinancialItems(PartnerReport report) {
        List<FinancialReportItem> items = financialReportItemRepository.findByPartnerReportId(report.getId());

        if (items.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "Le rapport doit contenir au moins une ligne financière."
            );
        }

        for (FinancialReportItem item : items) {
            if (item.getQuantity() == null
                    || item.getQuantity().signum() <= 0
                    || item.getUnitAmount() == null
                    || item.getUnitAmount().signum() < 0) {

                throw new ResponseStatusException(
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        "Une ligne financière contient une quantité ou un montant invalide."
                );
            }

            if (item.getDesignation() == null
                    || item.getDesignation().isBlank()) {
                throw new ResponseStatusException(
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        "Chaque dépense doit avoir une désignation."
                );
            }
        }
    }

    private void validateEvidence(PartnerReport report) {

        List<TechnicalReportItem> technicalItems = technicalReportItemRepository.findByPartnerReportId(report.getId());

        for (TechnicalReportItem item : technicalItems) {
            long count =
                    reportAttachmentRepository
                            .countByPartnerReportIdAndTechnicalReportItemId(
                                    report.getId(),
                                    item.getId()
                            );

            if (count == 0) {
                throw new ResponseStatusException(
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        "Chaque ligne technique doit comporter au moins une pièce justificative."
                );
            }
        }

        List<FinancialReportItem> financialItems = financialReportItemRepository.findByPartnerReportId(report.getId());

        for (FinancialReportItem item : financialItems) {
            long count =
                    reportAttachmentRepository
                            .countByPartnerReportIdAndFinancialReportItemId(
                                    report.getId(),
                                    item.getId()
                            );

            if (count == 0) {
                throw new ResponseStatusException(
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        "Chaque ligne financière doit comporter au moins une pièce justificative."
                );
            }
        }
    }
}
