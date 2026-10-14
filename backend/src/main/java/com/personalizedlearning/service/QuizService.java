package com.personalizedlearning.service;

import com.personalizedlearning.dto.QuestionDto;
import com.personalizedlearning.dto.QuizDto;
import com.personalizedlearning.entity.Quiz;
import com.personalizedlearning.entity.Subject;
import com.personalizedlearning.entity.Topic;
import com.personalizedlearning.exception.ResourceNotFoundException;
import com.personalizedlearning.repository.QuizRepository;
import com.personalizedlearning.repository.SubjectRepository;
import com.personalizedlearning.repository.TopicRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuizService {

    private final QuizRepository quizRepository;
    private final SubjectRepository subjectRepository;
    private final TopicRepository topicRepository;
    private final QuestionService questionService;

    public QuizService(QuizRepository quizRepository,
                       SubjectRepository subjectRepository,
                       TopicRepository topicRepository,
                       QuestionService questionService) {
        this.quizRepository = quizRepository;
        this.subjectRepository = subjectRepository;
        this.topicRepository = topicRepository;
        this.questionService = questionService;
    }

    public List<QuizDto> getAllQuizzes() {
        return quizRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<QuizDto> getActiveQuizzes() {
        return quizRepository.findByActiveTrue().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<QuizDto> getQuizzesBySubject(Long subjectId) {
        return quizRepository.findBySubjectIdAndActiveTrue(subjectId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<QuizDto> getQuizzesByTopic(Long topicId) {
        return quizRepository.findByTopicIdAndActiveTrue(topicId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public QuizDto getQuizById(Long id) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found with id: " + id));
        QuizDto dto = toDto(quiz);
        // Include questions
        List<QuestionDto> questions = questionService.getQuestionsByQuiz(id);
        dto.setQuestions(questions);
        dto.setQuestionCount(questions.size());
        return dto;
    }

    public Quiz getQuizEntity(Long id) {
        return quizRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found with id: " + id));
    }

    @Transactional
    public QuizDto createQuiz(QuizDto dto, Long createdBy) {
        Subject subject = subjectRepository.findById(dto.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: " + dto.getSubjectId()));

        Topic topic = topicRepository.findById(dto.getTopicId())
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found with id: " + dto.getTopicId()));

        Quiz quiz = new Quiz();
        quiz.setSubject(subject);
        quiz.setTopic(topic);
        quiz.setTitle(dto.getTitle());
        quiz.setDescription(dto.getDescription());
        quiz.setDifficulty(dto.getDifficulty() != null ? dto.getDifficulty().toUpperCase() : "MEDIUM");
        quiz.setDurationMinutes(dto.getDurationMinutes() != null ? dto.getDurationMinutes() : 15);
        quiz.setTotalMarks(dto.getTotalMarks() != null ? dto.getTotalMarks() : 10);
        quiz.setCreatedBy(createdBy);
        quiz.setActive(dto.getActive() != null ? dto.getActive() : true);

        Quiz saved = quizRepository.save(quiz);

        if (dto.getQuestions() != null && !dto.getQuestions().isEmpty()) {
            for (QuestionDto qDto : dto.getQuestions()) {
                qDto.setQuizId(saved.getId());
                qDto.setTopicId(topic.getId());
                questionService.createQuestion(qDto);
            }
        }

        return getQuizById(saved.getId());
    }

    @Transactional
    public QuizDto updateQuiz(Long id, QuizDto dto) {
        Quiz quiz = getQuizEntity(id);

        if (dto.getTitle() != null) quiz.setTitle(dto.getTitle());
        if (dto.getDescription() != null) quiz.setDescription(dto.getDescription());
        if (dto.getDifficulty() != null) quiz.setDifficulty(dto.getDifficulty().toUpperCase());
        if (dto.getDurationMinutes() != null) quiz.setDurationMinutes(dto.getDurationMinutes());
        if (dto.getTotalMarks() != null) quiz.setTotalMarks(dto.getTotalMarks());
        if (dto.getActive() != null) quiz.setActive(dto.getActive());

        if (dto.getSubjectId() != null && !dto.getSubjectId().equals(quiz.getSubject().getId())) {
            Subject subject = subjectRepository.findById(dto.getSubjectId())
                    .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: " + dto.getSubjectId()));
            quiz.setSubject(subject);
        }

        if (dto.getTopicId() != null && !dto.getTopicId().equals(quiz.getTopic().getId())) {
            Topic topic = topicRepository.findById(dto.getTopicId())
                    .orElseThrow(() -> new ResourceNotFoundException("Topic not found with id: " + dto.getTopicId()));
            quiz.setTopic(topic);
        }

        return toDto(quizRepository.save(quiz));
    }

    @Transactional
    public void deleteQuiz(Long id) {
        if (!quizRepository.existsById(id)) {
            throw new ResourceNotFoundException("Quiz not found with id: " + id);
        }
        quizRepository.deleteById(id);
    }

    public QuizDto toDto(Quiz quiz) {
        int questionCount = quiz.getQuestions() != null ? quiz.getQuestions().size() : 0;
        return new QuizDto(
                quiz.getId(),
                quiz.getSubject() != null ? quiz.getSubject().getId() : null,
                quiz.getSubject() != null ? quiz.getSubject().getName() : null,
                quiz.getTopic() != null ? quiz.getTopic().getId() : null,
                quiz.getTopic() != null ? quiz.getTopic().getName() : null,
                quiz.getTitle(),
                quiz.getDescription(),
                quiz.getDifficulty(),
                quiz.getDurationMinutes(),
                quiz.getTotalMarks(),
                quiz.getCreatedBy(),
                quiz.getCreatedAt(),
                quiz.getActive(),
                questionCount,
                null
        );
    }
}
