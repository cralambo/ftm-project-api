package org.ftm.api.report;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record FinancialReportItemRequest(
        @NotBlank
        @Size(max = 255)
        String designation,

        @NotBlank
        String quantity,

        @NotBlank
        String unit_amount,

        String observations
) {
}
