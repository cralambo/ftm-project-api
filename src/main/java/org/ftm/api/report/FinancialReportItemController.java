package org.ftm.api.report;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ftm.api.security.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/partner-reports/{report-id}")
@RequiredArgsConstructor
public class FinancialReportItemController {

    private final FinancialReportItemService financialReportItemService;

    @GetMapping("/financial-items")
    @PreAuthorize("hasAuthority('financials.reports.view')")
    public ResponseEntity<List<FinancialReportItemResponse>> list(
            @PathVariable("report-id") UUID reportId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(financialReportItemService.list(reportId, userDetails));
    }

    @PostMapping("/financial-reports")
    @PreAuthorize("hasAuthority('financials.reports.create')")
    public ResponseEntity<FinancialReportItemResponse> createFinancialItem(
            @PathVariable("report-id") UUID reportId,
            @Valid @RequestBody CreateFinancialReportItemRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(financialReportItemService.createFinancialItem(
                reportId,
                request,
                userDetails
        ));
    }

    @PutMapping("/fianncial-items/{item-id}")
    @PreAuthorize("hasAuthority('financials.reports.update')")
    public ResponseEntity<FinancialReportItemResponse> updateFinancialItem(
            @PathVariable("report-id") UUID reportId,
            @PathVariable("item-id") UUID itemId,
            @Valid @RequestBody UpdateFinancialReportItemRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(financialReportItemService.updateFinancialItem(
                reportId,
                itemId,
                request,
                userDetails
        ));
    }

    @DeleteMapping("/financial-items/{item-id}")
    @PreAuthorize("hasAuthority('financials.reports.delete')")
    public ResponseEntity<Void> deleteFinancialItem(
            @PathVariable("report-id") UUID reportId,
            @PathVariable("item-id") UUID itemId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        financialReportItemService.deleteFinancialItem(reportId, itemId, userDetails);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/financial-summary")
    @PreAuthorize("hasAuthority('financials.reports.view')")
    public ResponseEntity<FinancialSummaryDto> summary(
            @PathVariable("report-id") UUID reportId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        return ResponseEntity.ok(financialReportItemService.summary(reportId, userDetails));
    }
}
