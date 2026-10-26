package org.ftm.api.report;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.ftm.api.security.CustomUserDetails;
import org.ftm.api.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FinancialReportItemService {

    private final ReportCommonService reportCommonService;
    private final FinancialSummaryService financialSummaryService;
    private final FinancialReportItemRepository financialReportItemRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public List<FinancialReportItemResponse> list(UUID reportId, CustomUserDetails userDetails) {
        PartnerReport report = reportCommonService.getReport(reportId);
        User actor = reportCommonService.getCurrentUser(userDetails);
        reportCommonService.ensureCanRead(report, actor);
        return financialReportItemRepository.findByPartnerReportId(reportId)
                .stream()
                .map(FinancialReportItemMapper::mapToFinancialReportItemResponse)
                .toList();
    }

    @Transactional
    public FinancialReportItemResponse createFinancialItem(
            UUID reportId,
            CreateFinancialReportItemRequest request,
            CustomUserDetails userDetails
    ) {
        final User actor = reportCommonService.getCurrentUser(userDetails);
        final PartnerReport report = reportCommonService.getReportForUpdate(reportId);
        reportCommonService.ensureOwner(report, actor);
        reportCommonService.ensureEditable(report);

        FinancialReportItem item = FinancialReportItem.builder()
                .partnerReport(report)
                .build();
        apply(
                item,
                request.designation(),
                request.quantity(),
                request.unit_amount(),
                request.observations()

        );
        final FinancialReportItem saved = financialReportItemRepository.save(item);
        final FinancialReportItem reloaded = financialReportItemRepository.findById(saved.getId()).orElseThrow();
        return FinancialReportItemMapper.mapToFinancialReportItemResponse(reloaded);
    }

    private void apply(
            FinancialReportItem item,
            String designation,
            String quantity,
            String unitAmount,
            String observations
    ){
        item.setDesignation(designation);
        item.setQuantity(
                quantity == null ? null : new BigDecimal(quantity)
        );
        item.setUnitAmount(
                unitAmount == null ? null : new BigDecimal(unitAmount)
        );
        item.setObservations(observations);
    }

    @Transactional
    public FinancialReportItemResponse updateFinancialItem(
            UUID reportId,
            UUID itemId,
            UpdateFinancialReportItemRequest request,
            CustomUserDetails userDetails
    ) {
        final User actor = reportCommonService.getCurrentUser(userDetails);
        final PartnerReport report = reportCommonService.getReportForUpdate(reportId);
        reportCommonService.ensureOwner(report, actor);
        reportCommonService.ensureEditable(report);

        FinancialReportItem item = financialReportItemRepository.findByIdAndPartnerReportId(itemId, reportId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Dépense introuvable dans ce rapport."
                ));
        apply(item, request.designation(), request.quantity(), request.unit_amount(), request.observations());
        FinancialReportItem saved = financialReportItemRepository.saveAndFlush(item);
        entityManager.refresh(saved);
        FinancialReportItem reloaded = financialReportItemRepository.findById(itemId).orElseThrow();
        return FinancialReportItemMapper.mapToFinancialReportItemResponse(reloaded);
    }

    @Transactional
    public void deleteFinancialItem(
            UUID reportId,
            UUID itemId,
            CustomUserDetails userDetails
    ) {
        final User actor = reportCommonService.getCurrentUser(userDetails);
        final PartnerReport report = reportCommonService.getReportForUpdate(reportId);
        reportCommonService.ensureOwner(report, actor);
        reportCommonService.ensureEditable(report);

        final FinancialReportItem item = financialReportItemRepository.findByIdAndPartnerReportId(itemId, reportId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Dépense introuvable dans ce rapport."
                ));
        financialReportItemRepository.delete(item);
    }

    @Transactional(readOnly = true)
    public FinancialSummaryDto summary(UUID reportId, CustomUserDetails userDetails) {
        final PartnerReport report = reportCommonService.getReport(reportId);
        final User actor = reportCommonService.getCurrentUser(userDetails);
        reportCommonService.ensureCanRead(report, actor);
        return financialSummaryService.calculate(reportId);
    }
}
