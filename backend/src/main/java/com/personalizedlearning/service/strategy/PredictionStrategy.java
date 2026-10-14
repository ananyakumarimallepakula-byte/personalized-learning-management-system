package com.personalizedlearning.service.strategy;

import com.personalizedlearning.entity.QuizAttempt;
import com.personalizedlearning.entity.Topic;
import com.personalizedlearning.entity.TopicPerformance;
import com.personalizedlearning.entity.User;

import java.util.List;

public interface PredictionStrategy {
    PredictionResult predict(User student, Topic topic, TopicPerformance performance, List<QuizAttempt> recentAttempts);
}
