package com.personalizedlearning.service;

import com.personalizedlearning.dto.TopicDto;
import com.personalizedlearning.entity.Subject;
import com.personalizedlearning.entity.Topic;
import com.personalizedlearning.exception.ResourceNotFoundException;
import com.personalizedlearning.repository.SubjectRepository;
import com.personalizedlearning.repository.TopicRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TopicService {

    private final TopicRepository topicRepository;
    private final SubjectRepository subjectRepository;

    public TopicService(TopicRepository topicRepository, SubjectRepository subjectRepository) {
        this.topicRepository = topicRepository;
        this.subjectRepository = subjectRepository;
    }

    public List<TopicDto> getAllTopics() {
        return topicRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<TopicDto> getTopicsBySubject(Long subjectId) {
        return topicRepository.findBySubjectIdAndActiveTrue(subjectId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public TopicDto getTopicById(Long id) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found with id: " + id));
        return toDto(topic);
    }

    public Topic getTopicEntity(Long id) {
        return topicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found with id: " + id));
    }

    @Transactional
    public TopicDto createTopic(TopicDto dto) {
        Subject subject = subjectRepository.findById(dto.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: " + dto.getSubjectId()));

        Topic topic = new Topic();
        topic.setSubject(subject);
        topic.setName(dto.getName());
        topic.setDescription(dto.getDescription());
        topic.setDifficulty(dto.getDifficulty() != null ? dto.getDifficulty().toUpperCase() : "MEDIUM");
        topic.setActive(dto.getActive() != null ? dto.getActive() : true);

        return toDto(topicRepository.save(topic));
    }

    @Transactional
    public TopicDto updateTopic(Long id, TopicDto dto) {
        Topic topic = getTopicEntity(id);
        if (dto.getName() != null) topic.setName(dto.getName());
        if (dto.getDescription() != null) topic.setDescription(dto.getDescription());
        if (dto.getDifficulty() != null) topic.setDifficulty(dto.getDifficulty().toUpperCase());
        if (dto.getActive() != null) topic.setActive(dto.getActive());

        if (dto.getSubjectId() != null && !dto.getSubjectId().equals(topic.getSubject().getId())) {
            Subject subject = subjectRepository.findById(dto.getSubjectId())
                    .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: " + dto.getSubjectId()));
            topic.setSubject(subject);
        }

        return toDto(topicRepository.save(topic));
    }

    @Transactional
    public void deleteTopic(Long id) {
        if (!topicRepository.existsById(id)) {
            throw new ResourceNotFoundException("Topic not found with id: " + id);
        }
        topicRepository.deleteById(id);
    }

    public TopicDto toDto(Topic topic) {
        TopicDto dto = new TopicDto(
                topic.getId(),
                topic.getSubject() != null ? topic.getSubject().getId() : null,
                topic.getSubject() != null ? topic.getSubject().getName() : null,
                topic.getName(),
                topic.getDescription(),
                topic.getDifficulty(),
                topic.getActive(),
                topic.getCreatedAt()
        );
        dto.setQuestionCount(topic.getQuestions() != null ? topic.getQuestions().size() : 0);
        dto.setMaterialCount(topic.getStudyMaterials() != null ? topic.getStudyMaterials().size() : 0);
        return dto;
    }
}
