package org.ftm.api.topic;

import lombok.RequiredArgsConstructor;
import org.ftm.api.common.PageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/topics")
public class TopicController {

    private final TopicService topicService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('topics.create')")
    public ResponseEntity<String> createTopic(@RequestBody TopicRequest request) {
        return ResponseEntity.ok(topicService.createTopic(request));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('topics.view')")
    public ResponseEntity<PageResponse<TopicResponse>> getTopics(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        return ResponseEntity.ok(topicService.getTopics(page, size));
    }

    @GetMapping("/{topic-id}")
    @PreAuthorize("hasAuthority('topics.view')")
    public ResponseEntity<TopicResponse> getTopicById(@PathVariable("topic-id") UUID topicId) {
        return ResponseEntity.ok(topicService.getTopicById(topicId));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('topics.view')")
    public ResponseEntity<List<ShortTopicResponse>> getAllTopics() {
        return ResponseEntity.ok(topicService.getAllTopics());
    }

    @PutMapping("/update/{topic-id}")
    @PreAuthorize("hasAuthority('topics.update')")
    public ResponseEntity<String> updateTopic(
            @PathVariable("topic-id") UUID topicId,
            @RequestBody TopicRequest request
    ) {
        return ResponseEntity.ok(topicService.updateTopic(topicId, request));
    }

    @DeleteMapping("/{topic-id}")
    @PreAuthorize("hasAuthority('topics.delete')")
    public ResponseEntity<Void> deleteTopic(@PathVariable("topic-id") UUID topicId) {
        topicService.deleteTopic(topicId);
        return ResponseEntity.noContent().build();
    }
}
