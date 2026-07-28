package com.helpdeskflow.service;

import com.helpdeskflow.model.Category;
import com.helpdeskflow.model.ClassOfService;
import com.helpdeskflow.model.Impact;
import com.helpdeskflow.model.Incident;
import com.helpdeskflow.model.Priority;
import com.helpdeskflow.model.Urgency;
import com.helpdeskflow.persistence.DatabaseManager;
import com.helpdeskflow.repository.IncidentRepositoryJdbc;
import com.helpdeskflow.view.IncidentDetailPanel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.swing.JLabel;
import java.nio.file.Path;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExpediteServiceTest {

    @Test
    void standardIncidentKeepsStandardClassOfService() {
        IncidentService incidentService = new IncidentService();

        Incident incident = incidentService.registerIncident("Standard", "Description",
                Category.SOFTWARE, Impact.LOW, Urgency.LOW, ClassOfService.STANDARD);

        assertEquals(ClassOfService.STANDARD, incident.getClassOfService());
    }

    @Test
    void registersExpediteIncidentWithAllDataPreserved() {
        IncidentService incidentService = new IncidentService();
        ExpediteService expediteService = new ExpediteService(incidentService);

        Incident incident = expediteService.registerExpedited("Urgent outage", "Critical service outage",
                Category.NETWORK, Impact.HIGH, Urgency.HIGH);

        assertEquals(ClassOfService.EXPEDITE, incident.getClassOfService());
        assertEquals("Urgent outage", incident.getTitle());
        assertEquals("Critical service outage", incident.getDescription());
        assertEquals(Category.NETWORK, incident.getCategory());
        assertEquals(Impact.HIGH, incident.getImpact());
        assertEquals(Urgency.HIGH, incident.getUrgency());
        assertEquals(Priority.CRITICAL, incident.getPriority());
        assertTrue(expediteService.isExpedited(incident));
    }

    @Test
    void retrievesOnlyExpediteIncidents() {
        IncidentService incidentService = new IncidentService();
        ExpediteService expediteService = new ExpediteService(incidentService);
        Incident expedited = expediteService.registerExpedited("Expedite", "Description",
                Category.SECURITY, Impact.MEDIUM, Urgency.HIGH);
        incidentService.registerIncident("Standard", "Description", Category.SOFTWARE,
                Impact.LOW, Urgency.LOW, ClassOfService.STANDARD);

        assertEquals(java.util.List.of(expedited), expediteService.findExpedited());
        assertFalse(expediteService.findExpedited().stream()
                .anyMatch(incident -> incident.getClassOfService() == ClassOfService.STANDARD));
    }

    @Test
    void persistsExpediteClassOfService(@TempDir Path temporaryDirectory) {
        DatabaseManager databaseManager = new DatabaseManager(
                "jdbc:sqlite:" + temporaryDirectory.resolve("incidents.db"));
        IncidentRepositoryJdbc repository = new IncidentRepositoryJdbc(databaseManager);
        IncidentService incidentService = new IncidentService(repository);
        ExpediteService expediteService = new ExpediteService(incidentService);

        Incident incident = expediteService.registerExpedited("Persistent expedite", "Description",
                Category.HARDWARE, Impact.HIGH, Urgency.MEDIUM);

        Incident stored = repository.findById(incident.getId()).orElseThrow();
        assertEquals(ClassOfService.EXPEDITE, stored.getClassOfService());
    }

    @Test
    void detailPanelDisplaysClassOfService(@TempDir Path temporaryDirectory) {
        Incident incident = new IncidentService().registerIncident("Expedite", "Description",
                Category.SOFTWARE, Impact.LOW, Urgency.LOW, ClassOfService.EXPEDITE);
        IncidentDetailPanel panel = new IncidentDetailPanel();

        panel.display(incident);

        assertTrue(Arrays.stream(panel.getComponents())
                .filter(JLabel.class::isInstance)
                .map(component -> ((JLabel) component).getText())
                .anyMatch(text -> text.contains("EXPEDITE")));
    }
}
