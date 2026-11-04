package org.ftm.api.report;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ftm.api.common.PageResponse;
import org.ftm.api.security.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/partner-reports")
@RequiredArgsConstructor
public class PartnerReportController {

    private final PartnerReportService partnerReportService;

    @PostMapping("/create")
    public ResponseEntity<PartnerReportResponse> createReport(
            @Valid @RequestBody CreatePartnerReportRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        return ResponseEntity.ok(partnerReportService.createReport(request, userDetails));
    }

    @PostMapping("/initialize")
    public ResponseEntity<PartnerReportResponse> initializeReport(
            @Valid @RequestBody CreatePartnerReportRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(partnerReportService.initializeReport(request, userDetails));
    }

    @GetMapping("/{report-id}")
    public ResponseEntity<PartnerReportResponse> getReportById(
            @PathVariable("report-id") UUID reportId,
            @AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.ok(partnerReportService.getReportById(reportId, principal));
    }

    @GetMapping("/my-reports")
    public ResponseEntity<PageResponse<PartnerReportResponse>> getMyReports(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(partnerReportService.getMyReports(page, size, userDetails));
    }

    @PatchMapping("/update/{report-id}")
    public ResponseEntity<PartnerReportResponse> updateReport(
            @PathVariable("report-id") UUID reportId,
            @Valid @RequestBody UpdatePartnerReportRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(partnerReportService.updateReport(reportId, request, userDetails));
    }

    @GetMapping("/detail/{report-id}")
    public ResponseEntity<PartnerReportDetailResponse> getReportDetailById(
            @PathVariable("report-id") UUID reportId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(partnerReportService.getReportDetailById(reportId, userDetails));
    }

    @DeleteMapping("/{report-id}")
    public ResponseEntity<Void> deleteReport(
            @PathVariable("report-id") UUID reportId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        partnerReportService.deleteReport(reportId, userDetails);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{report-id}/submit")
    public ResponseEntity<PartnerReportResponse> submitReport(
            @PathVariable("report-id") UUID reportId,
            @RequestBody SubmitPartnerReportRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(partnerReportService.submitReport(reportId, request,userDetails));
    }


}
