package com.helpdeskflow.repository;

import com.helpdeskflow.model.Incident;
import com.helpdeskflow.model.IncidentId;

import java.util.List;
import java.util.Optional;

public interface IncidentRepository {

    void save(Incident incident);

    Optional<Incident> findById(IncidentId incidentId);

    List<Incident> findAll();

    void update(Incident incident);

    void delete(IncidentId incidentId);

    boolean existsById(IncidentId incidentId);
}
