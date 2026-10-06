package org.ftm.api.municipality;

import lombok.Builder;

@Builder
public record ShortMunicipalityResponse(
        String municipality_id,
        String name
) {
}
