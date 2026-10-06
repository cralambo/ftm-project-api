package org.ftm.api.district;

import lombok.Builder;

@Builder
public record DistrictRequest(
        String code,
        String name,
        String region_id
) {
}
