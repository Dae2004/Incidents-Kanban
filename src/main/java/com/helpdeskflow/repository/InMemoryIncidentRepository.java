package com.helpdeskflow.repository;

import com.helpdeskflow.exception.PersistenceException;
import com.helpdeskflow.model.Incident;
import com.helpdeskflow.model.IncidentId;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryIncidentRepository implements IncidentRepository {

    private final List<Incident> incidents = new ArrayList<>();

    @Override
    public void save(Incident incident) {
        incidents.add(incident);
    }

    @Override
    public Optional<Incident> findById(IncidentId incidentId) {
        return incidents.stream()
                .filter(incident -> incident.getId().equals(incidentId))
                .findFirst();
    }

    @Override
    public List<Incident> findAll() {
        return List.copyOf(incidents);
    }

    @Override
    public void update(Incident incident) {
        int incidentIndex = findIndexById(incident.getId());
        if (incidentIndex < 0) {
            throw new PersistenceException("Incident does not exist: " + incident.getId());
        }
        incidents.set(incidentIndex, incident);
    }

    @Override
    public void delete(IncidentId incidentId) {
        incidents.removeIf(incident -> incident.getId().equals(incidentId));
    }

    @Override
    public boolean existsById(IncidentId incidentId) {
        return findById(incidentId).isPresent();
    }

    private int findIndexById(IncidentId incidentId) {
        for (int index = 0; index < incidents.size(); index++) {
            if (incidents.get(index).getId().equals(incidentId)) {
                return index;
            }
        }
        return -1;
    }
}
