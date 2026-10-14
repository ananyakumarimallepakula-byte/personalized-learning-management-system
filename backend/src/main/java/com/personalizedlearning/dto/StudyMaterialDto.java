package com.personalizedlearning.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class StudyMaterialDto {

    private Long id;

    @NotNull(message = "Topic ID is required")
    private Long topicId;

    private String topicName;
    private String subjectName;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;
    private String materialType = "NOTES"; // NOTES, PDF, VIDEO, ARTICLE, LINK
    private String contentUrl;
    private String content;
    private String difficulty = "MEDIUM";
    private Long createdBy;
    private LocalDateTime createdAt;
    private Boolean active = true;

    public StudyMaterialDto() {
    }

    public StudyMaterialDto(Long id, Long topicId, String topicName, String subjectName, String title,
                            String description, String materialType, String contentUrl, String content,
                            String difficulty, Long createdBy, LocalDateTime createdAt, Boolean active) {
        this.id = id;
        this.topicId = topicId;
        this.topicName = topicName;
        this.subjectName = subjectName;
        this.title = title;
        this.description = description;
        this.materialType = materialType != null ? materialType : "NOTES";
        this.contentUrl = contentUrl;
        this.content = content;
        this.difficulty = difficulty != null ? difficulty : "MEDIUM";
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.active = active != null ? active : true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getMaterialType() {
        return materialType;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
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
}
