package com.personalizedlearning.service;

import com.personalizedlearning.dto.QuestionDto;
import com.personalizedlearning.dto.QuestionOptionDto;
import com.personalizedlearning.entity.Question;
import com.personalizedlearning.entity.QuestionOption;
import com.personalizedlearning.entity.Quiz;
import com.personalizedlearning.entity.Topic;
import com.personalizedlearning.exception.BadRequestException;
import com.personalizedlearning.exception.ResourceNotFoundException;
import com.personalizedlearning.repository.QuestionOptionRepository;
import com.personalizedlearning.repository.QuestionRepository;
import com.personalizedlearning.repository.QuizRepository;
import com.personalizedlearning.repository.TopicRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionOptionRepository questionOptionRepository;
    private final QuizRepository quizRepository;
    private final TopicRepository topicRepository;

    public QuestionService(QuestionRepository questionRepository,
                           QuestionOptionRepository questionOptionRepository,
                           QuizRepository quizRepository,
                           TopicRepository topicRepository) {
        this.questionRepository = questionRepository;
        this.questionOptionRepository = questionOptionRepository;
        this.quizRepository = quizRepository;
        this.topicRepository = topicRepository;
    }

    public List<QuestionDto> getAllQuestions() {
        return questionRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<QuestionDto> getQuestionsByQuiz(Long quizId) {
        return questionRepository.findByQuizId(quizId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<QuestionDto> getQuestionsByTopic(Long topicId) {
        return questionRepository.findByTopicId(topicId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public QuestionDto getQuestionById(Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with id: " + id));
        return toDto(question);
    }

    @Transactional
    public QuestionDto createQuestion(QuestionDto dto) {
        Topic topic = topicRepository.findById(dto.getTopicId())
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found with id: " + dto.getTopicId()));

        Quiz quiz = null;
        if (dto.getQuizId() != null) {
            quiz = quizRepository.findById(dto.getQuizId())
                    .orElseThrow(() -> new ResourceNotFoundException("Quiz not found with id: " + dto.getQuizId()));
        }

        if (dto.getOptions() == null || dto.getOptions().size() < 2) {
            throw new BadRequestException("A question must have at least 2 options (normally 4)");
        }

        boolean hasCorrect = dto.getOptions().stream().anyMatch(o -> Boolean.TRUE.equals(o.getIsCorrect()));
        if (!hasCorrect) {
            throw new BadRequestException("At least one option must be marked as correct");
        }

        Question question = new Question();
        question.setTopic(topic);
        question.setQuiz(quiz);
        question.setQuestionText(dto.getQuestionText());
        question.setDifficulty(dto.getDifficulty() != null ? dto.getDifficulty().toUpperCase() : "MEDIUM");
        question.setMarks(dto.getMarks() != null ? dto.getMarks() : 1);
        question.setExplanation(dto.getExplanation());

        for (QuestionOptionDto optDto : dto.getOptions()) {
            QuestionOption opt = new QuestionOption();
            opt.setOptionText(optDto.getOptionText());
            opt.setIsCorrect(Boolean.TRUE.equals(optDto.getIsCorrect()));
            question.addOption(opt);
        }

        return toDto(questionRepository.save(question));
    }

    @Transactional
    public QuestionDto updateQuestion(Long id, QuestionDto dto) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with id: " + id));

        if (dto.getQuestionText() != null) question.setQuestionText(dto.getQuestionText());
        if (dto.getDifficulty() != null) question.setDifficulty(dto.getDifficulty().toUpperCase());
        if (dto.getMarks() != null) question.setMarks(dto.getMarks());
        if (dto.getExplanation() != null) question.setExplanation(dto.getExplanation());

        if (dto.getTopicId() != null && !dto.getTopicId().equals(question.getTopic().getId())) {
            Topic topic = topicRepository.findById(dto.getTopicId())
                    .orElseThrow(() -> new ResourceNotFoundException("Topic not found with id: " + dto.getTopicId()));
            question.setTopic(topic);
        }

        if (dto.getOptions() != null && !dto.getOptions().isEmpty()) {
            question.getOptions().clear();
            for (QuestionOptionDto optDto : dto.getOptions()) {
                QuestionOption opt = new QuestionOption();
                opt.setOptionText(optDto.getOptionText());
                opt.setIsCorrect(Boolean.TRUE.equals(optDto.getIsCorrect()));
                question.addOption(opt);
            }
        }

        return toDto(questionRepository.save(question));
    }

    @Transactional
    public void deleteQuestion(Long id) {
        if (!questionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Question not found with id: " + id);
        }
        questionRepository.deleteById(id);
    }

    public QuestionDto toDto(Question question) {
        String topicName = question.getTopic() != null ? question.getTopic().getName() : null;
        String subjectName = (question.getTopic() != null && question.getTopic().getSubject() != null)
                ? question.getTopic().getSubject().getName() : null;

        List<QuestionOptionDto> optionDtos = question.getOptions().stream()
                .map(opt -> new QuestionOptionDto(opt.getId(), opt.getOptionText(), opt.getIsCorrect()))
                .collect(Collectors.toList());

        return new QuestionDto(
                question.getId(),
                question.getQuiz() != null ? question.getQuiz().getId() : null,
                question.getTopic() != null ? question.getTopic().getId() : null,
                topicName,
                subjectName,
                question.getQuestionText(),
                question.getDifficulty(),
                question.getMarks(),
                question.getExplanation(),
                question.getCreatedAt(),
                optionDtos
        );
    }
}
