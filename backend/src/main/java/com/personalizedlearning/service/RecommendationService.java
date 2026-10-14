package com.personalizedlearning.service;

import com.personalizedlearning.dto.QuestionOptionDto;
import com.personalizedlearning.dto.RecommendationResponse;
import com.personalizedlearning.dto.RecommendedQuestionResponse;
import com.personalizedlearning.entity.*;
import com.personalizedlearning.exception.ResourceNotFoundException;
import com.personalizedlearning.repository.*;
import com.personalizedlearning.util.RecommendationEngine;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;
    private final RecommendedQuestionRepository recommendedQuestionRepository;
    private final StudyMaterialRepository studyMaterialRepository;
    private final QuestionRepository questionRepository;
    private final StudentAnswerRepository studentAnswerRepository;
    private final RecommendationEngine recommendationEngine;

    public RecommendationService(RecommendationRepository recommendationRepository,
                                  RecommendedQuestionRepository recommendedQuestionRepository,
                                  StudyMaterialRepository studyMaterialRepository,
                                  QuestionRepository questionRepository,
                                  StudentAnswerRepository studentAnswerRepository,
                                  RecommendationEngine recommendationEngine) {
        this.recommendationRepository = recommendationRepository;
        this.recommendedQuestionRepository = recommendedQuestionRepository;
        this.studyMaterialRepository = studyMaterialRepository;
        this.questionRepository = questionRepository;
        this.studentAnswerRepository = studentAnswerRepository;
        this.recommendationEngine = recommendationEngine;
    }

    @Transactional
    public List<RecommendationResponse> generateRecommendations(User student, Topic topic, String riskLevel, String reason) {
        List<StudyMaterial> availableMaterials = studyMaterialRepository.findByTopicIdAndActiveTrue(topic.getId());

        Set<Long> completedIds = recommendationRepository.findByStudentIdAndTopicId(student.getId(), topic.getId())
                .stream()
                .filter(Recommendation::getCompleted)
                .map(r -> r.getMaterial() != null ? r.getMaterial().getId() : -1L)
                .collect(Collectors.toSet());

        List<StudyMaterial> selected = recommendationEngine.selectMaterialsForRisk(riskLevel, availableMaterials, completedIds);

        List<Recommendation> saved = new ArrayList<>();
        for (StudyMaterial sm : selected) {
            boolean exists = recommendationRepository.existsByStudentIdAndTopicIdAndMaterialId(student.getId(), topic.getId(), sm.getId());
            if (!exists) {
                String priority = "HIGH".equalsIgnoreCase(riskLevel) ? "HIGH" : ("MEDIUM".equalsIgnoreCase(riskLevel) ? "MEDIUM" : "LOW");
                String recommendationReason = "Recommended due to " + riskLevel + " risk in " + topic.getName();
                Recommendation rec = new Recommendation(student, topic, sm, priority, recommendationReason);
                saved.add(recommendationRepository.save(rec));
            }
        }

        // Also generate practice questions
        generatePracticeQuestions(student, topic, riskLevel);

        return getStudentRecommendations(student.getId());
    }

    @Transactional
    public List<RecommendedQuestionResponse> generatePracticeQuestions(User student, Topic topic, String riskLevel) {
        int targetCount = recommendationEngine.getQuestionCountForRisk(riskLevel);

        List<Question> allTopicQuestions = questionRepository.findByTopicId(topic.getId());
        Set<Long> attemptedIds = new HashSet<>(studentAnswerRepository.findAttemptedQuestionIdsByStudentId(student.getId()));
        Set<Long> incorrectIds = new HashSet<>(studentAnswerRepository.findIncorrectQuestionIdsByStudentId(student.getId()));

        List<Question> selectedQuestions = recommendationEngine.prioritizePracticeQuestions(
                allTopicQuestions, attemptedIds, incorrectIds, targetCount
        );

        for (Question q : selectedQuestions) {
            if (!recommendedQuestionRepository.existsByStudentIdAndQuestionId(student.getId(), q.getId())) {
                String priority = "HIGH".equalsIgnoreCase(riskLevel) ? "HIGH" : "MEDIUM";
                String reason = "Personalized practice for weak topic " + topic.getName();
                RecommendedQuestion rq = new RecommendedQuestion(student, q, topic, priority, reason);
                recommendedQuestionRepository.save(rq);
            }
        }

        return getStudentPracticeQuestions(student.getId());
    }

    public List<RecommendationResponse> getStudentRecommendations(Long studentId) {
        return recommendationRepository.findByStudentIdOrderByRecommendedAtDesc(studentId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<RecommendedQuestionResponse> getStudentPracticeQuestions(Long studentId) {
        return recommendedQuestionRepository.findByStudentIdOrderByCreatedAtDesc(studentId).stream()
                .map(this::toQuestionResponse)
                .collect(Collectors.toList());
    }

    public List<RecommendedQuestionResponse> getPracticeQuestionsForTopic(Long studentId, Long topicId) {
        return recommendedQuestionRepository.findByStudentIdAndTopicIdAndCompletedFalse(studentId, topicId).stream()
                .map(this::toQuestionResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public RecommendationResponse markRecommendationCompleted(Long id) {
        Recommendation rec = recommendationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recommendation not found with id: " + id));
        rec.setCompleted(true);
        return toResponse(recommendationRepository.save(rec));
    }

    @Transactional
    public RecommendedQuestionResponse markPracticeQuestionCompleted(Long id) {
        RecommendedQuestion rq = recommendedQuestionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recommended question not found with id: " + id));
        rq.setCompleted(true);
        return toQuestionResponse(recommendedQuestionRepository.save(rq));
    }

    public RecommendationResponse toResponse(Recommendation rec) {
        RecommendationResponse resp = new RecommendationResponse();
        resp.setId(rec.getId());
        resp.setStudentId(rec.getStudent().getId());
        resp.setTopicId(rec.getTopic().getId());
        resp.setTopicName(rec.getTopic().getName());
        if (rec.getTopic().getSubject() != null) {
            resp.setSubjectName(rec.getTopic().getSubject().getName());
        }
        if (rec.getMaterial() != null) {
            resp.setMaterialId(rec.getMaterial().getId());
            resp.setMaterialTitle(rec.getMaterial().getTitle());
            resp.setMaterialType(rec.getMaterial().getMaterialType());
            resp.setContentUrl(rec.getMaterial().getContentUrl());
            resp.setContent(rec.getMaterial().getContent());
        }
        resp.setPriority(rec.getPriority());
        resp.setReason(rec.getReason());
        resp.setRecommendedAt(rec.getRecommendedAt());
        resp.setCompleted(rec.getCompleted());
        return resp;
    }

    public RecommendedQuestionResponse toQuestionResponse(RecommendedQuestion rq) {
        Question q = rq.getQuestion();
        List<QuestionOptionDto> options = q.getOptions().stream()
                .map(o -> new QuestionOptionDto(o.getId(), o.getOptionText(), o.getIsCorrect()))
                .collect(Collectors.toList());

        RecommendedQuestionResponse resp = new RecommendedQuestionResponse();
        resp.setId(rq.getId());
        resp.setStudentId(rq.getStudent().getId());
        resp.setQuestionId(q.getId());
        resp.setQuestionText(q.getQuestionText());
        resp.setTopicId(rq.getTopic().getId());
        resp.setTopicName(rq.getTopic().getName());
        if (rq.getTopic().getSubject() != null) {
            resp.setSubjectName(rq.getTopic().getSubject().getName());
        }
        resp.setDifficulty(q.getDifficulty());
        resp.setMarks(q.getMarks());
        resp.setExplanation(q.getExplanation());
        resp.setPriority(rq.getPriority());
        resp.setReason(rq.getReason());
        resp.setCompleted(rq.getCompleted());
        resp.setCreatedAt(rq.getCreatedAt());
        resp.setOptions(options);
        return resp;
    }
}
