package com.personalizedlearning.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class PredictionCalculatorTest {

    private PredictionCalculator calculator;

    @BeforeEach
    public void setUp() {
        calculator = new PredictionCalculator(40.0, 25.0, 15.0, 10.0, 10.0);
    }

    @Test
    public void testCalculateHistoricalRisk() {
        assertEquals(55.0, calculator.calculateHistoricalRisk(45.0));
        assertEquals(18.0, calculator.calculateHistoricalRisk(82.0));
        assertEquals(0.0, calculator.calculateHistoricalRisk(100.0));
    }

    @Test
    public void testCalculateRecentRisk() {
        assertEquals(60.0, calculator.calculateRecentPerformanceRisk(40.0));
        assertEquals(25.0, calculator.calculateRecentPerformanceRisk(75.0));
    }

    @Test
    public void testCalculateWrongAnswerRisk() {
        assertEquals(50.0, calculator.calculateWrongAnswerRisk(5, 10));
        assertEquals(70.0, calculator.calculateWrongAnswerRisk(7, 10));
        assertEquals(50.0, calculator.calculateWrongAnswerRisk(0, 0));
    }

    @Test
    public void testClassifyRiskLevel() {
        assertEquals("HIGH", calculator.classifyRiskLevel(78.0));
        assertEquals("HIGH", calculator.classifyRiskLevel(70.0));
        assertEquals("MEDIUM", calculator.classifyRiskLevel(65.0));
        assertEquals("MEDIUM", calculator.classifyRiskLevel(50.0));
        assertEquals("LOW", calculator.classifyRiskLevel(45.0));
        assertEquals("LOW", calculator.classifyRiskLevel(20.0));
    }

    @Test
    public void testCalculateOverallRiskFormula() {
        // Medium risk scenario: historical=55, recent=60, wrong=60, trend=85, inactivity=80 -> risk = 62.5
        double mediumRisk = calculator.calculateOverallRisk(55.0, 60.0, 60.0, 85.0, 80.0);
        assertEquals(62.5, mediumRisk);
        assertEquals("MEDIUM", calculator.classifyRiskLevel(mediumRisk));

        // High risk scenario: historical=75, recent=75, wrong=75, trend=85, inactivity=80 -> risk = 76.5
        double highRisk = calculator.calculateOverallRisk(75.0, 75.0, 75.0, 85.0, 80.0);
        assertEquals(76.5, highRisk);
        assertEquals("HIGH", calculator.classifyRiskLevel(highRisk));
    }
}
