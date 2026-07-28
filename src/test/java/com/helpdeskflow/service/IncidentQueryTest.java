package com.helpdeskflow.service;

import com.helpdeskflow.model.Category;
import com.helpdeskflow.model.Impact;
import com.helpdeskflow.model.Incident;
import com.helpdeskflow.model.IncidentId;
import com.helpdeskflow.model.Priority;
import com.helpdeskflow.model.Status;
import com.helpdeskflow.model.Urgency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IncidentQueryTest {

    private IncidentService service;
    private Incident registered;
    private Incident ready;
    private Incident inDevelopment;
    private Incident inValidation;
    private Incident finished;

    @BeforeEach
    void setUp() {
        service = new IncidentService();
        registered = service.registerIncident("Registered", "Registered incident",
                Category.SOFTWARE, Impact.LOW, Urgency.LOW);
        ready = service.registerIncident("Ready", "Ready incident",
                Category.HARDWARE, Impact.HIGH, Urgency.MEDIUM);
        inDevelopment = service.registerIncident("Development", "Development incident",
                Category.NETWORK, Impact.HIGH, Urgency.HIGH);
        inValidation = service.registerIncident("Validation", "Validation incident",
                Category.SECURITY, Impact.HIGH, Urgency.LOW);
        finished = service.registerIncident("Finished", "Finished incident",
                Category.OTHER, Impact.LOW, Urgency.LOW);

        service.transitionIncident(ready, Status.READY);
        service.transitionIncident(inDevelopment, Status.READY);
        service.transitionIncident(inDevelopment, Status.IN_DEVELOPMENT);
        service.transitionIncident(inValidation, Status.READY);
        service.transitionIncident(inValidation, Status.IN_DEVELOPMENT);
        service.transitionIncident(inValidation, Status.IN_VALIDATION);
        service.transitionIncident(finished, Status.READY);
        service.transitionIncident(finished, Status.IN_DEVELOPMENT);
        service.transitionIncident(finished, Status.IN_VALIDATION);
        service.transitionIncident(finished, Status.FINISHED);
    }

    @Test
    void findsIncidentByExistingId() {
        assertEquals(registered, service.findById(registered.getId()).orElseThrow());
    }

    @Test
    void returnsEmptyForNonExistingId() {
        assertTrue(service.findById(new IncidentId("missing")).isEmpty());
    }

    @Test
    void retrievesAllIncidents() {
        assertEquals(List.of(registered, ready, inDevelopment, inValidation, finished),
                service.getAllIncidents());
    }

    @Test
    void retrievesOnlyOpenIncidents() {
        assertEquals(List.of(registered, ready, inDevelopment, inValidation),
                service.getOpenIncidents());
    }

    @Test
    void retrievesOnlyClosedIncidents() {
        assertEquals(List.of(finished), service.getClosedIncidents());
    }

    @Test
    void filtersByHighPriority() {
        assertEquals(List.of(ready, inValidation), service.findByPriority(Priority.HIGH));
    }

    @Test
    void filtersByNormalPriority() {
        assertEquals(List.of(registered, finished), service.findByPriority(Priority.NORMAL));
    }

    @Test
    void filtersByCriticalPriority() {
        assertEquals(List.of(inDevelopment), service.findByPriority(Priority.CRITICAL));
    }

    @Test
    void filtersByRegisteredStatus() {
        assertEquals(List.of(registered), service.findByStatus(Status.REGISTERED));
    }

    @Test
    void filtersByReadyStatus() {
        assertEquals(List.of(ready), service.findByStatus(Status.READY));
    }

    @Test
    void filtersByInDevelopmentStatus() {
        assertEquals(List.of(inDevelopment), service.findByStatus(Status.IN_DEVELOPMENT));
    }

    @Test
    void filtersByInValidationStatus() {
        assertEquals(List.of(inValidation), service.findByStatus(Status.IN_VALIDATION));
    }

    @Test
    void filtersByFinishedStatus() {
        assertEquals(List.of(finished), service.findByStatus(Status.FINISHED));
    }

    @Test
    void queryResultsAreImmutable() {
        assertFalse(service.getAllIncidents().isEmpty());
        org.junit.jupiter.api.Assertions.assertThrows(UnsupportedOperationException.class,
                () -> service.getAllIncidents().clear());
    }
}
