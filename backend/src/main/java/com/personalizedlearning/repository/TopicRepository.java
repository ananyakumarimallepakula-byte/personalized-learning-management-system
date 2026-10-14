package com.personalizedlearning.repository;

import com.personalizedlearning.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    List<Topic> findBySubjectIdAndActiveTrue(Long subjectId);
    List<Topic> findByActiveTrue();
    List<Topic> findBySubjectId(Long subjectId);
}
