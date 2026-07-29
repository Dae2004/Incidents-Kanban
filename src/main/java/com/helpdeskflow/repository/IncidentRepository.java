package com.helpdeskflow.repository;

import com.helpdeskflow.model.Incident;
import com.helpdeskflow.model.IncidentId;

import java.util.List;
import java.util.Optional;

/** Persistence boundary for incident records. */
public interface IncidentRepository {

    /** Stores a new incident. */
    void save(Incident incident);

    /** Finds an incident by identifier. */
    Optional<Incident> findById(IncidentId incidentId);

    /** Returns all stored incidents. */
    List<Incident> findAll();

    /** Updates an existing incident. */
    void update(Incident incident);

    /** Deletes an incident by identifier. */
    void delete(IncidentId incidentId);

    /** Checks whether an identifier is stored. */
    boolean existsById(IncidentId incidentId);
}
