package org.ftm.api.disbursement_activity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record QuantitativeActivityRequest(
        @NotBlank @Size(max = 255) String name,
        String description,
        @NotBlank String target_quantity,
        @NotBlank @Size(max = 50) String unit
) implements DisbursementActivityRequest {}
