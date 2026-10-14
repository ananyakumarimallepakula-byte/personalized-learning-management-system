package com.personalizedlearning.service;

import com.personalizedlearning.dto.AnswerSubmissionDto;
import com.personalizedlearning.dto.QuizResultResponse;
import com.personalizedlearning.dto.QuizSubmissionRequest;
import com.personalizedlearning.entity.*;
import com.personalizedlearning.exception.BadRequestException;
import com.personalizedlearning.exception.ResourceNotFoundException;
import com.personalizedlearning.repository.*;
import com.personalizedlearning.util.PerformanceCalculator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuizAttemptService {

    private final QuizAttemptRepository quizAttemptRepository;
    private final StudentAnswerRepository studentAnswerRepository;
    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final QuestionOptionRepository questionOptionRepository;
    private final UserRepository userRepository;
    private final PerformanceCalculator performanceCalculator;
    private final PerformanceAnalysisService performanceAnalysisService;
    private final WeakTopicPredictionService predictionService;
    private final RecommendationService recommendationService;

    public QuizAttemptService(QuizAttemptRepository quizAttemptRepository,
                              StudentAnswerRepository studentAnswerRepository,
                              QuizRepository quizRepository,
                              QuestionRepository questionRepository,
                              QuestionOptionRepository questionOptionRepository,
                              UserRepository userRepository,
                              PerformanceCalculator performanceCalculator,
                              PerformanceAnalysisService performanceAnalysisService,
                              WeakTopicPredictionService predictionService,
                              RecommendationService recommendationService) {
        this.quizAttemptRepository = quizAttemptRepository;
        this.studentAnswerRepository = studentAnswerRepository;
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
        this.questionOptionRepository = questionOptionRepository;
        this.userRepository = userRepository;
        this.performanceCalculator = performanceCalculator;
        this.performanceAnalysisService = performanceAnalysisService;
        this.predictionService = predictionService;
        this.recommendationService = recommendationService;
    }

    @Transactional
    public QuizResultResponse submitQuiz(QuizSubmissionRequest request) {
        if (request.getQuizId() == null) {
            throw new BadRequestException("Quiz ID cannot be null");
        }
        if (request.getStudentId() == null) {
            throw new BadRequestException("Student ID cannot be null");
        }

        Quiz quiz = quizRepository.findById(request.getQuizId())
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found with id: " + request.getQuizId()));

        User student = userRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + request.getStudentId()));

        List<Question> questions = questionRepository.findByQuizId(quiz.getId());
        if (questions.isEmpty()) {
            throw new BadRequestException("Quiz has no questions configured");
        }

        Map<Long, Long> submittedAnswerMap = new HashMap<>();
        if (request.getAnswers() != null) {
            for (AnswerSubmissionDto ans : request.getAnswers()) {
                if (ans.getQuestionId() != null && ans.getSelectedOptionId() != null) {
                    submittedAnswerMap.put(ans.getQuestionId(), ans.getSelectedOptionId());
                }
            }
        }

        QuizAttempt attempt = new QuizAttempt(student, quiz, LocalDateTime.now());
        attempt.setSubmittedAt(LocalDateTime.now());
        attempt.setTimeTakenSeconds(request.getTimeTakenSeconds() != null ? request.getTimeTakenSeconds() : 0);
        attempt.setTotalQuestions(questions.size());

        int correctCount = 0;
        int wrongCount = 0;
        double totalScore = 0.0;

        List<StudentAnswer> studentAnswers = new ArrayList<>();

        for (Question q : questions) {
            Long selectedOptionId = submittedAnswerMap.get(q.getId());
            boolean isCorrect = false;
            double marksObtained = 0.0;

            if (selectedOptionId != null) {
                Optional<QuestionOption> optOpt = questionOptionRepository.findById(selectedOptionId);
                if (optOpt.isPresent() && Boolean.TRUE.equals(optOpt.get().getIsCorrect())) {
                    isCorrect = true;
                    marksObtained = q.getMarks() != null ? q.getMarks() : 1.0;
                    correctCount++;
                    totalScore += marksObtained;
                } else {
                    wrongCount++;
                }
            } else {
                // Unanswered question counts as wrong
                wrongCount++;
            }

            StudentAnswer answer = new StudentAnswer(attempt, q, selectedOptionId, isCorrect, marksObtained);
            studentAnswers.add(answer);
        }

        double percentage = performanceCalculator.calculatePercentage(correctCount, questions.size());

        attempt.setCorrectAnswers(correctCount);
        attempt.setWrongAnswers(wrongCount);
        attempt.setScore(Math.round(totalScore * 100.0) / 100.0);
        attempt.setPercentage(percentage);

        QuizAttempt savedAttempt = quizAttemptRepository.save(attempt);
        for (StudentAnswer sa : studentAnswers) {
            sa.setQuizAttempt(savedAttempt);
            studentAnswerRepository.save(sa);
        }

        // STEP: Performance Analysis - Update TopicPerformance
        Topic topic = quiz.getTopic();
        TopicPerformance topicPerformance = performanceAnalysisService.updateTopicPerformance(student, topic, savedAttempt);

        // STEP: Weak Topic Detection & Prediction Engine
        WeakTopicPrediction prediction = predictionService.recalculateForStudentAndTopic(student, topic);

        // STEP: Recommendation Engine
        boolean isWeak = performanceCalculator.isWeak(topicPerformance.getAveragePercentage());
        if (isWeak || "HIGH".equalsIgnoreCase(prediction.getRiskLevel()) || "MEDIUM".equalsIgnoreCase(prediction.getRiskLevel())) {
            recommendationService.generateRecommendations(student, topic, prediction.getRiskLevel(), prediction.getReason());
        }

        // STEP: Assemble Response
        QuizResultResponse response = new QuizResultResponse();
        response.setAttemptId(savedAttempt.getId());
        response.setQuizId(quiz.getId());
        response.setQuizTitle(quiz.getTitle());
        response.setTopicId(topic.getId());
        response.setTopicName(topic.getName());
        if (quiz.getSubject() != null) {
            response.setSubjectName(quiz.getSubject().getName());
        }
        response.setScore(savedAttempt.getScore());
        response.setPercentage(savedAttempt.getPercentage());
        response.setTotalQuestions(savedAttempt.getTotalQuestions());
        response.setCorrectAnswers(savedAttempt.getCorrectAnswers());
        response.setWrongAnswers(savedAttempt.getWrongAnswers());
        response.setTimeTakenSeconds(savedAttempt.getTimeTakenSeconds());
        response.setSubmittedAt(savedAttempt.getSubmittedAt());

        response.setWeakDetected(isWeak);
        response.setRiskLevel(prediction.getRiskLevel());
        response.setPredictionReason(prediction.getReason());
        response.setPredictionScore(prediction.getPredictionScore());

        List<String> actions = new ArrayList<>();
        if (isWeak) {
            actions.add("Revise " + topic.getName() + " concepts");
            actions.add("Practice " + ( "HIGH".equalsIgnoreCase(prediction.getRiskLevel()) ? 10 : 5 ) + " questions");
            actions.add("Take another " + topic.getName() + " quiz");
        } else {
            actions.add("Great job! Keep reviewing to maintain strong mastery.");
        }
        response.setRecommendedActions(actions);

        response.setRecommendations(recommendationService.getStudentRecommendations(student.getId()));
        response.setPracticeQuestions(recommendationService.getPracticeQuestionsForTopic(student.getId(), topic.getId()));

        return response;
    }

    public QuizResultResponse getAttemptResult(Long attemptId) {
        QuizAttempt attempt = quizAttemptRepository.findById(attemptId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz attempt not found with id: " + attemptId));

        Topic topic = attempt.getQuiz().getTopic();
        User student = attempt.getStudent();

        TopicPerformance perf = performanceAnalysisService.getStudentTopicPerformances(student.getId()).stream()
                .filter(p -> p.getTopicId().equals(topic.getId()))
                .findFirst()
                .map(tp -> {
                    TopicPerformance mock = new TopicPerformance();
                    mock.setAveragePercentage(tp.getAveragePercentage());
                    return mock;
                }).orElse(null);

        double avgPercentage = perf != null ? perf.getAveragePercentage() : attempt.getPercentage();
        boolean isWeak = performanceCalculator.isWeak(avgPercentage);

        WeakTopicPrediction prediction = predictionService.recalculateForStudentAndTopic(student, topic);

        QuizResultResponse response = new QuizResultResponse();
        response.setAttemptId(attempt.getId());
        response.setQuizId(attempt.getQuiz().getId());
        response.setQuizTitle(attempt.getQuiz().getTitle());
        response.setTopicId(topic.getId());
        response.setTopicName(topic.getName());
        if (attempt.getQuiz().getSubject() != null) {
            response.setSubjectName(attempt.getQuiz().getSubject().getName());
        }
        response.setScore(attempt.getScore());
        response.setPercentage(attempt.getPercentage());
        response.setTotalQuestions(attempt.getTotalQuestions());
        response.setCorrectAnswers(attempt.getCorrectAnswers());
        response.setWrongAnswers(attempt.getWrongAnswers());
        response.setTimeTakenSeconds(attempt.getTimeTakenSeconds());
        response.setSubmittedAt(attempt.getSubmittedAt());

        response.setWeakDetected(isWeak);
        response.setRiskLevel(prediction.getRiskLevel());
        response.setPredictionReason(prediction.getReason());
        response.setPredictionScore(prediction.getPredictionScore());

        response.setRecommendations(recommendationService.getStudentRecommendations(student.getId()));
        response.setPracticeQuestions(recommendationService.getPracticeQuestionsForTopic(student.getId(), topic.getId()));

        return response;
    }

    public List<QuizResultResponse> getAttemptsByStudent(Long studentId) {
        return quizAttemptRepository.findByStudentIdOrderByStartedAtDesc(studentId).stream()
                .map(this::toResultSummary)
                .collect(Collectors.toList());
    }

    private QuizResultResponse toResultSummary(QuizAttempt attempt) {
        QuizResultResponse r = new QuizResultResponse();
        r.setAttemptId(attempt.getId());
        r.setQuizId(attempt.getQuiz().getId());
        r.setQuizTitle(attempt.getQuiz().getTitle());
        if (attempt.getQuiz().getTopic() != null) {
            r.setTopicId(attempt.getQuiz().getTopic().getId());
            r.setTopicName(attempt.getQuiz().getTopic().getName());
        }
        if (attempt.getQuiz().getSubject() != null) {
            r.setSubjectName(attempt.getQuiz().getSubject().getName());
        }
        r.setScore(attempt.getScore());
        r.setPercentage(attempt.getPercentage());
        r.setTotalQuestions(attempt.getTotalQuestions());
        r.setCorrectAnswers(attempt.getCorrectAnswers());
        r.setWrongAnswers(attempt.getWrongAnswers());
        r.setTimeTakenSeconds(attempt.getTimeTakenSeconds());
        r.setSubmittedAt(attempt.getSubmittedAt());
        return r;
    }
}
