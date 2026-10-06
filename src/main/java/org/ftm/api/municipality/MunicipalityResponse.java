package org.ftm.api.municipality;

import lombok.Builder;
import org.ftm.api.district.ShortDistrictResponse;

@Builder
public record MunicipalityResponse(
        String municipality_id,
        String name,
        ShortDistrictResponse district
) {
}
