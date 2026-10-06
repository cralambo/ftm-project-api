package org.ftm.api.municipality;

import lombok.Builder;

@Builder
public record MunicipalityRequest(
        String name,
        String district_id
) {
}
