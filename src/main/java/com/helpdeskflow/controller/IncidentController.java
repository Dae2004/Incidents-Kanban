package com.helpdeskflow.controller;

import com.helpdeskflow.model.Category;
import com.helpdeskflow.model.ClassOfService;
import com.helpdeskflow.model.Impact;
import com.helpdeskflow.model.Incident;
import com.helpdeskflow.model.IncidentId;
import com.helpdeskflow.model.Priority;
import com.helpdeskflow.model.Status;
import com.helpdeskflow.model.Urgency;
import com.helpdeskflow.service.IncidentService;

import java.util.List;
import java.util.Optional;

/** Adapts incident view actions to the incident application service. */
public class IncidentController {

    private final IncidentService incidentService;

    public IncidentController(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    /** Registers an incident from the form values. */
    public Incident register(String title, String description, Category category,
                             Impact impact, Urgency urgency, ClassOfService classOfService) {
        return incidentService.registerIncident(title, description, category, impact, urgency, classOfService);
    }

    /** Finds one incident by identifier. */
    public Optional<Incident> findById(IncidentId incidentId) {
        return incidentService.findById(incidentId);
    }

    /** Returns all incidents for the list view. */
    public List<Incident> findAll() {
        return incidentService.getAllIncidents();
    }

    /** Returns incidents that remain open. */
    public List<Incident> findOpen() {
        return incidentService.getOpenIncidents();
    }

    /** Returns finished incidents. */
    public List<Incident> findClosed() {
        return incidentService.getClosedIncidents();
    }

    /** Filters incidents by priority. */
    public List<Incident> findByPriority(Priority priority) {
        return incidentService.findByPriority(priority);
    }

    /** Filters incidents by status. */
    public List<Incident> findByStatus(Status status) {
        return incidentService.findByStatus(status);
    }

    /** Requests a validated workflow transition. */
    public void changeStatus(Incident incident, Status status) {
        incidentService.transitionIncident(incident, status);
    }
}
