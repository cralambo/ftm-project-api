package org.ftm.api.report;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
                    HttpStatus.UNPROCESSABLE_ENTITY,
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

        FinancialSummaryDto summary = calculate(reportId);

        if (new BigDecimal(summary.budget_amount()).compareTo(ZERO) <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "Le budget doit être strictement positif pour soumettre le rapport."
            );
        }

        if (new BigDecimal(summary.total_expenses()).compareTo(new BigDecimal(summary.budget_amount())) > 0) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "La soumission est impossible : les dépenses dépassent le budget autorisé."
            );
        }
    }
}
