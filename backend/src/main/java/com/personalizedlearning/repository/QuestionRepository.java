package com.personalizedlearning.repository;

import com.personalizedlearning.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByQuizId(Long quizId);
    List<Question> findByTopicId(Long topicId);
    List<Question> findByTopicIdAndDifficulty(Long topicId, String difficulty);
}
