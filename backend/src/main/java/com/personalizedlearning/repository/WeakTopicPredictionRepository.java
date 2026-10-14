package com.personalizedlearning.repository;

import com.personalizedlearning.entity.WeakTopicPrediction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WeakTopicPredictionRepository extends JpaRepository<WeakTopicPrediction, Long> {
    List<WeakTopicPrediction> findByStudentIdOrderByPredictionScoreDesc(Long studentId);
    Optional<WeakTopicPrediction> findByStudentIdAndTopicId(Long studentId, Long topicId);
    List<WeakTopicPrediction> findByRiskLevel(String riskLevel);
    void deleteByStudentIdAndTopicId(Long studentId, Long topicId);
}
