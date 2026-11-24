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
@RequestMapping("/partner-reports/{report-id}/technical-items")
@RequiredArgsConstructor
public class TechnicalReportItemController {

    private final TechnicalReportItemService technicalReportItemService;

    @GetMapping
    @PreAuthorize("hasAuthority('technicals.reports.view')")
    public ResponseEntity<List<TechnicalReportItemResponse>> list(
            @PathVariable("report-id") UUID reportId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(technicalReportItemService.list(reportId, userDetails));
    }

    @PutMapping("/{activity-id}")
    @PreAuthorize("hasAuthority('technicals.reports.upsert')")
    public ResponseEntity<TechnicalReportItemResponse> upsert(
            @PathVariable("report-id") UUID reportId,
            @PathVariable("activity-id") UUID activityId,
            @Valid @RequestBody UpsertTechnicalReportItemRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(technicalReportItemService.upsert(
                reportId,
                activityId,
                request,
                userDetails
        ));
    }

    @DeleteMapping("/{activity-id}")
    @PreAuthorize("hasAuthority('technicals.reports.delete')")
    public ResponseEntity<Void> deleteTechnicalItem(
            @PathVariable("report-id") UUID reportId,
            @PathVariable("activity-id") UUID activityId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        technicalReportItemService.deleteItem(reportId, activityId, userDetails);
        return ResponseEntity.noContent().build();
    }
}
