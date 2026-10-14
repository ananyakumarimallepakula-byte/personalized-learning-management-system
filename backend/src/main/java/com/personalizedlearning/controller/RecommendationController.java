package com.personalizedlearning.controller;

import com.personalizedlearning.dto.RecommendationResponse;
import com.personalizedlearning.dto.RecommendedQuestionResponse;
import com.personalizedlearning.service.RecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/students/{studentId}/recommendations")
    public ResponseEntity<List<RecommendationResponse>> getRecommendations(@PathVariable Long studentId) {
        return ResponseEntity.ok(recommendationService.getStudentRecommendations(studentId));
    }

    @PutMapping("/recommendations/{id}/complete")
    public ResponseEntity<RecommendationResponse> markRecommendationCompleted(@PathVariable Long id) {
        return ResponseEntity.ok(recommendationService.markRecommendationCompleted(id));
    }

    @GetMapping("/students/{studentId}/practice-questions")
    public ResponseEntity<List<RecommendedQuestionResponse>> getPracticeQuestions(@PathVariable Long studentId) {
        return ResponseEntity.ok(recommendationService.getStudentPracticeQuestions(studentId));
    }

    @GetMapping("/students/{studentId}/practice-questions/topic/{topicId}")
    public ResponseEntity<List<RecommendedQuestionResponse>> getTopicPracticeQuestions(@PathVariable Long studentId, @PathVariable Long topicId) {
        return ResponseEntity.ok(recommendationService.getPracticeQuestionsForTopic(studentId, topicId));
    }

    @PutMapping("/practice-questions/{id}/complete")
    public ResponseEntity<RecommendedQuestionResponse> markQuestionCompleted(@PathVariable Long id) {
        return ResponseEntity.ok(recommendationService.markPracticeQuestionCompleted(id));
    }
}
