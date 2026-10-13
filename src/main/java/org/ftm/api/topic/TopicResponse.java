package org.ftm.api.topic;

import lombok.Builder;

@Builder
public record TopicResponse(
        String id,
        String name,
        String description
) {
}
