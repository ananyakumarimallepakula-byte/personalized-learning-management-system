package com.personalizedlearning.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "recommendations")
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "material_id", nullable = true)
    private StudyMaterial material;

    @Column(length = 20, nullable = false)
    private String priority = "MEDIUM"; // HIGH, MEDIUM, LOW

    @Column(columnDefinition = "TEXT")
    private String reason;

    @Column(name = "recommended_at")
    private LocalDateTime recommendedAt = LocalDateTime.now();

    @Column(nullable = false)
    private Boolean completed = false;

    public Recommendation() {
    }

    public Recommendation(User student, Topic topic, StudyMaterial material, String priority, String reason) {
        this.student = student;
        this.topic = topic;
        this.material = material;
        this.priority = priority != null ? priority : "MEDIUM";
        this.reason = reason;
        this.recommendedAt = LocalDateTime.now();
        this.completed = false;
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

    public StudyMaterial getMaterial() {
        return material;
    }

    public void setMaterial(StudyMaterial material) {
        this.material = material;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getRecommendedAt() {
        return recommendedAt;
    }

    public void setRecommendedAt(LocalDateTime recommendedAt) {
        this.recommendedAt = recommendedAt;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
}
