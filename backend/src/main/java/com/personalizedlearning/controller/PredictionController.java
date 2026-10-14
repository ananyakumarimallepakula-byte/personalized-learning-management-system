package com.personalizedlearning.controller;

import com.personalizedlearning.dto.PredictionResponse;
import com.personalizedlearning.service.WeakTopicPredictionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students/{studentId}/predictions")
public class PredictionController {

    private final WeakTopicPredictionService predictionService;

    public PredictionController(WeakTopicPredictionService predictionService) {
        this.predictionService = predictionService;
    }

    @GetMapping
    public ResponseEntity<List<PredictionResponse>> getPredictions(@PathVariable Long studentId) {
        return ResponseEntity.ok(predictionService.getStudentPredictions(studentId));
    }

    @PostMapping("/recalculate")
    public ResponseEntity<List<PredictionResponse>> recalculatePredictions(@PathVariable Long studentId) {
        return ResponseEntity.ok(predictionService.recalculateAllForStudent(studentId));
    }

    @GetMapping("/topic/{topicId}")
    public ResponseEntity<PredictionResponse> getTopicPrediction(@PathVariable Long studentId, @PathVariable Long topicId) {
        return ResponseEntity.ok(predictionService.getPredictionForTopic(studentId, topicId));
    }
}
