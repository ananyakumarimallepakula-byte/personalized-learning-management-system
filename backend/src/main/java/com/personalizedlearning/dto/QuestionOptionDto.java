package com.personalizedlearning.dto;

public class QuestionOptionDto {

    private Long id;
    private String optionText;
    private Boolean isCorrect;

    public QuestionOptionDto() {
    }

    public QuestionOptionDto(Long id, String optionText, Boolean isCorrect) {
        this.id = id;
        this.optionText = optionText;
        this.isCorrect = isCorrect;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

    public Boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(Boolean isCorrect) {
        this.isCorrect = isCorrect;
    }
}
