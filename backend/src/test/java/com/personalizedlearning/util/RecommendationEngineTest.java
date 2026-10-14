package com.personalizedlearning.util;

import com.personalizedlearning.entity.Question;
import com.personalizedlearning.entity.StudyMaterial;
import com.personalizedlearning.entity.Topic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class RecommendationEngineTest {

    private RecommendationEngine engine;

    @BeforeEach
    public void setUp() {
        engine = new RecommendationEngine();
    }

    @Test
    public void testMaterialSelectionForHighRisk() {
        Topic topic = new Topic();
        topic.setId(1L);
        topic.setName("Inheritance");

        StudyMaterial notes = new StudyMaterial(topic, "Notes", "Desc", "NOTES", "url", "content", "MEDIUM", 1L);
        notes.setId(101L);
        StudyMaterial video = new StudyMaterial(topic, "Video", "Desc", "VIDEO", "url", "content", "HARD", 1L);
        video.setId(102L);

        List<StudyMaterial> materials = Arrays.asList(notes, video);
        List<StudyMaterial> selected = engine.selectMaterialsForRisk("HIGH", materials, Collections.emptySet());

        assertEquals(2, selected.size());
        assertTrue(selected.stream().anyMatch(m -> "NOTES".equalsIgnoreCase(m.getMaterialType())));
        assertTrue(selected.stream().anyMatch(m -> "VIDEO".equalsIgnoreCase(m.getMaterialType())));
    }

    @Test
    public void testPrioritizePracticeQuestions() {
        Topic topic = new Topic();
        topic.setId(1L);

        Question q1 = new Question(null, topic, "Q1 unattempted", "MEDIUM", 1, "exp");
        q1.setId(1L);
        Question q2 = new Question(null, topic, "Q2 incorrect", "HARD", 1, "exp");
        q2.setId(2L);
        Question q3 = new Question(null, topic, "Q3 already correct", "EASY", 1, "exp");
        q3.setId(3L);

        List<Question> allQuestions = Arrays.asList(q3, q2, q1);
        Set<Long> attempted = new HashSet<>(Arrays.asList(2L, 3L));
        Set<Long> incorrect = new HashSet<>(Collections.singletonList(2L));

        List<Question> prioritized = engine.prioritizePracticeQuestions(allQuestions, attempted, incorrect, 3);

        assertEquals(3, prioritized.size());
        // Q1 (unattempted) comes first
        assertEquals(1L, prioritized.get(0).getId());
        // Q2 (previously incorrect) comes second
        assertEquals(2L, prioritized.get(1).getId());
    }

    @Test
    public void testQuestionCountForRisk() {
        assertEquals(10, engine.getQuestionCountForRisk("HIGH"));
        assertEquals(5, engine.getQuestionCountForRisk("MEDIUM"));
        assertEquals(3, engine.getQuestionCountForRisk("LOW"));
    }
}
