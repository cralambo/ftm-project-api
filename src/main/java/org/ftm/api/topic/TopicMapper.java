package org.ftm.api.topic;

public class TopicMapper {
    public static Topic mapToTopicCreateRequest(TopicRequest request) {
        return Topic.builder()
                .name(request.name())
                .description(request.description())
                .build();
    }

    public static TopicResponse mapToTopicResponse(Topic topic) {
        return TopicResponse.builder()
                .id(topic.getId().toString())
                .name(topic.getName())
                .description(topic.getDescription())
                .build();
    }

    public static ShortTopicResponse mapToShortTopicResponse(Topic topic) {
        return ShortTopicResponse.builder()
                .id(topic.getId().toString())
                .name(topic.getName())
                .build();
    }
}
