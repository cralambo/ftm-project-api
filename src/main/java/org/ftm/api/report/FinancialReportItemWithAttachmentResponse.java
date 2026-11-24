package org.ftm.api.report;

import lombok.Builder;

import java.util.List;

@Builder
public record FinancialReportItemWithAttachmentResponse(
        String id,
        String designation,
        String quantity,
        String unit_amount,
        String total_amount,
        String observations,
        List<ReportAttachmentResponse> documents
) {
}
