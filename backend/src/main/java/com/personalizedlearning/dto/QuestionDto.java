package com.personalizedlearning.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class QuestionDto {

    private Long id;
    private Long quizId;

    @NotNull(message = "Topic ID is required")
    private Long topicId;

    private String topicName;
    private String subjectName;

    @NotBlank(message = "Question text is required")
    private String questionText;

    private String difficulty = "MEDIUM";
    private Integer marks = 1;
    private String explanation;
    private LocalDateTime createdAt;
    private List<QuestionOptionDto> options = new ArrayList<>();

    public QuestionDto() {
    }

    public QuestionDto(Long id, Long quizId, Long topicId, String topicName, String subjectName,
                       String questionText, String difficulty, Integer marks, String explanation,
                       LocalDateTime createdAt, List<QuestionOptionDto> options) {
        this.id = id;
        this.quizId = quizId;
        this.topicId = topicId;
        this.topicName = topicName;
        this.subjectName = subjectName;
        this.questionText = questionText;
        this.difficulty = difficulty != null ? difficulty : "MEDIUM";
        this.marks = marks != null ? marks : 1;
        this.explanation = explanation;
        this.createdAt = createdAt;
        this.options = options != null ? options : new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public void setQuizId(Long quizId) {
        this.quizId = quizId;
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

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public Integer getMarks() {
        return marks;
    }

    public void setMarks(Integer marks) {
        this.marks = marks;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<QuestionOptionDto> getOptions() {
        return options;
    }

    public void setOptions(List<QuestionOptionDto> options) {
        this.options = options;
    }
}
