package org.ftm.api.position;

import lombok.Builder;

@Builder
public record PositionResponse(
        String id,
        String code,
        String name
) {
}
