package com.personalizedlearning.repository;

import com.personalizedlearning.entity.StudyMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyMaterialRepository extends JpaRepository<StudyMaterial, Long> {
    List<StudyMaterial> findByTopicIdAndActiveTrue(Long topicId);
    List<StudyMaterial> findByTopicId(Long topicId);
    List<StudyMaterial> findByActiveTrue();
    List<StudyMaterial> findByCreatedBy(Long createdBy);
    List<StudyMaterial> findByTopicIdAndMaterialTypeIgnoreCaseAndActiveTrue(Long topicId, String materialType);
}
