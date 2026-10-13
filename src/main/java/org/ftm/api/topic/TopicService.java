package org.ftm.api.topic;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.ftm.api.common.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;

    public String createTopic(TopicRequest request) {
        Topic topic = TopicMapper.mapToTopicCreateRequest(request);
        return topicRepository.save(topic).getId().toString();
    }

    public PageResponse<TopicResponse> getTopics(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Topic> topics = topicRepository.findAll(pageable);
        List<TopicResponse> content = topics
                .stream()
                .map(TopicMapper::mapToTopicResponse)
                .toList();
        return PageResponse.<TopicResponse>builder()
                .content(content)
                .number(topics.getNumber())
                .size(topics.getSize())
                .totalElements(topics.getTotalElements())
                .totalPages(topics.getTotalPages())
                .first(topics.isFirst())
                .last(topics.isLast())
                .build();
    }

    public TopicResponse getTopicById(UUID topicId) {
        return topicRepository.findById(topicId)
                .map(TopicMapper::mapToTopicResponse)
                .orElseThrow(() -> new EntityNotFoundException("No topic found with id: " + topicId));
    }

    public List<ShortTopicResponse> getAllTopics() {
        List<Topic> topics = topicRepository.findAll(Sort.by("createdDate").descending());
        return topics.stream()
                .map(TopicMapper::mapToShortTopicResponse)
                .toList();
    }

    public String updateTopic(UUID topicId, TopicRequest request) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new EntityNotFoundException("No topic found with id: " + topicId));
        topic.setName(request.name());
        topic.setDescription(request.description());
        return topicRepository.save(topic).getId().toString();
    }

    public void deleteTopic(UUID topicId) {
        final Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new EntityNotFoundException("No topic found with id: " + topicId));
        topicRepository.delete(topic);
    }
}
