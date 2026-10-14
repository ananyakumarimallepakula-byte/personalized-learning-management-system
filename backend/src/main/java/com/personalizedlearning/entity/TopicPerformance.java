package com.personalizedlearning.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "topic_performance",
       uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "topic_id"}))
public class TopicPerformance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @Column(name = "total_attempts")
    private Integer totalAttempts = 0;

    @Column(name = "total_questions")
    private Integer totalQuestions = 0;

    @Column(name = "correct_answers")
    private Integer correctAnswers = 0;

    @Column(name = "wrong_answers")
    private Integer wrongAnswers = 0;

    @Column(name = "average_score", columnDefinition = "DECIMAL(5,2)")
    private Double averageScore = 0.0;

    @Column(name = "average_percentage", columnDefinition = "DECIMAL(5,2)")
    private Double averagePercentage = 0.0;

    @Column(name = "last_attempt_at")
    private LocalDateTime lastAttemptAt;

    @Column(length = 20)
    private String trend = "STABLE"; // IMPROVING, DECLINING, STABLE

    public TopicPerformance() {
    }

    public TopicPerformance(User student, Topic topic) {
        this.student = student;
        this.topic = topic;
        this.totalAttempts = 0;
        this.totalQuestions = 0;
        this.correctAnswers = 0;
        this.wrongAnswers = 0;
        this.averageScore = 0.0;
        this.averagePercentage = 0.0;
        this.trend = "STABLE";
        this.lastAttemptAt = LocalDateTime.now();
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

    public Integer getTotalAttempts() {
        return totalAttempts;
    }

    public void setTotalAttempts(Integer totalAttempts) {
        this.totalAttempts = totalAttempts;
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

    public Double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(Double averageScore) {
        this.averageScore = averageScore;
    }

    public Double getAveragePercentage() {
        return averagePercentage;
    }

    public void setAveragePercentage(Double averagePercentage) {
        this.averagePercentage = averagePercentage;
    }

    public LocalDateTime getLastAttemptAt() {
        return lastAttemptAt;
    }

    public void setLastAttemptAt(LocalDateTime lastAttemptAt) {
        this.lastAttemptAt = lastAttemptAt;
    }

    public String getTrend() {
        return trend;
    }

    public void setTrend(String trend) {
        this.trend = trend;
    }
}
