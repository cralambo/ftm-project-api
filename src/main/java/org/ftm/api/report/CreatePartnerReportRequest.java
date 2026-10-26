package org.ftm.api.report;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CreatePartnerReportRequest(
        @NotBlank String disbursement_schedule_id
) {
}
