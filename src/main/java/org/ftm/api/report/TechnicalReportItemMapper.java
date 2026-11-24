package org.ftm.api.report;

import java.util.List;

public class TechnicalReportItemMapper {
    public static TechnicalReportItemResponse mapToTechnicalReportItemResponse(
            TechnicalReportItem item
    ) {
        return TechnicalReportItemResponse.builder()
                .id(item.getId().toString())
                .disbursement_activity_id(item.getDisbursementActivity().getId().toString())
                .activity_name(item.getDisbursementActivity().getName())
                .achieved_quantity(
                        item.getAchievedQuantity() == null
                                ? null
                                : item.getAchievedQuantity().toPlainString()
                )
                .achieved_unit(item.getAchievedUnit())
                .result_description(item.getResultDescription())
                .observations(item.getObservations())
                .build();
    }

    public static TechnicalReportItemWithAttachmentResponse mapToTechnicalReportItemWithAttachmentResponse(
            TechnicalReportItem item,
            List<ReportAttachmentResponse> documents
    ) {
        return TechnicalReportItemWithAttachmentResponse.builder()
                .id(item.getId().toString())
                .disbursement_activity_id(item.getDisbursementActivity().getId().toString())
                .achieved_quantity(item.getAchievedQuantity() == null ? null : item.getAchievedQuantity().toPlainString())
                .unit(item.getAchievedUnit())
                .observations(item.getObservations())
                .result_description(item.getResultDescription())
                .documents(documents)
                .build();
    }
}
