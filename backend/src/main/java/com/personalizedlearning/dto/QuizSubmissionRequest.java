package com.personalizedlearning.dto;

import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class QuizSubmissionRequest {

    @NotNull(message = "Quiz ID is required")
    private Long quizId;

    private Long studentId; // If extracted from auth or passed
    private Integer timeTakenSeconds = 0;
    private List<AnswerSubmissionDto> answers = new ArrayList<>();

    public QuizSubmissionRequest() {
    }

    public QuizSubmissionRequest(Long quizId, Long studentId, Integer timeTakenSeconds, List<AnswerSubmissionDto> answers) {
        this.quizId = quizId;
        this.studentId = studentId;
        this.timeTakenSeconds = timeTakenSeconds;
        this.answers = answers != null ? answers : new ArrayList<>();
    }

    public Long getQuizId() {
        return quizId;
    }

    public void setQuizId(Long quizId) {
        this.quizId = quizId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Integer getTimeTakenSeconds() {
        return timeTakenSeconds;
    }

    public void setTimeTakenSeconds(Integer timeTakenSeconds) {
        this.timeTakenSeconds = timeTakenSeconds;
    }

    public List<AnswerSubmissionDto> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerSubmissionDto> answers) {
        this.answers = answers;
    }
}
