package org.ftm.api.topic;

import lombok.RequiredArgsConstructor;
import org.ftm.api.common.PageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/topics")
public class TopicController {

    private final TopicService topicService;

    @PostMapping("/create")
    public ResponseEntity<String> createTopic(@RequestBody TopicRequest request) {
        return ResponseEntity.ok(topicService.createTopic(request));
    }

    @GetMapping
    public ResponseEntity<PageResponse<TopicResponse>> getTopics(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        return ResponseEntity.ok(topicService.getTopics(page, size));
    }

    @GetMapping("/{topic-id}")
    public ResponseEntity<TopicResponse> getTopicById(@PathVariable("topic-id") UUID topicId) {
        return ResponseEntity.ok(topicService.getTopicById(topicId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ShortTopicResponse>> getAllTopics() {
        return ResponseEntity.ok(topicService.getAllTopics());
    }

    @PutMapping("/update/{topic-id}")
    public ResponseEntity<String> updateTopic(
            @PathVariable("topic-id") UUID topicId,
            @RequestBody TopicRequest request
    ) {
        return ResponseEntity.ok(topicService.updateTopic(topicId, request));
    }

    @DeleteMapping("/{topic-id}")
    public ResponseEntity<Void> deleteTopic(@PathVariable("topic-id") UUID topicId) {
        topicService.deleteTopic(topicId);
        return ResponseEntity.noContent().build();
    }
}
