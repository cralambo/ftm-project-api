package org.ftm.api.landscape;

import lombok.Builder;

@Builder
public record LandscapeResponse(
        String landscape_id,
        String name
) {
}
