package com.personalizedlearning.service;

import com.personalizedlearning.dto.SubjectDto;
import com.personalizedlearning.entity.Subject;
import com.personalizedlearning.exception.ResourceNotFoundException;
import com.personalizedlearning.repository.SubjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class SubjectService {

    private final SubjectRepository subjectRepository;

    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public List<SubjectDto> getAllSubjects() {
        return subjectRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<SubjectDto> getActiveSubjects() {
        return subjectRepository.findByActiveTrue().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public SubjectDto getSubjectById(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: " + id));
        return toDto(subject);
    }

    public Subject getSubjectEntity(Long id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: " + id));
    }

    @Transactional
    public SubjectDto createSubject(SubjectDto dto, Long createdBy) {
        Subject subject = new Subject();
        subject.setName(dto.getName());
        subject.setDescription(dto.getDescription());
        subject.setCreatedBy(createdBy);
        subject.setActive(dto.getActive() != null ? dto.getActive() : true);
        return toDto(subjectRepository.save(subject));
    }

    @Transactional
    public SubjectDto updateSubject(Long id, SubjectDto dto) {
        Subject subject = getSubjectEntity(id);
        if (dto.getName() != null) subject.setName(dto.getName());
        if (dto.getDescription() != null) subject.setDescription(dto.getDescription());
        if (dto.getActive() != null) subject.setActive(dto.getActive());
        return toDto(subjectRepository.save(subject));
    }

    @Transactional
    public void deleteSubject(Long id) {
        if (!subjectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Subject not found with id: " + id);
        }
        subjectRepository.deleteById(id);
    }

    public SubjectDto toDto(Subject subject) {
        return new SubjectDto(
                subject.getId(),
                subject.getName(),
                subject.getDescription(),
                subject.getCreatedBy(),
                subject.getCreatedAt(),
                subject.getActive(),
                subject.getTopics() != null ? subject.getTopics().size() : 0
        );
    }
}
