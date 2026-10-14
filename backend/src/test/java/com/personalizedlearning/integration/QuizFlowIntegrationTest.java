package com.personalizedlearning.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.personalizedlearning.dto.AnswerSubmissionDto;
import com.personalizedlearning.dto.QuizSubmissionRequest;
import com.personalizedlearning.entity.Question;
import com.personalizedlearning.entity.QuestionOption;
import com.personalizedlearning.entity.Quiz;
import com.personalizedlearning.entity.User;
import com.personalizedlearning.repository.QuestionRepository;
import com.personalizedlearning.repository.QuizRepository;
import com.personalizedlearning.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class QuizFlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    public void testFullQuizAttemptToWeakTopicRecommendationFlow() throws Exception {
        User student = userRepository.findByEmail("student@learning.com")
                .orElseThrow(() -> new RuntimeException("Seeded student not found"));

        Quiz quiz = quizRepository.findAll().stream()
                .filter(q -> q.getTitle().contains("Inheritance"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Inheritance quiz not found"));

        List<Question> questions = questionRepository.findByQuizId(quiz.getId());
        List<AnswerSubmissionDto> answers = new ArrayList<>();

        // Intentionally answer 4 questions correctly and 6 questions incorrectly
        int count = 0;
        for (Question q : questions) {
            QuestionOption correctOpt = q.getOptions().stream().filter(QuestionOption::getIsCorrect).findFirst().orElse(null);
            QuestionOption wrongOpt = q.getOptions().stream().filter(o -> !o.getIsCorrect()).findFirst().orElse(null);

            if (count < 4 && correctOpt != null) {
                answers.add(new AnswerSubmissionDto(q.getId(), correctOpt.getId()));
            } else if (wrongOpt != null) {
                answers.add(new AnswerSubmissionDto(q.getId(), wrongOpt.getId()));
            }
            count++;
        }

        QuizSubmissionRequest submission = new QuizSubmissionRequest(
                quiz.getId(),
                student.getId(),
                360,
                answers
        );

        // Submit Quiz
        mockMvc.perform(post("/api/quiz-attempts/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(submission)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.percentage").value(40.0))
                .andExpect(jsonPath("$.correctAnswers").value(4))
                .andExpect(jsonPath("$.wrongAnswers").value(6))
                .andExpect(jsonPath("$.weakDetected").value(true))
                .andExpect(jsonPath("$.riskLevel").exists())
                .andExpect(jsonPath("$.recommendations").isArray());

        // Verify Performance API reflects weak status
        mockMvc.perform(get("/api/students/" + student.getId() + "/performance/weak-topics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        // Verify Predictions API
        mockMvc.perform(get("/api/students/" + student.getId() + "/predictions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        // Verify Recommendations API
        mockMvc.perform(get("/api/students/" + student.getId() + "/recommendations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        // Verify Student Dashboard
        mockMvc.perform(get("/api/students/" + student.getId() + "/dashboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentName").value("Alex Johnson"))
                .andExpect(jsonPath("$.weakTopicsCount").exists());
    }
}
