package org.ftm.api.report;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ftm.api.security.CustomUserDetails;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<FinancialReportItemResponse>> list(
            @PathVariable("report-id") UUID reportId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(financialReportItemService.list(reportId, userDetails));
    }

    @PostMapping("/financial-reports")
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
    public ResponseEntity<Void> deleteFinancialItem(
            @PathVariable("report-id") UUID reportId,
            @PathVariable("item-id") UUID itemId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        financialReportItemService.deleteFinancialItem(reportId, itemId, userDetails);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/financial-summary")
    public ResponseEntity<FinancialSummaryDto> summary(
            @PathVariable("report-id") UUID reportId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        return ResponseEntity.ok(financialReportItemService.summary(reportId, userDetails));
    }
}
