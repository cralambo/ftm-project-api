package org.ftm.api.landscape;

import lombok.Builder;

@Builder
public record ShortLandscapeResponse(
        String id,
        String name
) {
}
