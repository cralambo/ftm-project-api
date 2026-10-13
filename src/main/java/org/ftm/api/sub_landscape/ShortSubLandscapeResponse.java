package org.ftm.api.sub_landscape;

import lombok.Builder;

@Builder
public record ShortSubLandscapeResponse(
        String id,
        String name
) {
}
