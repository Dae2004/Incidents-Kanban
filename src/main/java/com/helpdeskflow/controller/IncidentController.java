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

public class IncidentController {

    private final IncidentService incidentService;

    public IncidentController(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    public Incident register(String title, String description, Category category,
                             Impact impact, Urgency urgency, ClassOfService classOfService) {
        return incidentService.registerIncident(title, description, category, impact, urgency, classOfService);
    }

    public Optional<Incident> findById(IncidentId incidentId) {
        return incidentService.findById(incidentId);
    }

    public List<Incident> findAll() {
        return incidentService.getAllIncidents();
    }

    public List<Incident> findOpen() {
        return incidentService.getOpenIncidents();
    }

    public List<Incident> findClosed() {
        return incidentService.getClosedIncidents();
    }

    public List<Incident> findByPriority(Priority priority) {
        return incidentService.findByPriority(priority);
    }

    public List<Incident> findByStatus(Status status) {
        return incidentService.findByStatus(status);
    }

    public void changeStatus(Incident incident, Status status) {
        incidentService.transitionIncident(incident, status);
    }
}
