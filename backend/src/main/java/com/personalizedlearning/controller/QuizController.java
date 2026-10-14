package com.personalizedlearning.controller;

import com.personalizedlearning.dto.QuizDto;
import com.personalizedlearning.service.QuizService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping
    public ResponseEntity<List<QuizDto>> getAllQuizzes() {
        return ResponseEntity.ok(quizService.getActiveQuizzes());
    }

    @GetMapping("/all")
    public ResponseEntity<List<QuizDto>> getAllQuizzesAdmin() {
        return ResponseEntity.ok(quizService.getAllQuizzes());
    }

    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<List<QuizDto>> getQuizzesBySubject(@PathVariable Long subjectId) {
        return ResponseEntity.ok(quizService.getQuizzesBySubject(subjectId));
    }

    @GetMapping("/topic/{topicId}")
    public ResponseEntity<List<QuizDto>> getQuizzesByTopic(@PathVariable Long topicId) {
        return ResponseEntity.ok(quizService.getQuizzesByTopic(topicId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizDto> getQuizById(@PathVariable Long id) {
        return ResponseEntity.ok(quizService.getQuizById(id));
    }

    @PostMapping
    public ResponseEntity<QuizDto> createQuiz(@Valid @RequestBody QuizDto dto,
                                              @RequestParam(required = false, defaultValue = "1") Long createdBy) {
        QuizDto created = quizService.createQuiz(dto, createdBy);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuizDto> updateQuiz(@PathVariable Long id, @RequestBody QuizDto dto) {
        return ResponseEntity.ok(quizService.updateQuiz(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteQuiz(@PathVariable Long id) {
        quizService.deleteQuiz(id);
        Map<String, String> resp = new HashMap<>();
        resp.put("message", "Quiz deleted successfully");
        return ResponseEntity.ok(resp);
    }
}
