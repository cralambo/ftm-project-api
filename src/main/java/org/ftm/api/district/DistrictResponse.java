package org.ftm.api.district;

import lombok.Builder;
import org.ftm.api.region.RegionResponse;

@Builder
public record DistrictResponse(
        String district_id,
        String code,
        String name,
        RegionResponse region
) {
}
