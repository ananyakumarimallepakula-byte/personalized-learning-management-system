package com.personalizedlearning.repository;

import com.personalizedlearning.entity.QuizAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {
    List<QuizAttempt> findByStudentIdOrderByStartedAtDesc(Long studentId);
    List<QuizAttempt> findByQuizIdOrderByStartedAtDesc(Long quizId);
    List<QuizAttempt> findByStudentIdAndQuizIdOrderByStartedAtDesc(Long studentId, Long quizId);

    @Query("SELECT qa FROM QuizAttempt qa WHERE qa.student.id = :studentId AND qa.quiz.topic.id = :topicId ORDER BY qa.submittedAt DESC")
    List<QuizAttempt> findByStudentIdAndTopicId(@Param("studentId") Long studentId, @Param("topicId") Long topicId);

    long countByStudentId(Long studentId);
}
