package com.personalizedlearning.dto;

import java.time.LocalDateTime;

public class PredictionResponse {

    private Long id;
    private Long studentId;
    private Long topicId;
    private String topicName;
    private String subjectName;
    private Double predictionScore;
    private String riskLevel; // LOW, MEDIUM, HIGH
    private String reason;
    private LocalDateTime predictedAt;
    private Double currentPerformance;
    private String recommendedAction;

    public PredictionResponse() {
    }

    public PredictionResponse(Long id, Long studentId, Long topicId, String topicName, String subjectName,
                              Double predictionScore, String riskLevel, String reason, LocalDateTime predictedAt,
                              Double currentPerformance, String recommendedAction) {
        this.id = id;
        this.studentId = studentId;
        this.topicId = topicId;
        this.topicName = topicName;
        this.subjectName = subjectName;
        this.predictionScore = predictionScore;
        this.riskLevel = riskLevel;
        this.reason = reason;
        this.predictedAt = predictedAt;
        this.currentPerformance = currentPerformance;
        this.recommendedAction = recommendedAction;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Double getPredictionScore() {
        return predictionScore;
    }

    public void setPredictionScore(Double predictionScore) {
        this.predictionScore = predictionScore;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getPredictedAt() {
        return predictedAt;
    }

    public void setPredictedAt(LocalDateTime predictedAt) {
        this.predictedAt = predictedAt;
    }

    public Double getCurrentPerformance() {
        return currentPerformance;
    }

    public void setCurrentPerformance(Double currentPerformance) {
        this.currentPerformance = currentPerformance;
    }

    public String getRecommendedAction() {
        return recommendedAction;
    }

    public void setRecommendedAction(String recommendedAction) {
        this.recommendedAction = recommendedAction;
    }
}
