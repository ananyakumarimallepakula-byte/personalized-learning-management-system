package com.personalizedlearning.dto;

import java.util.HashMap;
import java.util.Map;

public class SystemStatsResponse {

    private long totalUsers;
    private long totalStudents;
    private long totalTeachers;
    private long totalQuizzes;
    private long totalAttempts;
    private long totalSubjects;
    private long totalTopics;
    private long totalQuestions;
    private long totalMaterials;

    private Map<String, Long> weakTopicsFrequency = new HashMap<>();
    private Map<String, Double> subjectAverages = new HashMap<>();

    public SystemStatsResponse() {
    }

    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public long getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(long totalStudents) {
        this.totalStudents = totalStudents;
    }

    public long getTotalTeachers() {
        return totalTeachers;
    }

    public void setTotalTeachers(long totalTeachers) {
        this.totalTeachers = totalTeachers;
    }

    public long getTotalQuizzes() {
        return totalQuizzes;
    }

    public void setTotalQuizzes(long totalQuizzes) {
        this.totalQuizzes = totalQuizzes;
    }

    public long getTotalAttempts() {
        return totalAttempts;
    }

    public void setTotalAttempts(long totalAttempts) {
        this.totalAttempts = totalAttempts;
    }

    public long getTotalSubjects() {
        return totalSubjects;
    }

    public void setTotalSubjects(long totalSubjects) {
        this.totalSubjects = totalSubjects;
    }

    public long getTotalTopics() {
        return totalTopics;
    }

    public void setTotalTopics(long totalTopics) {
        this.totalTopics = totalTopics;
    }

    public long getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(long totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public long getTotalMaterials() {
        return totalMaterials;
    }

    public void setTotalMaterials(long totalMaterials) {
        this.totalMaterials = totalMaterials;
    }

    public Map<String, Long> getWeakTopicsFrequency() {
        return weakTopicsFrequency;
    }

    public void setWeakTopicsFrequency(Map<String, Long> weakTopicsFrequency) {
        this.weakTopicsFrequency = weakTopicsFrequency;
    }

    public Map<String, Double> getSubjectAverages() {
        return subjectAverages;
    }

    public void setSubjectAverages(Map<String, Double> subjectAverages) {
        this.subjectAverages = subjectAverages;
    }
}
