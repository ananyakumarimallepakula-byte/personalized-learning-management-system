package com.personalizedlearning.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardResponse {

    private String studentName;
    private Double overallPerformance = 0.0;
    private Integer strongTopicsCount = 0;
    private Integer weakTopicsCount = 0;
    private Integer averageTopicsCount = 0;
    private Integer predictedRiskTopicsCount = 0;
    private Integer quizzesCompleted = 0;
    private Double learningProgress = 0.0;

    private Map<String, Double> subjectPerformance = new HashMap<>();
    private List<TopicPerformanceResponse> weakTopics = new ArrayList<>();
    private List<TopicPerformanceResponse> allTopicPerformances = new ArrayList<>();
    private List<QuizResultResponse> recentQuizScores = new ArrayList<>();
    private List<PredictionResponse> predictions = new ArrayList<>();
    private List<RecommendationResponse> recommendations = new ArrayList<>();
    private List<RecommendedQuestionResponse> practiceQuestions = new ArrayList<>();

    public DashboardResponse() {
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Double getOverallPerformance() {
        return overallPerformance;
    }

    public void setOverallPerformance(Double overallPerformance) {
        this.overallPerformance = overallPerformance;
    }

    public Integer getStrongTopicsCount() {
        return strongTopicsCount;
    }

    public void setStrongTopicsCount(Integer strongTopicsCount) {
        this.strongTopicsCount = strongTopicsCount;
    }

    public Integer getWeakTopicsCount() {
        return weakTopicsCount;
    }

    public void setWeakTopicsCount(Integer weakTopicsCount) {
        this.weakTopicsCount = weakTopicsCount;
    }

    public Integer getAverageTopicsCount() {
        return averageTopicsCount;
    }

    public void setAverageTopicsCount(Integer averageTopicsCount) {
        this.averageTopicsCount = averageTopicsCount;
    }

    public Integer getPredictedRiskTopicsCount() {
        return predictedRiskTopicsCount;
    }

    public void setPredictedRiskTopicsCount(Integer predictedRiskTopicsCount) {
        this.predictedRiskTopicsCount = predictedRiskTopicsCount;
    }

    public Integer getQuizzesCompleted() {
        return quizzesCompleted;
    }

    public void setQuizzesCompleted(Integer quizzesCompleted) {
        this.quizzesCompleted = quizzesCompleted;
    }

    public Double getLearningProgress() {
        return learningProgress;
    }

    public void setLearningProgress(Double learningProgress) {
        this.learningProgress = learningProgress;
    }

    public Map<String, Double> getSubjectPerformance() {
        return subjectPerformance;
    }

    public void setSubjectPerformance(Map<String, Double> subjectPerformance) {
        this.subjectPerformance = subjectPerformance;
    }

    public List<TopicPerformanceResponse> getWeakTopics() {
        return weakTopics;
    }

    public void setWeakTopics(List<TopicPerformanceResponse> weakTopics) {
        this.weakTopics = weakTopics;
    }

    public List<TopicPerformanceResponse> getAllTopicPerformances() {
        return allTopicPerformances;
    }

    public void setAllTopicPerformances(List<TopicPerformanceResponse> allTopicPerformances) {
        this.allTopicPerformances = allTopicPerformances;
    }

    public List<QuizResultResponse> getRecentQuizScores() {
        return recentQuizScores;
    }

    public void setRecentQuizScores(List<QuizResultResponse> recentQuizScores) {
        this.recentQuizScores = recentQuizScores;
    }

    public List<PredictionResponse> getPredictions() {
        return predictions;
    }

    public void setPredictions(List<PredictionResponse> predictions) {
        this.predictions = predictions;
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
