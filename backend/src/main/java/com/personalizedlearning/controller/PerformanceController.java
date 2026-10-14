package com.personalizedlearning.controller;

import com.personalizedlearning.dto.QuizResultResponse;
import com.personalizedlearning.dto.TopicPerformanceResponse;
import com.personalizedlearning.service.PerformanceAnalysisService;
import com.personalizedlearning.service.QuizAttemptService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students/{studentId}")
public class PerformanceController {

    private final PerformanceAnalysisService performanceAnalysisService;
    private final QuizAttemptService quizAttemptService;

    public PerformanceController(PerformanceAnalysisService performanceAnalysisService,
                                 QuizAttemptService quizAttemptService) {
        this.performanceAnalysisService = performanceAnalysisService;
        this.quizAttemptService = quizAttemptService;
    }

    @GetMapping("/performance")
    public ResponseEntity<List<TopicPerformanceResponse>> getPerformance(@PathVariable Long studentId) {
        return ResponseEntity.ok(performanceAnalysisService.getStudentTopicPerformances(studentId));
    }

    @GetMapping("/performance/topics")
    public ResponseEntity<List<TopicPerformanceResponse>> getTopicPerformances(@PathVariable Long studentId) {
        return ResponseEntity.ok(performanceAnalysisService.getStudentTopicPerformances(studentId));
    }

    @GetMapping("/performance/subjects")
    public ResponseEntity<Map<String, Double>> getSubjectPerformances(@PathVariable Long studentId) {
        return ResponseEntity.ok(performanceAnalysisService.getStudentSubjectPerformances(studentId));
    }

    @GetMapping("/performance/weak-topics")
    public ResponseEntity<List<TopicPerformanceResponse>> getWeakTopics(@PathVariable Long studentId) {
        return ResponseEntity.ok(performanceAnalysisService.getWeakTopicsForStudent(studentId));
    }

    @GetMapping("/attempts")
    public ResponseEntity<List<QuizResultResponse>> getStudentAttempts(@PathVariable Long studentId) {
        return ResponseEntity.ok(quizAttemptService.getAttemptsByStudent(studentId));
    }
}
