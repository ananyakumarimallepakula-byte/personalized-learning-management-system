package com.personalizedlearning.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class QuizResultResponse {

    private Long attemptId;
    private Long quizId;
    private String quizTitle;
    private Long topicId;
    private String topicName;
    private String subjectName;
    private Double score;
    private Double percentage;
    private Integer totalQuestions;
    private Integer correctAnswers;
    private Integer wrongAnswers;
    private Integer timeTakenSeconds;
    private LocalDateTime submittedAt;

    private boolean weakDetected;
    private String riskLevel; // LOW, MEDIUM, HIGH
    private String predictionReason;
    private Double predictionScore;
    private List<String> recommendedActions = new ArrayList<>();
    private List<RecommendationResponse> recommendations = new ArrayList<>();
    private List<RecommendedQuestionResponse> practiceQuestions = new ArrayList<>();

    public QuizResultResponse() {
    }

    public Long getAttemptId() {
        return attemptId;
    }

    public void setAttemptId(Long attemptId) {
        this.attemptId = attemptId;
    }

    public Long getQuizId() {
        return quizId;
    }

    public void setQuizId(Long quizId) {
        this.quizId = quizId;
    }

    public String getQuizTitle() {
        return quizTitle;
    }

    public void setQuizTitle(String quizTitle) {
        this.quizTitle = quizTitle;
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

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public Integer getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(Integer totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public Integer getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(Integer correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public Integer getWrongAnswers() {
        return wrongAnswers;
    }

    public void setWrongAnswers(Integer wrongAnswers) {
        this.wrongAnswers = wrongAnswers;
    }

    public Integer getTimeTakenSeconds() {
        return timeTakenSeconds;
    }

    public void setTimeTakenSeconds(Integer timeTakenSeconds) {
        this.timeTakenSeconds = timeTakenSeconds;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public boolean isWeakDetected() {
        return weakDetected;
    }

    public void setWeakDetected(boolean weakDetected) {
        this.weakDetected = weakDetected;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public String getPredictionReason() {
        return predictionReason;
    }

    public void setPredictionReason(String predictionReason) {
        this.predictionReason = predictionReason;
    }

    public Double getPredictionScore() {
        return predictionScore;
    }

    public void setPredictionScore(Double predictionScore) {
        this.predictionScore = predictionScore;
    }

    public List<String> getRecommendedActions() {
        return recommendedActions;
    }

    public void setRecommendedActions(List<String> recommendedActions) {
        this.recommendedActions = recommendedActions;
    }

    public List<RecommendationResponse> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<RecommendationResponse> recommendations) {
        this.recommendations = recommendations;
    }

    public List<RecommendedQuestionResponse> getPracticeQuestions() {
        return practiceQuestions;
    }

    public void setPracticeQuestions(List<RecommendedQuestionResponse> practiceQuestions) {
        this.practiceQuestions = practiceQuestions;
    }
}
