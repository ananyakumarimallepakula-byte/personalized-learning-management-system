package com.personalizedlearning.service;

import com.personalizedlearning.dto.TopicPerformanceResponse;
import com.personalizedlearning.entity.*;
import com.personalizedlearning.exception.ResourceNotFoundException;
import com.personalizedlearning.repository.SubjectRepository;
import com.personalizedlearning.repository.TopicPerformanceRepository;
import com.personalizedlearning.repository.TopicRepository;
import com.personalizedlearning.repository.UserRepository;
import com.personalizedlearning.util.PerformanceCalculator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PerformanceAnalysisService {

    private final TopicPerformanceRepository topicPerformanceRepository;
    private final TopicRepository topicRepository;
    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;
    private final PerformanceCalculator performanceCalculator;

    public PerformanceAnalysisService(TopicPerformanceRepository topicPerformanceRepository,
                                      TopicRepository topicRepository,
                                      SubjectRepository subjectRepository,
                                      UserRepository userRepository,
                                      PerformanceCalculator performanceCalculator) {
        this.topicPerformanceRepository = topicPerformanceRepository;
        this.topicRepository = topicRepository;
        this.subjectRepository = subjectRepository;
        this.userRepository = userRepository;
        this.performanceCalculator = performanceCalculator;
    }

    @Transactional
    public TopicPerformance updateTopicPerformance(User student, Topic topic, QuizAttempt attempt) {
        TopicPerformance perf = topicPerformanceRepository.findByStudentIdAndTopicId(student.getId(), topic.getId())
                .orElseGet(() -> new TopicPerformance(student, topic));

        double previousAverage = perf.getAveragePercentage() != null ? perf.getAveragePercentage() : 0.0;

        int newTotalAttempts = (perf.getTotalAttempts() != null ? perf.getTotalAttempts() : 0) + 1;
        int newTotalQuestions = (perf.getTotalQuestions() != null ? perf.getTotalQuestions() : 0) + attempt.getTotalQuestions();
        int newCorrectAnswers = (perf.getCorrectAnswers() != null ? perf.getCorrectAnswers() : 0) + attempt.getCorrectAnswers();
        int newWrongAnswers = (perf.getWrongAnswers() != null ? perf.getWrongAnswers() : 0) + attempt.getWrongAnswers();

        double newAvgPercentage = performanceCalculator.calculatePercentage(newCorrectAnswers, newTotalQuestions);
        double newAvgScore = ((double) newCorrectAnswers / newTotalAttempts);

        String newTrend = "STABLE";
        if (perf.getTotalAttempts() > 0) {
            newTrend = performanceCalculator.calculateTrend(previousAverage, attempt.getPercentage());
        }

        perf.setTotalAttempts(newTotalAttempts);
        perf.setTotalQuestions(newTotalQuestions);
        perf.setCorrectAnswers(newCorrectAnswers);
        perf.setWrongAnswers(newWrongAnswers);
        perf.setAveragePercentage(newAvgPercentage);
        perf.setAverageScore(Math.round(newAvgScore * 100.0) / 100.0);
        perf.setTrend(newTrend);
        perf.setLastAttemptAt(LocalDateTime.now());

        return topicPerformanceRepository.save(perf);
    }

    public List<TopicPerformanceResponse> getStudentTopicPerformances(Long studentId) {
        if (!userRepository.existsById(studentId)) {
            throw new ResourceNotFoundException("Student not found with id: " + studentId);
        }

        List<TopicPerformance> performances = topicPerformanceRepository.findByStudentId(studentId);
        return performances.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Map<String, Double> getStudentSubjectPerformances(Long studentId) {
        List<TopicPerformance> performances = topicPerformanceRepository.findByStudentId(studentId);
        Map<String, List<Double>> subjectScores = new HashMap<>();

        for (TopicPerformance tp : performances) {
            if (tp.getTopic() != null && tp.getTopic().getSubject() != null) {
                String subName = tp.getTopic().getSubject().getName();
                subjectScores.computeIfAbsent(subName, k -> new ArrayList<>()).add(tp.getAveragePercentage());
            }
        }

        Map<String, Double> result = new LinkedHashMap<>();
        for (Map.Entry<String, List<Double>> entry : subjectScores.entrySet()) {
            double avg = entry.getValue().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            result.put(entry.getKey(), Math.round(avg * 100.0) / 100.0);
        }
        return result;
    }

    public List<TopicPerformanceResponse> getWeakTopicsForStudent(Long studentId) {
        return getStudentTopicPerformances(studentId).stream()
                .filter(tp -> "WEAK".equalsIgnoreCase(tp.getStatus()))
                .collect(Collectors.toList());
    }

    public TopicPerformanceResponse toResponse(TopicPerformance perf) {
        TopicPerformanceResponse resp = new TopicPerformanceResponse();
        resp.setTopicId(perf.getTopic().getId());
        resp.setTopicName(perf.getTopic().getName());
        if (perf.getTopic().getSubject() != null) {
            resp.setSubjectId(perf.getTopic().getSubject().getId());
            resp.setSubjectName(perf.getTopic().getSubject().getName());
        }
        resp.setTotalAttempts(perf.getTotalAttempts());
        resp.setTotalQuestions(perf.getTotalQuestions());
        resp.setCorrectAnswers(perf.getCorrectAnswers());
        resp.setWrongAnswers(perf.getWrongAnswers());
        resp.setAverageScore(perf.getAverageScore());
        resp.setAveragePercentage(perf.getAveragePercentage());
        resp.setLastAttemptAt(perf.getLastAttemptAt());
        resp.setTrend(perf.getTrend());
        resp.setStatus(performanceCalculator.classifyStatus(perf.getAveragePercentage()));
        return resp;
    }
}
