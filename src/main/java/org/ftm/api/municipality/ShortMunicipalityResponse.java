package org.ftm.api.municipality;

import lombok.Builder;

@Builder
public record ShortMunicipalityResponse(
        String id,
        String name
) {
}
