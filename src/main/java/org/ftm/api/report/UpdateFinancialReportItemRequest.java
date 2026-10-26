package org.ftm.api.report;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UpdateFinancialReportItemRequest(
        @NotBlank
        String designation,
        @NotBlank
        String quantity,
        @NotBlank
        String unit_amount,
        String observations
) {
}
