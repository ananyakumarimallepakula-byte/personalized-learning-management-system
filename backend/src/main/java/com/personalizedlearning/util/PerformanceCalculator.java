package com.personalizedlearning.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PerformanceCalculator {

    @Value("${app.thresholds.weak:60.0}")
    private double weakThreshold = 60.0;

    @Value("${app.thresholds.strong:75.0}")
    private double strongThreshold = 75.0;

    public PerformanceCalculator() {
    }

    public PerformanceCalculator(double weakThreshold, double strongThreshold) {
        this.weakThreshold = weakThreshold;
        this.strongThreshold = strongThreshold;
    }

    public double calculatePercentage(int correctAnswers, int totalQuestions) {
        if (totalQuestions <= 0) {
            return 0.0;
        }
        double percentage = ((double) correctAnswers / totalQuestions) * 100.0;
        return Math.round(percentage * 100.0) / 100.0;
    }

    public String classifyStatus(double percentage) {
        if (percentage < weakThreshold) {
            return "WEAK";
        } else if (percentage <= strongThreshold) {
            return "AVERAGE";
        } else {
            return "STRONG";
        }
    }

    public boolean isWeak(double percentage) {
        return percentage < weakThreshold;
    }

    public String calculateTrend(double previousAverage, double recentScore) {
        double diff = recentScore - previousAverage;
        if (diff > 3.0) {
            return "IMPROVING";
        } else if (diff < -3.0) {
            return "DECLINING";
        } else {
            return "STABLE";
        }
    }

    public double getWeakThreshold() {
        return weakThreshold;
    }

    public void setWeakThreshold(double weakThreshold) {
        this.weakThreshold = weakThreshold;
    }

    public double getStrongThreshold() {
        return strongThreshold;
    }

    public void setStrongThreshold(double strongThreshold) {
        this.strongThreshold = strongThreshold;
    }
}
