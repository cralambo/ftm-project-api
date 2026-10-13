package org.ftm.api.co_funder;

import lombok.Builder;

@Builder
public record CoFunderResponse(
        String id,
        String name
) {
}
