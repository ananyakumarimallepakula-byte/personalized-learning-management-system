package com.personalizedlearning.service;

import com.personalizedlearning.dto.PredictionResponse;
import com.personalizedlearning.entity.*;
import com.personalizedlearning.exception.ResourceNotFoundException;
import com.personalizedlearning.repository.QuizAttemptRepository;
import com.personalizedlearning.repository.TopicPerformanceRepository;
import com.personalizedlearning.repository.TopicRepository;
import com.personalizedlearning.repository.UserRepository;
import com.personalizedlearning.repository.WeakTopicPredictionRepository;
import com.personalizedlearning.service.strategy.PredictionResult;
import com.personalizedlearning.service.strategy.PredictionStrategy;
import com.personalizedlearning.util.PredictionCalculator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WeakTopicPredictionService {

    private final WeakTopicPredictionRepository predictionRepository;
    private final TopicPerformanceRepository topicPerformanceRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final TopicRepository topicRepository;
    private final UserRepository userRepository;
    private final PredictionStrategy predictionStrategy;
    private final PredictionCalculator predictionCalculator;

    public WeakTopicPredictionService(WeakTopicPredictionRepository predictionRepository,
                                      TopicPerformanceRepository topicPerformanceRepository,
                                      QuizAttemptRepository quizAttemptRepository,
                                      TopicRepository topicRepository,
                                      UserRepository userRepository,
                                      PredictionStrategy predictionStrategy,
                                      PredictionCalculator predictionCalculator) {
        this.predictionRepository = predictionRepository;
        this.topicPerformanceRepository = topicPerformanceRepository;
        this.quizAttemptRepository = quizAttemptRepository;
        this.topicRepository = topicRepository;
        this.userRepository = userRepository;
        this.predictionStrategy = predictionStrategy;
        this.predictionCalculator = predictionCalculator;
    }

    @Transactional
    public WeakTopicPrediction recalculateForStudentAndTopic(User student, Topic topic) {
        TopicPerformance performance = topicPerformanceRepository
                .findByStudentIdAndTopicId(student.getId(), topic.getId())
                .orElse(null);

        List<QuizAttempt> recentAttempts = quizAttemptRepository.findByStudentIdAndTopicId(student.getId(), topic.getId());

        PredictionResult result = predictionStrategy.predict(student, topic, performance, recentAttempts);

        WeakTopicPrediction prediction = predictionRepository
                .findByStudentIdAndTopicId(student.getId(), topic.getId())
                .orElseGet(() -> new WeakTopicPrediction(student, topic, result.getPredictionScore(), result.getRiskLevel(), result.getReason()));

        prediction.setPredictionScore(result.getPredictionScore());
        prediction.setRiskLevel(result.getRiskLevel());
        prediction.setReason(result.getReason());
        prediction.setPredictedAt(LocalDateTime.now());

        return predictionRepository.save(prediction);
    }

    @Transactional
    public List<PredictionResponse> recalculateAllForStudent(Long studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));

        List<TopicPerformance> performances = topicPerformanceRepository.findByStudentId(studentId);
        for (TopicPerformance tp : performances) {
            recalculateForStudentAndTopic(student, tp.getTopic());
        }

        return getStudentPredictions(studentId);
    }

    public List<PredictionResponse> getStudentPredictions(Long studentId) {
        return predictionRepository.findByStudentIdOrderByPredictionScoreDesc(studentId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public PredictionResponse getPredictionForTopic(Long studentId, Long topicId) {
        WeakTopicPrediction pred = predictionRepository.findByStudentIdAndTopicId(studentId, topicId)
                .orElseGet(() -> {
                    User student = userRepository.findById(studentId)
                            .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));
                    Topic topic = topicRepository.findById(topicId)
                            .orElseThrow(() -> new ResourceNotFoundException("Topic not found with id: " + topicId));
                    return recalculateForStudentAndTopic(student, topic);
                });
        return toResponse(pred);
    }

    public double calculateHistoricalRisk(double avgPercentage) {
        return predictionCalculator.calculateHistoricalRisk(avgPercentage);
    }

    public double calculateRecentPerformanceRisk(double recentPercentage) {
        return predictionCalculator.calculateRecentPerformanceRisk(recentPercentage);
    }

    public double calculateWrongAnswerRisk(int wrongAnswers, int totalQuestions) {
        return predictionCalculator.calculateWrongAnswerRisk(wrongAnswers, totalQuestions);
    }

    public double calculateTrendRisk(String trend, double currentPercentage) {
        return predictionCalculator.calculateTrendRisk(trend, currentPercentage);
    }

    public double calculateInactivityRisk(LocalDateTime lastAttemptAt) {
        return predictionCalculator.calculateInactivityRisk(lastAttemptAt);
    }

    public PredictionResponse toResponse(WeakTopicPrediction pred) {
        String subjectName = (pred.getTopic().getSubject() != null) ? pred.getTopic().getSubject().getName() : null;
        TopicPerformance perf = topicPerformanceRepository
                .findByStudentIdAndTopicId(pred.getStudent().getId(), pred.getTopic().getId())
                .orElse(null);

        double currentScore = perf != null ? perf.getAveragePercentage() : 50.0;

        String action;
        if ("HIGH".equalsIgnoreCase(pred.getRiskLevel())) {
            action = "Revise concepts and solve 10 practice questions immediately.";
        } else if ("MEDIUM".equalsIgnoreCase(pred.getRiskLevel())) {
            action = "Review notes and practice 5 questions to strengthen concepts.";
        } else {
            action = "Topic is stable. Take occasional revision quizzes.";
        }

        return new PredictionResponse(
                pred.getId(),
                pred.getStudent().getId(),
                pred.getTopic().getId(),
                pred.getTopic().getName(),
                subjectName,
                pred.getPredictionScore(),
                pred.getRiskLevel(),
                pred.getReason(),
                pred.getPredictedAt(),
                currentScore,
                action
        );
    }
}
