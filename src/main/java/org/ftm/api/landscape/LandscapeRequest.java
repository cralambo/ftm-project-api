package org.ftm.api.landscape;

import lombok.Builder;

@Builder
public record LandscapeRequest(
        String name
) {
}
