package com.helpdeskflow.service;

import com.helpdeskflow.model.Category;
import com.helpdeskflow.model.ClassOfService;
import com.helpdeskflow.model.Impact;
import com.helpdeskflow.model.Incident;
import com.helpdeskflow.model.Priority;
import com.helpdeskflow.model.Status;
import com.helpdeskflow.model.Urgency;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IncidentServiceTest {

    private final IncidentService service = new IncidentService();

    @Test
    void registersIncidentWithCalculatedAndInitialValues() {
        LocalDateTime beforeRegistration = LocalDateTime.now();

        Incident incident = service.registerIncident("Network outage", "Office network is unavailable",
                Category.NETWORK, Impact.HIGH, Urgency.HIGH, ClassOfService.STANDARD);

        LocalDateTime afterRegistration = LocalDateTime.now();
        assertNotNull(incident.getId());
        assertEquals("Network outage", incident.getTitle());
        assertEquals("Office network is unavailable", incident.getDescription());
        assertEquals(Category.NETWORK, incident.getCategory());
        assertEquals(Impact.HIGH, incident.getImpact());
        assertEquals(Urgency.HIGH, incident.getUrgency());
        assertEquals(Priority.CRITICAL, incident.getPriority());
        assertEquals(Status.REGISTERED, incident.getStatus());
        assertEquals(ClassOfService.STANDARD, incident.getClassOfService());
        assertNotNull(incident.getCreationDate());
        assertFalse(incident.getCreationDate().isBefore(beforeRegistration));
        assertFalse(incident.getCreationDate().isAfter(afterRegistration));
        assertTrue(service.getRegisteredIncidents().contains(incident));
    }

    @Test
    void defaultsClassOfServiceToStandardWhenNotSupplied() {
        Incident incident = service.registerIncident("Printer issue", "Printer is offline",
                Category.HARDWARE, Impact.LOW, Urgency.MEDIUM);

        assertEquals(ClassOfService.STANDARD, incident.getClassOfService());
    }

    @Test
    void rejectsNullTitle() {
        assertThrows(IllegalArgumentException.class, () -> service.registerIncident(null, "Description",
                Category.SOFTWARE, Impact.LOW, Urgency.LOW));
    }

    @Test
    void rejectsBlankTitle() {
        assertThrows(IllegalArgumentException.class, () -> service.registerIncident("   ", "Description",
                Category.SOFTWARE, Impact.LOW, Urgency.LOW));
    }

    @Test
    void rejectsNullDescription() {
        assertThrows(IllegalArgumentException.class, () -> service.registerIncident("Title", null,
                Category.SOFTWARE, Impact.LOW, Urgency.LOW));
    }

    @Test
    void rejectsBlankDescription() {
        assertThrows(IllegalArgumentException.class, () -> service.registerIncident("Title", "  ",
                Category.SOFTWARE, Impact.LOW, Urgency.LOW));
    }

    @Test
    void rejectsNullCategory() {
        assertThrows(IllegalArgumentException.class, () -> service.registerIncident("Title", "Description",
                null, Impact.LOW, Urgency.LOW));
    }

    @Test
    void rejectsNullImpact() {
        assertThrows(IllegalArgumentException.class, () -> service.registerIncident("Title", "Description",
                Category.SOFTWARE, null, Urgency.LOW));
    }

    @Test
    void rejectsNullUrgency() {
        assertThrows(IllegalArgumentException.class, () -> service.registerIncident("Title", "Description",
                Category.SOFTWARE, Impact.LOW, null));
    }
}
