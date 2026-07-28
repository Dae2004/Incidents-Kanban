package com.helpdeskflow.service;

import com.helpdeskflow.model.Impact;
import com.helpdeskflow.model.Priority;
import com.helpdeskflow.model.Urgency;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PriorityCalculatorTest {

    private final PriorityCalculator calculator = new PriorityCalculator();

    @Test
    void highImpactAndHighUrgencyAreCritical() {
        assertEquals(Priority.CRITICAL, calculator.calculate(Impact.HIGH, Urgency.HIGH));
    }

    @Test
    void highImpactAndMediumUrgencyAreHigh() {
        assertEquals(Priority.HIGH, calculator.calculate(Impact.HIGH, Urgency.MEDIUM));
    }

    @Test
    void highImpactAndLowUrgencyAreHigh() {
        assertEquals(Priority.HIGH, calculator.calculate(Impact.HIGH, Urgency.LOW));
    }

    @Test
    void mediumImpactAndHighUrgencyAreHigh() {
        assertEquals(Priority.HIGH, calculator.calculate(Impact.MEDIUM, Urgency.HIGH));
    }

    @Test
    void lowImpactAndHighUrgencyAreHigh() {
        assertEquals(Priority.HIGH, calculator.calculate(Impact.LOW, Urgency.HIGH));
    }

    @Test
    void mediumImpactAndMediumUrgencyAreNormal() {
        assertEquals(Priority.NORMAL, calculator.calculate(Impact.MEDIUM, Urgency.MEDIUM));
    }

    @Test
    void mediumImpactAndLowUrgencyAreNormal() {
        assertEquals(Priority.NORMAL, calculator.calculate(Impact.MEDIUM, Urgency.LOW));
    }

    @Test
    void lowImpactAndMediumUrgencyAreNormal() {
        assertEquals(Priority.NORMAL, calculator.calculate(Impact.LOW, Urgency.MEDIUM));
    }

    @Test
    void lowImpactAndLowUrgencyAreNormal() {
        assertEquals(Priority.NORMAL, calculator.calculate(Impact.LOW, Urgency.LOW));
    }
}
