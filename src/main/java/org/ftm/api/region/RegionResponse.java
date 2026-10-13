package org.ftm.api.region;

import lombok.Builder;

@Builder
public record RegionResponse(
        String id,
        String name
) {
}
