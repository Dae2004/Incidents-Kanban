package com.helpdeskflow.repository;

import com.helpdeskflow.model.Category;
import com.helpdeskflow.model.ClassOfService;
import com.helpdeskflow.model.Impact;
import com.helpdeskflow.model.Incident;
import com.helpdeskflow.model.IncidentId;
import com.helpdeskflow.model.Priority;
import com.helpdeskflow.model.Status;
import com.helpdeskflow.model.Urgency;
import com.helpdeskflow.persistence.DatabaseManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IncidentRepositoryJdbcTest {

    @TempDir
    Path temporaryDirectory;

    private IncidentRepositoryJdbc repository;

    @BeforeEach
    void setUp() {
        DatabaseManager databaseManager = new DatabaseManager(
                "jdbc:sqlite:" + temporaryDirectory.resolve("incidents.db"));
        repository = new IncidentRepositoryJdbc(databaseManager);
    }

    @Test
    void savesAndFindsIncidentById() {
        Incident incident = incident("INC-001", "Original title");

        repository.save(incident);

        assertEquals(incident, repository.findById(incident.getId()).orElseThrow());
    }

    @Test
    void returnsEmptyWhenIdDoesNotExist() {
        assertTrue(repository.findById(new IncidentId("missing")).isEmpty());
    }

    @Test
    void retrievesAllIncidents() {
        Incident first = incident("INC-001", "First");
        Incident second = incident("INC-002", "Second");
        repository.save(first);
        repository.save(second);

        assertEquals(List.of(first, second), repository.findAll());
    }

    @Test
    void updatesIncident() {
        Incident incident = incident("INC-001", "Original title");
        repository.save(incident);
        incident.setTitle("Updated title");
        incident.setStatus(Status.FINISHED);
        incident.setClosingDate(LocalDateTime.of(2026, 7, 30, 12, 0));

        repository.update(incident);

        Incident updated = repository.findById(incident.getId()).orElseThrow();
        assertEquals("Updated title", updated.getTitle());
        assertEquals(Status.FINISHED, updated.getStatus());
        assertEquals(incident.getClosingDate(), updated.getClosingDate());
    }

    @Test
    void deletesIncident() {
        Incident incident = incident("INC-001", "To delete");
        repository.save(incident);

        repository.delete(incident.getId());

        assertFalse(repository.existsById(incident.getId()));
        assertTrue(repository.findById(incident.getId()).isEmpty());
    }

    @Test
    void dataPersistsAcrossRepositoryInstances() {
        Incident incident = incident("INC-001", "Persistent incident");
        repository.save(incident);

        DatabaseManager sameDatabase = new DatabaseManager(
                "jdbc:sqlite:" + temporaryDirectory.resolve("incidents.db"));
        IncidentRepositoryJdbc secondRepository = new IncidentRepositoryJdbc(sameDatabase);

        assertEquals(incident, secondRepository.findById(incident.getId()).orElseThrow());
    }

    @Test
    void createsDatabaseSchemaAutomatically() {
        assertTrue(repository.findAll().isEmpty());
    }

    private Incident incident(String id, String title) {
        return new Incident(new IncidentId(id), title, "Description", Category.SOFTWARE,
                Impact.MEDIUM, Urgency.MEDIUM, Priority.NORMAL, Status.REGISTERED,
                LocalDateTime.of(2026, 7, 28, 10, 0), null, null, ClassOfService.STANDARD);
    }
}
