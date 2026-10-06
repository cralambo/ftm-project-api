package org.ftm.api.sub_landscape;

import lombok.Builder;
import org.ftm.api.landscape.ShortLandscapeResponse;

@Builder
public record SubLandscapeResponse(
        String sub_landscape_id,
        String name,
        ShortLandscapeResponse landscape
) {
}
