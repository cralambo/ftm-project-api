package org.ftm.api.disbursement_activity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record QualitativeActivityRequest(
        @NotBlank @Size(max = 255) String name,
        String description,
        @NotBlank String success_criteria
) implements DisbursementActivityRequest {}
