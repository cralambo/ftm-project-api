package org.ftm.api.position;

import lombok.Builder;

@Builder
public record ShortPositionResponse(
        String position_id,
        String code,
        String name
) {
}
