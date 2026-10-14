package com.personalizedlearning.repository;

import com.personalizedlearning.entity.RecommendedQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendedQuestionRepository extends JpaRepository<RecommendedQuestion, Long> {
    List<RecommendedQuestion> findByStudentIdOrderByCreatedAtDesc(Long studentId);
    List<RecommendedQuestion> findByStudentIdAndCompletedFalse(Long studentId);
    List<RecommendedQuestion> findByStudentIdAndTopicIdAndCompletedFalse(Long studentId, Long topicId);
    boolean existsByStudentIdAndQuestionId(Long studentId, Long questionId);
}
