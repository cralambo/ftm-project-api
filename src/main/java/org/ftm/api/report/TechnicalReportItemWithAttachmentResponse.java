package org.ftm.api.report;

import lombok.Builder;

import java.util.List;

@Builder
public record TechnicalReportItemWithAttachmentResponse(
        String id,
        String disbursement_activity_id,
        String activity_name,
        String achieved_quantity,
        String unit,
        String result_description,
        String observations,
        List<ReportAttachmentResponse> documents
) {
}
