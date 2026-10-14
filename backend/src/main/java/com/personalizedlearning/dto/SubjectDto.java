package com.personalizedlearning.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public class SubjectDto {

    private Long id;

    @NotBlank(message = "Subject name is required")
    @Size(min = 2, max = 150, message = "Subject name must be between 2 and 150 characters")
    private String name;

    private String description;
    private Long createdBy;
    private LocalDateTime createdAt;
    private Boolean active = true;
    private int topicCount;

    public SubjectDto() {
    }

    public SubjectDto(Long id, String name, String description, Long createdBy, LocalDateTime createdAt, Boolean active, int topicCount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.active = active;
        this.topicCount = topicCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public int getTopicCount() {
        return topicCount;
    }

    public void setTopicCount(int topicCount) {
        this.topicCount = topicCount;
    }
}
