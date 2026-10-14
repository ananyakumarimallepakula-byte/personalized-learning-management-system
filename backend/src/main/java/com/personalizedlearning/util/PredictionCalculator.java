package com.personalizedlearning.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class PredictionCalculator {

    @Value("${app.prediction.weights.historical-risk:40.0}")
    private double historicalWeight = 40.0;

    @Value("${app.prediction.weights.recent-risk:25.0}")
    private double recentWeight = 25.0;

    @Value("${app.prediction.weights.wrong-answer-rate:15.0}")
    private double wrongAnswerWeight = 15.0;

    @Value("${app.prediction.weights.trend-risk:10.0}")
    private double trendWeight = 10.0;

    @Value("${app.prediction.weights.inactivity-risk:10.0}")
    private double inactivityWeight = 10.0;

    public PredictionCalculator() {
    }

    public PredictionCalculator(double historicalWeight, double recentWeight,
                                double wrongAnswerWeight, double trendWeight, double inactivityWeight) {
        this.historicalWeight = historicalWeight;
        this.recentWeight = recentWeight;
        this.wrongAnswerWeight = wrongAnswerWeight;
        this.trendWeight = trendWeight;
        this.inactivityWeight = inactivityWeight;
    }

    public double calculateHistoricalRisk(double avgPercentage) {
        // Lower performance indicates higher risk
        return Math.max(0.0, Math.min(100.0, 100.0 - avgPercentage));
    }

    public double calculateRecentPerformanceRisk(double recentPercentage) {
        return Math.max(0.0, Math.min(100.0, 100.0 - recentPercentage));
    }

    public double calculateWrongAnswerRisk(int wrongAnswers, int totalQuestions) {
        if (totalQuestions <= 0) {
            return 50.0; // Moderate default for unattempted
        }
        double wrongRate = ((double) wrongAnswers / totalQuestions) * 100.0;
        return Math.max(0.0, Math.min(100.0, wrongRate));
    }

    public double calculateTrendRisk(String trend, double currentPercentage) {
        if ("DECLINING".equalsIgnoreCase(trend)) {
            return 85.0;
        } else if ("IMPROVING".equalsIgnoreCase(trend)) {
            return 20.0;
        } else { // STABLE
            return currentPercentage < 60.0 ? 65.0 : 30.0;
        }
    }

    public double calculateInactivityRisk(LocalDateTime lastAttemptAt) {
        if (lastAttemptAt == null) {
            return 80.0;
        }
        long days = Duration.between(lastAttemptAt, LocalDateTime.now()).toDays();
        if (days > 14) {
            return 80.0;
        } else if (days > 7) {
            return 50.0;
        } else if (days > 3) {
            return 30.0;
        } else {
            return 10.0;
        }
    }

    public double calculateOverallRisk(double historicalRisk, double recentRisk,
                                       double wrongAnswerRisk, double trendRisk, double inactivityRisk) {
        double totalWeight = historicalWeight + recentWeight + wrongAnswerWeight + trendWeight + inactivityWeight;
        if (totalWeight <= 0) {
            totalWeight = 100.0;
        }
        double weightedRisk = (historicalRisk * historicalWeight
                + recentRisk * recentWeight
                + wrongAnswerRisk * wrongAnswerWeight
                + trendRisk * trendWeight
                + inactivityRisk * inactivityWeight) / totalWeight;

        return Math.max(0.0, Math.min(100.0, Math.round(weightedRisk * 100.0) / 100.0));
    }

    public String classifyRiskLevel(double riskScore) {
        if (riskScore >= 70.0) {
            return "HIGH";
        } else if (riskScore >= 50.0) {
            return "MEDIUM";
        } else {
            return "LOW";
        }
    }

    // Getters and Setters
    public double getHistoricalWeight() { return historicalWeight; }
    public void setHistoricalWeight(double historicalWeight) { this.historicalWeight = historicalWeight; }
    public double getRecentWeight() { return recentWeight; }
    public void setRecentWeight(double recentWeight) { this.recentWeight = recentWeight; }
    public double getWrongAnswerWeight() { return wrongAnswerWeight; }
    public void setWrongAnswerWeight(double wrongAnswerWeight) { this.wrongAnswerWeight = wrongAnswerWeight; }
    public double getTrendWeight() { return trendWeight; }
    public void setTrendWeight(double trendWeight) { this.trendWeight = trendWeight; }
    public double getInactivityWeight() { return inactivityWeight; }
    public void setInactivityWeight(double inactivityWeight) { this.inactivityWeight = inactivityWeight; }
}
