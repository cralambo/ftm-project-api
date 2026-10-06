package org.ftm.api.position;

import lombok.Builder;

@Builder
public record PositionRequest(
        String code,
        String name
) {
}
