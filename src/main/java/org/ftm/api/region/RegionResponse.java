package org.ftm.api.region;

import lombok.Builder;

@Builder
public record RegionResponse(
        String region_id,
        String name
) {
}
