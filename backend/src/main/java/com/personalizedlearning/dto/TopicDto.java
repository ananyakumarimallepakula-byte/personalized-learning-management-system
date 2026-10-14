package com.personalizedlearning.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public class TopicDto {

    private Long id;

    @NotNull(message = "Subject ID is required")
    private Long subjectId;

    private String subjectName;

    @NotBlank(message = "Topic name is required")
    @Size(min = 2, max = 150, message = "Topic name must be between 2 and 150 characters")
    private String name;

    private String description;
    private String difficulty = "MEDIUM";
    private Boolean active = true;
    private LocalDateTime createdAt;
    private int questionCount;
    private int materialCount;

    public TopicDto() {
    }

    public TopicDto(Long id, Long subjectId, String subjectName, String name, String description,
                    String difficulty, Boolean active, LocalDateTime createdAt) {
        this.id = id;
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.name = name;
        this.description = description;
        this.difficulty = difficulty != null ? difficulty : "MEDIUM";
        this.active = active != null ? active : true;
        this.createdAt = createdAt;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }

    public int getMaterialCount() {
        return materialCount;
    }

    public void setMaterialCount(int materialCount) {
        this.materialCount = materialCount;
    }
}
