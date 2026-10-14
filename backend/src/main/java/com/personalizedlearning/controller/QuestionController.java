package com.personalizedlearning.controller;

import com.personalizedlearning.dto.QuestionDto;
import com.personalizedlearning.service.QuestionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping
    public ResponseEntity<List<QuestionDto>> getAllQuestions() {
        return ResponseEntity.ok(questionService.getAllQuestions());
    }

    @GetMapping("/quiz/{quizId}")
    public ResponseEntity<List<QuestionDto>> getQuestionsByQuiz(@PathVariable Long quizId) {
        return ResponseEntity.ok(questionService.getQuestionsByQuiz(quizId));
    }

    @GetMapping("/topic/{topicId}")
    public ResponseEntity<List<QuestionDto>> getQuestionsByTopic(@PathVariable Long topicId) {
        return ResponseEntity.ok(questionService.getQuestionsByTopic(topicId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionDto> getQuestionById(@PathVariable Long id) {
        return ResponseEntity.ok(questionService.getQuestionById(id));
    }

    @PostMapping
    public ResponseEntity<QuestionDto> createQuestion(@Valid @RequestBody QuestionDto dto) {
        QuestionDto created = questionService.createQuestion(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuestionDto> updateQuestion(@PathVariable Long id, @RequestBody QuestionDto dto) {
        return ResponseEntity.ok(questionService.updateQuestion(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        Map<String, String> resp = new HashMap<>();
        resp.put("message", "Question deleted successfully");
        return ResponseEntity.ok(resp);
    }
}
