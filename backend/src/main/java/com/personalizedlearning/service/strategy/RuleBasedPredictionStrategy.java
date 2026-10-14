package com.personalizedlearning.service.strategy;

import com.personalizedlearning.entity.QuizAttempt;
import com.personalizedlearning.entity.Topic;
import com.personalizedlearning.entity.TopicPerformance;
import com.personalizedlearning.entity.User;
import com.personalizedlearning.util.PredictionCalculator;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class RuleBasedPredictionStrategy implements PredictionStrategy {

    private final PredictionCalculator predictionCalculator;

    public RuleBasedPredictionStrategy(PredictionCalculator predictionCalculator) {
        this.predictionCalculator = predictionCalculator;
    }

    @Override
    public PredictionResult predict(User student, Topic topic, TopicPerformance performance, List<QuizAttempt> recentAttempts) {
        double avgPercentage = performance != null ? performance.getAveragePercentage() : 50.0;
        int wrongAnswers = performance != null ? performance.getWrongAnswers() : 0;
        int totalQuestions = performance != null ? performance.getTotalQuestions() : 0;
        String trend = performance != null ? performance.getTrend() : "STABLE";
        LocalDateTime lastAttemptAt = performance != null ? performance.getLastAttemptAt() : null;

        double recentPercentage = avgPercentage;
        if (recentAttempts != null && !recentAttempts.isEmpty()) {
            recentPercentage = recentAttempts.get(0).getPercentage();
        }

        double historicalRisk = predictionCalculator.calculateHistoricalRisk(avgPercentage);
        double recentRisk = predictionCalculator.calculateRecentPerformanceRisk(recentPercentage);
        double wrongAnswerRisk = predictionCalculator.calculateWrongAnswerRisk(wrongAnswers, totalQuestions);
        double trendRisk = predictionCalculator.calculateTrendRisk(trend, avgPercentage);
        double inactivityRisk = predictionCalculator.calculateInactivityRisk(lastAttemptAt);

        double totalRiskScore = predictionCalculator.calculateOverallRisk(
                historicalRisk, recentRisk, wrongAnswerRisk, trendRisk, inactivityRisk
        );

        String riskLevel = predictionCalculator.classifyRiskLevel(totalRiskScore);

        List<String> reasons = new ArrayList<>();
        if (avgPercentage < 60.0) {
            reasons.add(String.format("Historical average is low (%.1f%%)", avgPercentage));
        }
        if (recentPercentage < 60.0) {
            reasons.add(String.format("Recent quiz score is below threshold (%.1f%%)", recentPercentage));
        }
        if ("DECLINING".equalsIgnoreCase(trend)) {
            reasons.add("Performance trend is declining over recent attempts");
        }
        if (totalQuestions > 0 && ((double) wrongAnswers / totalQuestions) > 0.35) {
            reasons.add(String.format("High wrong answer frequency (%d wrong out of %d)", wrongAnswers, totalQuestions));
        }
        if (inactivityRisk > 40.0) {
            reasons.add("Topic has not been practiced recently");
        }
        if (reasons.isEmpty()) {
            reasons.add("Consistent steady progress observed on this topic");
        }

        String reasonSummary = String.join("; ", reasons);

        String recommendedAction;
        if ("HIGH".equalsIgnoreCase(riskLevel)) {
            recommendedAction = "Revise " + topic.getName() + " concepts, review notes & video lectures, and complete 10 practice questions.";
        } else if ("MEDIUM".equalsIgnoreCase(riskLevel)) {
            recommendedAction = "Review core principles for " + topic.getName() + " and practice 5 recommended questions.";
        } else {
            recommendedAction = "Maintain strong understanding with regular periodic revision quizzes.";
        }

        return new PredictionResult(totalRiskScore, riskLevel, reasonSummary, recommendedAction);
    }
}
