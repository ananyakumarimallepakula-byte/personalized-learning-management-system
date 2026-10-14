package com.personalizedlearning.service;

import com.personalizedlearning.dto.DashboardResponse;
import com.personalizedlearning.dto.SystemStatsResponse;
import com.personalizedlearning.dto.TopicPerformanceResponse;
import com.personalizedlearning.entity.Topic;
import com.personalizedlearning.entity.TopicPerformance;
import com.personalizedlearning.entity.User;
import com.personalizedlearning.exception.ResourceNotFoundException;
import com.personalizedlearning.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;
    private final TopicRepository topicRepository;
    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final StudyMaterialRepository studyMaterialRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final TopicPerformanceRepository topicPerformanceRepository;
    private final WeakTopicPredictionRepository predictionRepository;
    private final PerformanceAnalysisService performanceAnalysisService;
    private final WeakTopicPredictionService predictionService;
    private final RecommendationService recommendationService;
    private final QuizAttemptService quizAttemptService;

    public DashboardService(UserRepository userRepository,
                            SubjectRepository subjectRepository,
                            TopicRepository topicRepository,
                            QuizRepository quizRepository,
                            QuestionRepository questionRepository,
                            StudyMaterialRepository studyMaterialRepository,
                            QuizAttemptRepository quizAttemptRepository,
                            TopicPerformanceRepository topicPerformanceRepository,
                            WeakTopicPredictionRepository predictionRepository,
                            PerformanceAnalysisService performanceAnalysisService,
                            WeakTopicPredictionService predictionService,
                            RecommendationService recommendationService,
                            QuizAttemptService quizAttemptService) {
        this.userRepository = userRepository;
        this.subjectRepository = subjectRepository;
        this.topicRepository = topicRepository;
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
        this.studyMaterialRepository = studyMaterialRepository;
        this.quizAttemptRepository = quizAttemptRepository;
        this.topicPerformanceRepository = topicPerformanceRepository;
        this.predictionRepository = predictionRepository;
        this.performanceAnalysisService = performanceAnalysisService;
        this.predictionService = predictionService;
        this.recommendationService = recommendationService;
        this.quizAttemptService = quizAttemptService;
    }

    public DashboardResponse getStudentDashboard(Long studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));

        DashboardResponse resp = new DashboardResponse();
        resp.setStudentName(student.getName());

        List<TopicPerformanceResponse> allTopics = performanceAnalysisService.getStudentTopicPerformances(studentId);
        resp.setAllTopicPerformances(allTopics);

        int strong = 0;
        int weak = 0;
        int average = 0;
        double totalPct = 0.0;

        List<TopicPerformanceResponse> weakList = new ArrayList<>();

        for (TopicPerformanceResponse tp : allTopics) {
            totalPct += tp.getAveragePercentage();
            if ("WEAK".equalsIgnoreCase(tp.getStatus())) {
                weak++;
                weakList.add(tp);
            } else if ("STRONG".equalsIgnoreCase(tp.getStatus())) {
                strong++;
            } else {
                average++;
            }
        }

        resp.setStrongTopicsCount(strong);
        resp.setWeakTopicsCount(weak);
        resp.setAverageTopicsCount(average);
        resp.setWeakTopics(weakList);

        double overall = allTopics.isEmpty() ? 0.0 : Math.round((totalPct / allTopics.size()) * 100.0) / 100.0;
        resp.setOverallPerformance(overall);

        long quizzesCompleted = quizAttemptRepository.countByStudentId(studentId);
        resp.setQuizzesCompleted((int) quizzesCompleted);

        // Learning progress: ratio of topics attempted / total active topics
        long totalActiveTopics = topicRepository.count();
        double progress = totalActiveTopics == 0 ? 0.0 : Math.min(100.0, Math.round(((double) allTopics.size() / totalActiveTopics) * 100.0 * 100.0) / 100.0);
        resp.setLearningProgress(progress);

        resp.setSubjectPerformance(performanceAnalysisService.getStudentSubjectPerformances(studentId));
        resp.setRecentQuizScores(quizAttemptService.getAttemptsByStudent(studentId).stream().limit(5).collect(Collectors.toList()));
        resp.setPredictions(predictionService.getStudentPredictions(studentId));
        resp.setPredictedRiskTopicsCount((int) resp.getPredictions().stream().filter(p -> "HIGH".equalsIgnoreCase(p.getRiskLevel())).count());
        resp.setRecommendations(recommendationService.getStudentRecommendations(studentId).stream().limit(6).collect(Collectors.toList()));
        resp.setPracticeQuestions(recommendationService.getStudentPracticeQuestions(studentId).stream().limit(10).collect(Collectors.toList()));

        return resp;
    }

    public SystemStatsResponse getSystemStats() {
        SystemStatsResponse stats = new SystemStatsResponse();
        stats.setTotalUsers(userRepository.count());
        stats.setTotalStudents(userRepository.countByRole("STUDENT"));
        stats.setTotalTeachers(userRepository.countByRole("TEACHER"));
        stats.setTotalQuizzes(quizRepository.count());
        stats.setTotalAttempts(quizAttemptRepository.count());
        stats.setTotalSubjects(subjectRepository.count());
        stats.setTotalTopics(topicRepository.count());
        stats.setTotalQuestions(questionRepository.count());
        stats.setTotalMaterials(studyMaterialRepository.count());

        // Weak topics frequency
        List<TopicPerformance> allPerformances = topicPerformanceRepository.findAll();
        Map<String, Long> weakFreq = new HashMap<>();
        for (TopicPerformance tp : allPerformances) {
            if (tp.getAveragePercentage() != null && tp.getAveragePercentage() < 60.0 && tp.getTopic() != null) {
                weakFreq.put(tp.getTopic().getName(), weakFreq.getOrDefault(tp.getTopic().getName(), 0L) + 1L);
            }
        }
        stats.setWeakTopicsFrequency(weakFreq);

        // Subject averages across system
        Map<String, List<Double>> subjectScores = new HashMap<>();
        for (TopicPerformance tp : allPerformances) {
            if (tp.getTopic() != null && tp.getTopic().getSubject() != null && tp.getAveragePercentage() != null) {
                subjectScores.computeIfAbsent(tp.getTopic().getSubject().getName(), k -> new ArrayList<>()).add(tp.getAveragePercentage());
            }
        }
        Map<String, Double> subjectAvg = new LinkedHashMap<>();
        for (Map.Entry<String, List<Double>> entry : subjectScores.entrySet()) {
            double avg = entry.getValue().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            subjectAvg.put(entry.getKey(), Math.round(avg * 100.0) / 100.0);
        }
        stats.setSubjectAverages(subjectAvg);

        return stats;
    }
}
