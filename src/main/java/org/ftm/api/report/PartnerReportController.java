package org.ftm.api.report;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ftm.api.common.PageResponse;
import org.ftm.api.security.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/partner-reports")
@RequiredArgsConstructor
public class PartnerReportController {

    private final PartnerReportService partnerReportService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('reports.create')")
    public ResponseEntity<PartnerReportResponse> createReport(
            @Valid @RequestBody CreatePartnerReportRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        return ResponseEntity.ok(partnerReportService.createReport(request, userDetails));
    }

    @PostMapping("/initialize")
    @PreAuthorize("hasAuthority('reports.create')")
    public ResponseEntity<PartnerReportResponse> initializeReport(
            @Valid @RequestBody CreatePartnerReportRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(partnerReportService.initializeReport(request, userDetails));
    }

    @GetMapping("/{report-id}")
    @PreAuthorize("hasAuthority('reports.view')")
    public ResponseEntity<PartnerReportResponse> getReportById(
            @PathVariable("report-id") UUID reportId,
            @AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.ok(partnerReportService.getReportById(reportId, principal));
    }

    @GetMapping("/my-reports")
    @PreAuthorize("hasAuthority('reports.view')")
    public ResponseEntity<PageResponse<PartnerReportResponse>> getMyReports(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(partnerReportService.getMyReports(page, size, userDetails));
    }

    @PatchMapping("/update/{report-id}")
    @PreAuthorize("hasAuthority('reports.update')")
    public ResponseEntity<PartnerReportResponse> updateReport(
            @PathVariable("report-id") UUID reportId,
            @Valid @RequestBody UpdatePartnerReportRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(partnerReportService.updateReport(reportId, request, userDetails));
    }

    @GetMapping("/detail/{report-id}")
    @PreAuthorize("hasAuthority('reports.view')")
    public ResponseEntity<PartnerReportDetailResponse> getReportDetailById(
            @PathVariable("report-id") UUID reportId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(partnerReportService.getReportDetailById(reportId, userDetails));
    }

    @DeleteMapping("/{report-id}")
    @PreAuthorize("hasAuthority('reports.delete')")
    public ResponseEntity<Void> deleteReport(
            @PathVariable("report-id") UUID reportId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        partnerReportService.deleteReport(reportId, userDetails);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{report-id}/submit")
    @PreAuthorize("hasAuthority('reports.submit')")
    public ResponseEntity<PartnerReportResponse> submitReport(
            @PathVariable("report-id") UUID reportId,
            @RequestBody SubmitPartnerReportRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(partnerReportService.submitReport(reportId, request,userDetails));
    }

    @GetMapping("/submitted")
    @PreAuthorize("hasAuthority('reports.submitted.view')")
    public ResponseEntity<PageResponse<PartnerReportResponse>> getPartnerReportSubmittedAndAssignedToIsNull(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        return ResponseEntity.ok(partnerReportService.getPartnerReportSubmittedAndAssignedToIsNull(page, size));
    }

    @PostMapping("/{report-id}/assign")
    @PreAuthorize("hasAuthority('reports.submitted.assign')")
    public ResponseEntity<PartnerReportResponse> assignReportToMe(
            @PathVariable("report-id") UUID reportId,
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        return ResponseEntity.ok(partnerReportService.assignReportToMe(reportId, currentUser));
    }

    @GetMapping("/assigned")
    @PreAuthorize("hasAuthority('reports.submitted.assign')")
    public ResponseEntity<PageResponse<PartnerReportResponse>> getReportsToMe(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        return ResponseEntity.ok(partnerReportService.getReportsToMe(page, size, currentUser));
    }

    @GetMapping("/detail/{report-id}/assigned")
    public ResponseEntity<PartnerReportUnderReviewResponse> getAssignedReport(
            @PathVariable("report-id") UUID reportId,
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        return ResponseEntity.ok(partnerReportService.getAssignedReport(reportId, currentUser));
    }


}
