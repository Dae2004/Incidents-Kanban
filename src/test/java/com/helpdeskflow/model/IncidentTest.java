package com.helpdeskflow.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IncidentTest {

    @Test
    void constructorInitializesAllFields() {
        IncidentId id = new IncidentId("INC-001");
        LocalDateTime creationDate = LocalDateTime.of(2026, 7, 28, 10, 30);
        LocalDateTime closingDate = LocalDateTime.of(2026, 7, 29, 15, 45);
        Incident incident = new Incident(id, "Network outage", "Office network is unavailable",
                Category.NETWORK, Impact.HIGH, Urgency.HIGH, Priority.CRITICAL, Status.FINISHED,
                creationDate, closingDate, "Router replaced", ClassOfService.STANDARD);

        assertEquals(id, incident.getId());
        assertEquals("Network outage", incident.getTitle());
        assertEquals("Office network is unavailable", incident.getDescription());
        assertEquals(Category.NETWORK, incident.getCategory());
        assertEquals(Impact.HIGH, incident.getImpact());
        assertEquals(Urgency.HIGH, incident.getUrgency());
        assertEquals(Priority.CRITICAL, incident.getPriority());
        assertEquals(Status.FINISHED, incident.getStatus());
        assertEquals(creationDate, incident.getCreationDate());
        assertEquals(closingDate, incident.getClosingDate());
        assertEquals("Router replaced", incident.getSolutionDescription());
        assertEquals(ClassOfService.STANDARD, incident.getClassOfService());
    }

    @Test
    void incidentsWithSameIdentifierAreEqual() {
        Incident first = new Incident(new IncidentId("INC-001"), "Title", "Description",
                Category.SOFTWARE, Impact.LOW, Urgency.LOW, Priority.NORMAL, Status.REGISTERED,
                LocalDateTime.MIN, null, null, ClassOfService.STANDARD);
        Incident second = new Incident(new IncidentId("INC-001"), "Other title", "Other description",
                Category.HARDWARE, Impact.HIGH, Urgency.HIGH, Priority.CRITICAL, Status.FINISHED,
                LocalDateTime.MAX, null, null, ClassOfService.EXPEDITE);

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
    }
}
