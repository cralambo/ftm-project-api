package org.ftm.api.topic;

import lombok.Builder;

@Builder
public record ShortTopicResponse(
        String id,
        String name
) {
}
