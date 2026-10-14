package com.personalizedlearning.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PerformanceCalculatorTest {

    private PerformanceCalculator calculator;

    @BeforeEach
    public void setUp() {
        calculator = new PerformanceCalculator(60.0, 75.0);
    }

    @Test
    public void testCalculatePercentage() {
        assertEquals(70.0, calculator.calculatePercentage(7, 10));
        assertEquals(40.0, calculator.calculatePercentage(4, 10));
        assertEquals(100.0, calculator.calculatePercentage(5, 5));
        assertEquals(0.0, calculator.calculatePercentage(0, 10));
        assertEquals(0.0, calculator.calculatePercentage(5, 0));
    }

    @Test
    public void testClassifyStatus() {
        assertEquals("WEAK", calculator.classifyStatus(45.0));
        assertEquals("WEAK", calculator.classifyStatus(59.9));
        assertEquals("AVERAGE", calculator.classifyStatus(60.0));
        assertEquals("AVERAGE", calculator.classifyStatus(70.0));
        assertEquals("AVERAGE", calculator.classifyStatus(75.0));
        assertEquals("STRONG", calculator.classifyStatus(75.1));
        assertEquals("STRONG", calculator.classifyStatus(85.0));
    }

    @Test
    public void testIsWeak() {
        assertTrue(calculator.isWeak(52.0));
        assertTrue(calculator.isWeak(40.0));
        assertFalse(calculator.isWeak(60.0));
        assertFalse(calculator.isWeak(80.0));
    }

    @Test
    public void testCalculateTrend() {
        assertEquals("IMPROVING", calculator.calculateTrend(50.0, 60.0));
        assertEquals("DECLINING", calculator.calculateTrend(70.0, 60.0));
        assertEquals("STABLE", calculator.calculateTrend(65.0, 66.0));
    }
}
