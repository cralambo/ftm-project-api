package org.ftm.api.landscape;

import lombok.Builder;

@Builder
public record ShortLandscapeResponse(
        String landscape_id,
        String name
) {
}
