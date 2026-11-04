package org.ftm.api.report;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record SubmitPartnerReportRequest(
        @NotBlank(message = "Le commentaire est obligatoire.")
        String general_comments
) {
}
