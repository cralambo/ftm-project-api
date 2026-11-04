package org.ftm.api.report;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FinancialSummaryService {

    private static final BigDecimal ZERO = BigDecimal.ZERO;
    private static final BigDecimal HUNDRED = new BigDecimal("100");

    private final PartnerReportRepository reportRepository;
    private final FinancialReportItemRepository financialReportItemRepository;

    @Transactional(readOnly = true)
    public FinancialSummaryDto calculate(UUID reportId) {
        PartnerReport report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No report found"));

        BigDecimal budget = report.getDisbursementSchedule().getDisbursementAmount();

        if (budget == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_CONTENT,
                    "No disbursement schedule found"
            );
        }

        BigDecimal expenses = financialReportItemRepository.sumTotalAmountByReportId(reportId);

        if (expenses == null) {
            expenses = ZERO;
        }

        expenses = expenses.setScale(2, RoundingMode.HALF_UP);

        BigDecimal remaining = budget.subtract(expenses).setScale(2, RoundingMode.HALF_UP);

        BigDecimal overrun = expenses.subtract(budget).max(ZERO).setScale(2, RoundingMode.HALF_UP);

        BigDecimal consumptionRate = null;

        if (budget.compareTo(ZERO) > 0) {
            consumptionRate = expenses.multiply(HUNDRED).divide(budget, 2, RoundingMode.HALF_UP);
        }

        String budgetStatus;

        if (expenses.compareTo(budget) > 0) {
            budgetStatus = "OVER_BUDGET";
        } else if (expenses.compareTo(budget) == 0) {
            budgetStatus = "FULLY_CONSUMED";
        } else {
            budgetStatus = "WITH_REMAINDER";
        }

        assert consumptionRate != null;
        return FinancialSummaryDto.builder()
                .report_id(report.getId().toString())
                .disbursement_schedule_id(report.getDisbursementSchedule().getId().toString())
                .schedule_reference(report.getDisbursementSchedule().getReference())
                .budget_status(budgetStatus)
                .budget_amount(
                        budget.setScale(2, RoundingMode.HALF_UP).toPlainString()
                )
                .total_expenses(expenses.toPlainString())
                .remaining_amount(remaining.toPlainString())
                .overrun_amount(overrun.toPlainString())
                .consumption_rate(consumptionRate.toPlainString())
                .build();
    }

    @Transactional(readOnly = true)
    public void validateBudgetForSubmission(UUID reportId) {

        validateFinancialItems(reportId);

        FinancialSummaryDto summary = calculate(reportId);

        BigDecimal budget = new BigDecimal(summary.budget_amount());
        BigDecimal expenses = new BigDecimal(summary.total_expenses());

        if (budget.compareTo(ZERO) <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_CONTENT,
                    "Le budget doit être strictement positif pour soumettre le rapport."
            );
        }

        if (expenses.compareTo(ZERO) < 0) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_CONTENT,
                    "Le montant total des dépenses ne peut pas être négatif."
            );
        }

        if (expenses.compareTo(budget) > 0) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_CONTENT,
                    "La soumission est impossible : les dépenses dépassent le budget autorisé. "
                            + "Budget : " + budget.toPlainString()
                            + ", dépenses : " + expenses.toPlainString()
            );
        }
    }

    private void validateFinancialItems(UUID reportId) {

        List<FinancialReportItem> items = financialReportItemRepository.findByPartnerReportId(reportId);

        if (items.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_CONTENT,
                    "Le rapport financier doit contenir au moins une dépense."
            );
        }

        for (FinancialReportItem item: items) {
            if (item.getDesignation() == null || item.getDesignation().isBlank()) {
                throw new ResponseStatusException(
                        HttpStatus.UNPROCESSABLE_CONTENT,
                        "Une dépense ne possède pas de désignation."
                );
            }

            if (item.getQuantity() == null || item.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
                throw new ResponseStatusException(
                        HttpStatus.UNPROCESSABLE_CONTENT,
                        "La quantité doit être supérieure à zéro."
                );
            }

            if (item.getUnitAmount() == null || item.getUnitAmount().compareTo(BigDecimal.ZERO) < 0) {
                throw new ResponseStatusException(
                        HttpStatus.UNPROCESSABLE_CONTENT,
                        "Le montant unitaire ne peut pas être négatif."
                );
            }
        }
    }
}
