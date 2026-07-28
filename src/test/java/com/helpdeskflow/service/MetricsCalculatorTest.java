package com.helpdeskflow.service;

import com.helpdeskflow.model.Category;
import com.helpdeskflow.model.Impact;
import com.helpdeskflow.model.Incident;
import com.helpdeskflow.model.Priority;
import com.helpdeskflow.model.Status;
import com.helpdeskflow.model.Urgency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MetricsCalculatorTest {

    private IncidentService incidentService;
    private MetricsCalculator calculator;
    private List<Incident> incidents;

    @BeforeEach
    void setUp() {
        incidentService = new IncidentService();
        calculator = new MetricsCalculator();

        Incident registered = incidentService.registerIncident("Registered", "Registered incident",
                Category.SOFTWARE, Impact.LOW, Urgency.LOW);
        Incident ready = incidentService.registerIncident("Ready", "Ready incident",
                Category.HARDWARE, Impact.HIGH, Urgency.MEDIUM);
        Incident inDevelopment = incidentService.registerIncident("Development", "Development incident",
                Category.NETWORK, Impact.HIGH, Urgency.HIGH);
        Incident inValidation = incidentService.registerIncident("Validation", "Validation incident",
                Category.SECURITY, Impact.HIGH, Urgency.LOW);
        Incident finished = incidentService.registerIncident("Finished", "Finished incident",
                Category.OTHER, Impact.LOW, Urgency.LOW);

        incidentService.transitionIncident(ready, Status.READY);
        incidentService.transitionIncident(inDevelopment, Status.READY);
        incidentService.transitionIncident(inDevelopment, Status.IN_DEVELOPMENT);
        incidentService.transitionIncident(inValidation, Status.READY);
        incidentService.transitionIncident(inValidation, Status.IN_DEVELOPMENT);
        incidentService.transitionIncident(inValidation, Status.IN_VALIDATION);
        incidentService.transitionIncident(finished, Status.READY);
        incidentService.transitionIncident(finished, Status.IN_DEVELOPMENT);
        incidentService.transitionIncident(finished, Status.IN_VALIDATION);
        incidentService.transitionIncident(finished, Status.FINISHED);
        finished.setCreationDate(LocalDateTime.of(2026, 1, 1, 8, 0));
        finished.setClosingDate(LocalDateTime.of(2026, 1, 3, 8, 0));

        incidents = incidentService.getAllIncidents();
    }

    @Test
    void calculatesTotalIncidents() {
        assertEquals(5, calculator.calculate(incidents).getTotalIncidents());
    }

    @Test
    void calculatesOpenIncidents() {
        assertEquals(4, calculator.calculate(incidents).getTotalOpenIncidents());
    }

    @Test
    void calculatesClosedIncidents() {
        assertEquals(1, calculator.calculate(incidents).getTotalClosedIncidents());
    }

    @Test
    void groupsIncidentsByPriority() {
        assertEquals(2, calculator.calculate(incidents).getIncidentsByPriority().get(Priority.NORMAL));
        assertEquals(2, calculator.calculate(incidents).getIncidentsByPriority().get(Priority.HIGH));
        assertEquals(1, calculator.calculate(incidents).getIncidentsByPriority().get(Priority.CRITICAL));
    }

    @Test
    void groupsIncidentsByStatus() {
        assertEquals(1, calculator.calculate(incidents).getIncidentsByStatus().get(Status.REGISTERED));
        assertEquals(1, calculator.calculate(incidents).getIncidentsByStatus().get(Status.READY));
        assertEquals(1, calculator.calculate(incidents).getIncidentsByStatus().get(Status.IN_DEVELOPMENT));
        assertEquals(1, calculator.calculate(incidents).getIncidentsByStatus().get(Status.IN_VALIDATION));
        assertEquals(1, calculator.calculate(incidents).getIncidentsByStatus().get(Status.FINISHED));
    }

    @Test
    void calculatesThroughputAsCompletedIncidents() {
        assertEquals(1, calculator.calculate(incidents).getThroughput());
    }

    @Test
    void calculatesAverageLeadTimeForCompletedIncidentsWithTimestamps() {
        assertEquals(Optional.of(Duration.ofHours(48)), calculator.calculate(incidents).getAverageLeadTime());
    }

    @Test
    void omitsLeadTimeWhenCompletedIncidentsHaveNoClosingTimestamp() {
        Incident completedWithoutClosingDate = incidents.get(4);
        completedWithoutClosingDate.setClosingDate(null);

        assertTrue(calculator.calculate(incidents).getAverageLeadTime().isEmpty());
    }

    @Test
    void calculatesValidMetricsForEmptyCollections() {
        MetricsSummary summary = calculator.calculate(List.of());

        assertEquals(0, summary.getTotalIncidents());
        assertEquals(0, summary.getTotalOpenIncidents());
        assertEquals(0, summary.getTotalClosedIncidents());
        assertEquals(0, summary.getThroughput());
        assertTrue(summary.getIncidentsByPriority().isEmpty());
        assertTrue(summary.getIncidentsByStatus().isEmpty());
        assertTrue(summary.getAverageLeadTime().isEmpty());
    }
}
