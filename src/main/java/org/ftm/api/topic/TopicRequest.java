package org.ftm.api.topic;

import lombok.Builder;

@Builder
public record TopicRequest(
        String name,
        String description
) {
}
