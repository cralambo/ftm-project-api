package org.ftm.api.position;

import lombok.Builder;

@Builder
public record ShortPositionResponse(
        String id,
        String code,
        String name
) {
}
