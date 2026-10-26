package org.ftm.api.report;

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
}
