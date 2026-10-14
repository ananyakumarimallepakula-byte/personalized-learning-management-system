package com.personalizedlearning.repository;

import com.personalizedlearning.entity.StudentAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentAnswerRepository extends JpaRepository<StudentAnswer, Long> {
    List<StudentAnswer> findByQuizAttemptId(Long attemptId);

    @Query("SELECT sa FROM StudentAnswer sa WHERE sa.quizAttempt.student.id = :studentId AND sa.question.topic.id = :topicId")
    List<StudentAnswer> findByStudentIdAndTopicId(@Param("studentId") Long studentId, @Param("topicId") Long topicId);

    @Query("SELECT DISTINCT sa.question.id FROM StudentAnswer sa WHERE sa.quizAttempt.student.id = :studentId")
    List<Long> findAttemptedQuestionIdsByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT DISTINCT sa.question.id FROM StudentAnswer sa WHERE sa.quizAttempt.student.id = :studentId AND sa.correct = false")
    List<Long> findIncorrectQuestionIdsByStudentId(@Param("studentId") Long studentId);
}
