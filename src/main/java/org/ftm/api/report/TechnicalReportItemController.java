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
@RequestMapping("/partner-reports/{report-id}/technical-items")
@RequiredArgsConstructor
public class TechnicalReportItemController {

    private final TechnicalReportItemService technicalReportItemService;

    @GetMapping
    public ResponseEntity<List<TechnicalReportItemResponse>> list(
            @PathVariable("report-id") UUID reportId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(technicalReportItemService.list(reportId, userDetails));
    }

    @PutMapping("/{activity-id}")
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
    public ResponseEntity<Void> deleteTechnicalItem(
            @PathVariable("report-id") UUID reportId,
            @PathVariable("activity-id") UUID activityId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        technicalReportItemService.deleteItem(reportId, activityId, userDetails);
        return ResponseEntity.noContent().build();
    }
}
