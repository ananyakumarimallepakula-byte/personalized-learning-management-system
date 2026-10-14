package com.personalizedlearning.controller;

import com.personalizedlearning.dto.QuizResultResponse;
import com.personalizedlearning.dto.QuizSubmissionRequest;
import com.personalizedlearning.service.QuizAttemptService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quiz-attempts")
public class QuizAttemptController {

    private final QuizAttemptService quizAttemptService;

    public QuizAttemptController(QuizAttemptService quizAttemptService) {
        this.quizAttemptService = quizAttemptService;
    }

    @PostMapping("/submit")
    public ResponseEntity<QuizResultResponse> submitQuiz(@Valid @RequestBody QuizSubmissionRequest request) {
        QuizResultResponse result = quizAttemptService.submitQuiz(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}/result")
    public ResponseEntity<QuizResultResponse> getAttemptResult(@PathVariable Long id) {
        return ResponseEntity.ok(quizAttemptService.getAttemptResult(id));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<QuizResultResponse>> getAttemptsByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(quizAttemptService.getAttemptsByStudent(studentId));
    }
}
