package org.ftm.api.district;

import lombok.Builder;

@Builder
public record ShortDistrictResponse(
        String district_id,
        String code,
        String name
) {
}
