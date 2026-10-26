package org.ftm.api.report;

public class FinancialReportItemMapper {
    public static FinancialReportItemResponse mapToFinancialReportItemResponse(FinancialReportItem item) {
        return FinancialReportItemResponse.builder()
                .id(item.getId().toString())
                .designation(item.getDesignation())
                .quantity(
                        item.getQuantity() == null
                                ? null
                                : item.getQuantity().toPlainString()
                )
                .unit_amount(
                        item.getUnitAmount() == null
                                ? null
                                : item.getUnitAmount().toPlainString()
                )
                .total_amount(
                        item.getTotalAmount() == null
                                ? null
                                : item.getTotalAmount().toPlainString()
                )
                .observations(item.getObservations())
                .build();
    }
}
