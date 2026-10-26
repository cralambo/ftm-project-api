package org.ftm.api.report;

import lombok.Builder;

@Builder
public record PartnerReportDetailResponse(
        PartnerReportResponse report,
        FinancialSummaryDto financial_summary
) {
}
