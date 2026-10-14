package com.personalizedlearning.service.strategy;

public class PredictionResult {

    private double predictionScore;
    private String riskLevel; // LOW, MEDIUM, HIGH
    private String reason;
    private String recommendedAction;

    public PredictionResult() {
    }

    public PredictionResult(double predictionScore, String riskLevel, String reason, String recommendedAction) {
        this.predictionScore = Math.round(predictionScore * 100.0) / 100.0;
        this.riskLevel = riskLevel;
        this.reason = reason;
        this.recommendedAction = recommendedAction;
    }

    public double getPredictionScore() {
        return predictionScore;
    }

    public void setPredictionScore(double predictionScore) {
        this.predictionScore = Math.round(predictionScore * 100.0) / 100.0;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getRecommendedAction() {
        return recommendedAction;
    }

    public void setRecommendedAction(String recommendedAction) {
        this.recommendedAction = recommendedAction;
    }
}
