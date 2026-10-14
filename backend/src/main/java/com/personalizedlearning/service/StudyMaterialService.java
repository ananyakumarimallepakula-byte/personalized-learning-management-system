package com.personalizedlearning.service;

import com.personalizedlearning.dto.StudyMaterialDto;
import com.personalizedlearning.entity.StudyMaterial;
import com.personalizedlearning.entity.Topic;
import com.personalizedlearning.exception.ResourceNotFoundException;
import com.personalizedlearning.repository.StudyMaterialRepository;
import com.personalizedlearning.repository.TopicRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudyMaterialService {

    private final StudyMaterialRepository studyMaterialRepository;
    private final TopicRepository topicRepository;

    public StudyMaterialService(StudyMaterialRepository studyMaterialRepository, TopicRepository topicRepository) {
        this.studyMaterialRepository = studyMaterialRepository;
        this.topicRepository = topicRepository;
    }

    public List<StudyMaterialDto> getAllMaterials() {
        return studyMaterialRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<StudyMaterialDto> getMaterialsByTopic(Long topicId) {
        return studyMaterialRepository.findByTopicIdAndActiveTrue(topicId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public StudyMaterialDto getMaterialById(Long id) {
        StudyMaterial material = studyMaterialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Study material not found with id: " + id));
        return toDto(material);
    }

    @Transactional
    public StudyMaterialDto createMaterial(StudyMaterialDto dto, Long createdBy) {
        Topic topic = topicRepository.findById(dto.getTopicId())
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found with id: " + dto.getTopicId()));

        StudyMaterial material = new StudyMaterial();
        material.setTopic(topic);
        material.setTitle(dto.getTitle());
        material.setDescription(dto.getDescription());
        material.setMaterialType(dto.getMaterialType() != null ? dto.getMaterialType().toUpperCase() : "NOTES");
        material.setContentUrl(dto.getContentUrl());
        material.setContent(dto.getContent());
        material.setDifficulty(dto.getDifficulty() != null ? dto.getDifficulty().toUpperCase() : "MEDIUM");
        material.setCreatedBy(createdBy);
        material.setActive(dto.getActive() != null ? dto.getActive() : true);

        return toDto(studyMaterialRepository.save(material));
    }

    @Transactional
    public StudyMaterialDto updateMaterial(Long id, StudyMaterialDto dto) {
        StudyMaterial material = studyMaterialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Study material not found with id: " + id));

        if (dto.getTitle() != null) material.setTitle(dto.getTitle());
        if (dto.getDescription() != null) material.setDescription(dto.getDescription());
        if (dto.getMaterialType() != null) material.setMaterialType(dto.getMaterialType().toUpperCase());
        if (dto.getContentUrl() != null) material.setContentUrl(dto.getContentUrl());
        if (dto.getContent() != null) material.setContent(dto.getContent());
        if (dto.getDifficulty() != null) material.setDifficulty(dto.getDifficulty().toUpperCase());
        if (dto.getActive() != null) material.setActive(dto.getActive());

        if (dto.getTopicId() != null && !dto.getTopicId().equals(material.getTopic().getId())) {
            Topic topic = topicRepository.findById(dto.getTopicId())
                    .orElseThrow(() -> new ResourceNotFoundException("Topic not found with id: " + dto.getTopicId()));
            material.setTopic(topic);
        }

        return toDto(studyMaterialRepository.save(material));
    }

    @Transactional
    public void deleteMaterial(Long id) {
        if (!studyMaterialRepository.existsById(id)) {
            throw new ResourceNotFoundException("Study material not found with id: " + id);
        }
        studyMaterialRepository.deleteById(id);
    }

    public StudyMaterialDto toDto(StudyMaterial material) {
        String subjectName = (material.getTopic() != null && material.getTopic().getSubject() != null)
                ? material.getTopic().getSubject().getName() : null;

        return new StudyMaterialDto(
                material.getId(),
                material.getTopic() != null ? material.getTopic().getId() : null,
                material.getTopic() != null ? material.getTopic().getName() : null,
                subjectName,
                material.getTitle(),
                material.getDescription(),
                material.getMaterialType(),
                material.getContentUrl(),
                material.getContent(),
                material.getDifficulty(),
                material.getCreatedBy(),
                material.getCreatedAt(),
                material.getActive()
        );
    }
}
