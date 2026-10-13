package org.ftm.api.sub_landscape;

import lombok.Builder;
import org.ftm.api.landscape.ShortLandscapeResponse;

@Builder
public record SubLandscapeResponse(
        String id,
        String name,
        ShortLandscapeResponse landscape
) {
}
