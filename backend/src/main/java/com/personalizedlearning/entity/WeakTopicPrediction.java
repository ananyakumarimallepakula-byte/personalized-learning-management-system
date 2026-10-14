package com.personalizedlearning.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "weak_topic_predictions")
public class WeakTopicPrediction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @Column(name = "prediction_score", columnDefinition = "DECIMAL(5,2)", nullable = false)
    private Double predictionScore = 0.0;

    @Column(name = "risk_level", length = 20, nullable = false)
    private String riskLevel = "LOW"; // LOW, MEDIUM, HIGH

    @Column(columnDefinition = "TEXT")
    private String reason;

    @Column(name = "predicted_at")
    private LocalDateTime predictedAt = LocalDateTime.now();

    public WeakTopicPrediction() {
    }

    public WeakTopicPrediction(User student, Topic topic, Double predictionScore, String riskLevel, String reason) {
        this.student = student;
        this.topic = topic;
        this.predictionScore = predictionScore != null ? predictionScore : 0.0;
        this.riskLevel = riskLevel != null ? riskLevel : "LOW";
        this.reason = reason;
        this.predictedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
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
}
