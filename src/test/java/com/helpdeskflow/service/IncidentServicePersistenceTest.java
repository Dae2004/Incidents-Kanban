package com.helpdeskflow.service;

import com.helpdeskflow.model.Category;
import com.helpdeskflow.model.Impact;
import com.helpdeskflow.model.Incident;
import com.helpdeskflow.model.Urgency;
import com.helpdeskflow.persistence.DatabaseManager;
import com.helpdeskflow.repository.IncidentRepositoryJdbc;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IncidentServicePersistenceTest {

    @Test
    void persistsRegisteredIncidentThroughInjectedRepository(@TempDir Path temporaryDirectory) {
        DatabaseManager databaseManager = new DatabaseManager(
                "jdbc:sqlite:" + temporaryDirectory.resolve("incidents.db"));
        IncidentRepositoryJdbc repository = new IncidentRepositoryJdbc(databaseManager);
        IncidentService service = new IncidentService(repository);

        Incident incident = service.registerIncident("Persistent incident", "Description",
                Category.SOFTWARE, Impact.HIGH, Urgency.HIGH);

        assertEquals(incident, repository.findById(incident.getId()).orElseThrow());
    }
}
