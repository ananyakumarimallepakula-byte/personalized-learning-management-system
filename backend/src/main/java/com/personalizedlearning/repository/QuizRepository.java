package com.personalizedlearning.repository;

import com.personalizedlearning.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findByActiveTrue();
    List<Quiz> findBySubjectIdAndActiveTrue(Long subjectId);
    List<Quiz> findByTopicIdAndActiveTrue(Long topicId);
    List<Quiz> findByCreatedBy(Long createdBy);
}
