package com.personalizedlearning.util;

import com.personalizedlearning.entity.*;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class RecommendationEngine {

    public List<StudyMaterial> selectMaterialsForRisk(String riskLevel, List<StudyMaterial> availableMaterials, Set<Long> completedMaterialIds) {
        if (availableMaterials == null || availableMaterials.isEmpty()) {
            return Collections.emptyList();
        }

        List<StudyMaterial> uncompleted = availableMaterials.stream()
                .filter(m -> !completedMaterialIds.contains(m.getId()))
                .collect(Collectors.toList());

        List<StudyMaterial> source = uncompleted.isEmpty() ? availableMaterials : uncompleted;
        List<StudyMaterial> selected = new ArrayList<>();

        if ("HIGH".equalsIgnoreCase(riskLevel)) {
            // Notes and Videos prioritized for HIGH risk
            source.stream()
                    .filter(m -> "NOTES".equalsIgnoreCase(m.getMaterialType()) || "PDF".equalsIgnoreCase(m.getMaterialType()))
                    .findFirst()
                    .ifPresent(selected::add);

            source.stream()
                    .filter(m -> "VIDEO".equalsIgnoreCase(m.getMaterialType()))
                    .findFirst()
                    .ifPresent(selected::add);

            // Add other types if slots available
            for (StudyMaterial m : source) {
                if (!selected.contains(m) && selected.size() < 4) {
                    selected.add(m);
                }
            }
        } else if ("MEDIUM".equalsIgnoreCase(riskLevel)) {
            // Notes and articles for MEDIUM risk
            source.stream()
                    .filter(m -> "NOTES".equalsIgnoreCase(m.getMaterialType()) || "ARTICLE".equalsIgnoreCase(m.getMaterialType()))
                    .findFirst()
                    .ifPresent(selected::add);

            for (StudyMaterial m : source) {
                if (!selected.contains(m) && selected.size() < 2) {
                    selected.add(m);
                }
            }
        } else {
            // LOW risk: revision summary / links
            if (!source.isEmpty()) {
                selected.add(source.get(0));
            }
        }

        return selected;
    }

    public int getQuestionCountForRisk(String riskLevel) {
        if ("HIGH".equalsIgnoreCase(riskLevel)) {
            return 10;
        } else if ("MEDIUM".equalsIgnoreCase(riskLevel)) {
            return 5;
        } else {
            return 3;
        }
    }

    public List<Question> prioritizePracticeQuestions(List<Question> allTopicQuestions,
                                                      Set<Long> attemptedQuestionIds,
                                                      Set<Long> incorrectQuestionIds,
                                                      int targetCount) {
        if (allTopicQuestions == null || allTopicQuestions.isEmpty()) {
            return Collections.emptyList();
        }

        // Priority 1: Unattempted questions
        List<Question> unattempted = allTopicQuestions.stream()
                .filter(q -> !attemptedQuestionIds.contains(q.getId()))
                .sorted(Comparator.comparingInt(this::difficultyRank))
                .collect(Collectors.toList());

        // Priority 2: Previously answered incorrectly
        List<Question> incorrect = allTopicQuestions.stream()
                .filter(q -> incorrectQuestionIds.contains(q.getId()))
                .sorted(Comparator.comparingInt(this::difficultyRank))
                .collect(Collectors.toList());

        // Priority 3: Other attempted questions
        List<Question> otherAttempted = allTopicQuestions.stream()
                .filter(q -> attemptedQuestionIds.contains(q.getId()) && !incorrectQuestionIds.contains(q.getId()))
                .sorted(Comparator.comparingInt(this::difficultyRank))
                .collect(Collectors.toList());

        LinkedHashSet<Question> result = new LinkedHashSet<>();
        for (Question q : unattempted) {
            if (result.size() >= targetCount) break;
            result.add(q);
        }
        for (Question q : incorrect) {
            if (result.size() >= targetCount) break;
            result.add(q);
        }
        for (Question q : otherAttempted) {
            if (result.size() >= targetCount) break;
            result.add(q);
        }

        return new ArrayList<>(result);
    }

    private int difficultyRank(Question q) {
        if (q.getDifficulty() == null) return 2;
        switch (q.getDifficulty().toUpperCase()) {
            case "MEDIUM": return 1;
            case "HARD": return 2;
            case "EASY": return 3;
            default: return 2;
        }
    }
}
