package org.ftm.api.sub_landscape;

import lombok.Builder;

@Builder
public record SubLandscapeRequest(
        String name,
        String landscape_id
) {
}
