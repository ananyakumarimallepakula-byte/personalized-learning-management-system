package com.personalizedlearning.repository;

import com.personalizedlearning.entity.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
    List<Recommendation> findByStudentIdOrderByRecommendedAtDesc(Long studentId);
    List<Recommendation> findByStudentIdAndCompletedFalse(Long studentId);
    List<Recommendation> findByStudentIdAndTopicId(Long studentId, Long topicId);
    boolean existsByStudentIdAndTopicIdAndMaterialId(Long studentId, Long topicId, Long materialId);
}
