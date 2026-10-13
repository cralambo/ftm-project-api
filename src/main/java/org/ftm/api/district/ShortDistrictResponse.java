package org.ftm.api.district;

import lombok.Builder;

@Builder
public record ShortDistrictResponse(
        String id,
        String code,
        String name
) {
}
