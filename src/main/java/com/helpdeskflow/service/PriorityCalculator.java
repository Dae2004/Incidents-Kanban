package com.helpdeskflow.service;

import com.helpdeskflow.model.Impact;
import com.helpdeskflow.model.Priority;
import com.helpdeskflow.model.Urgency;

import java.util.Map;

public class PriorityCalculator {

    private static final Map<Impact, Map<Urgency, Priority>> PRIORITY_MATRIX = Map.of(
            Impact.HIGH, Map.of(
                    Urgency.HIGH, Priority.CRITICAL,
                    Urgency.MEDIUM, Priority.HIGH,
                    Urgency.LOW, Priority.HIGH
            ),
            Impact.MEDIUM, Map.of(Urgency.HIGH, Priority.HIGH),
            Impact.LOW, Map.of(Urgency.HIGH, Priority.HIGH)
    );

    public Priority calculate(Impact impact, Urgency urgency) {
        return PRIORITY_MATRIX
                .getOrDefault(impact, Map.of())
                .getOrDefault(urgency, Priority.NORMAL);
    }
}
