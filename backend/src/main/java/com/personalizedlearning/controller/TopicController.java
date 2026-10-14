package com.personalizedlearning.controller;

import com.personalizedlearning.dto.TopicDto;
import com.personalizedlearning.service.TopicService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/topics")
public class TopicController {

    private final TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @GetMapping
    public ResponseEntity<List<TopicDto>> getAllTopics() {
        return ResponseEntity.ok(topicService.getAllTopics());
    }

    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<List<TopicDto>> getTopicsBySubject(@PathVariable Long subjectId) {
        return ResponseEntity.ok(topicService.getTopicsBySubject(subjectId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TopicDto> getTopicById(@PathVariable Long id) {
        return ResponseEntity.ok(topicService.getTopicById(id));
    }

    @PostMapping
    public ResponseEntity<TopicDto> createTopic(@Valid @RequestBody TopicDto dto) {
        TopicDto created = topicService.createTopic(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TopicDto> updateTopic(@PathVariable Long id, @RequestBody TopicDto dto) {
        return ResponseEntity.ok(topicService.updateTopic(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteTopic(@PathVariable Long id) {
        topicService.deleteTopic(id);
        Map<String, String> resp = new HashMap<>();
        resp.put("message", "Topic deleted successfully");
        return ResponseEntity.ok(resp);
    }
}
