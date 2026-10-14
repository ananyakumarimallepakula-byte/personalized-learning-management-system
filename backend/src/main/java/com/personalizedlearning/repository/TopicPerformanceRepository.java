package com.personalizedlearning.repository;

import com.personalizedlearning.entity.TopicPerformance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TopicPerformanceRepository extends JpaRepository<TopicPerformance, Long> {
    List<TopicPerformance> findByStudentId(Long studentId);
    Optional<TopicPerformance> findByStudentIdAndTopicId(Long studentId, Long topicId);
    List<TopicPerformance> findByTopicId(Long topicId);
}
