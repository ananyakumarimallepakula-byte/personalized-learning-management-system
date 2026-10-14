package com.personalizedlearning.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class QuizDto {

    private Long id;

    @NotNull(message = "Subject ID is required")
    private Long subjectId;

    private String subjectName;

    @NotNull(message = "Topic ID is required")
    private Long topicId;

    private String topicName;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;
    private String difficulty = "MEDIUM";
    private Integer durationMinutes = 15;
    private Integer totalMarks = 10;
    private Long createdBy;
    private LocalDateTime createdAt;
    private Boolean active = true;
    private int questionCount;
    private List<QuestionDto> questions = new ArrayList<>();

    public QuizDto() {
    }

    public QuizDto(Long id, Long subjectId, String subjectName, Long topicId, String topicName,
                   String title, String description, String difficulty, Integer durationMinutes,
                   Integer totalMarks, Long createdBy, LocalDateTime createdAt, Boolean active,
                   int questionCount, List<QuestionDto> questions) {
        this.id = id;
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.topicId = topicId;
        this.topicName = topicName;
        this.title = title;
        this.description = description;
        this.difficulty = difficulty != null ? difficulty : "MEDIUM";
        this.durationMinutes = durationMinutes != null ? durationMinutes : 15;
        this.totalMarks = totalMarks != null ? totalMarks : 10;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.active = active != null ? active : true;
        this.questionCount = questionCount;
        this.questions = questions != null ? questions : new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public Integer getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(Integer totalMarks) {
        this.totalMarks = totalMarks;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }

    public List<QuestionDto> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionDto> questions) {
        this.questions = questions;
    }
}
