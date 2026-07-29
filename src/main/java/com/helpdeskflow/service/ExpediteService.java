package com.helpdeskflow.service;

import com.helpdeskflow.model.Category;
import com.helpdeskflow.model.ClassOfService;
import com.helpdeskflow.model.Impact;
import com.helpdeskflow.model.Incident;
import com.helpdeskflow.model.Urgency;

import java.util.List;

/** Provides operations for the EXPEDITE class of service. */
public class ExpediteService {

    private final IncidentService incidentService;

    public ExpediteService(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    /**
     * Registers an incident explicitly marked as EXPEDITE.
     *
     * @return the newly registered expedited incident
     */
    public Incident registerExpedited(String title, String description, Category category,
                                      Impact impact, Urgency urgency) {
        return incidentService.registerIncident(title, description, category, impact, urgency,
                ClassOfService.EXPEDITE);
    }

    /**
     * Finds all incidents marked as EXPEDITE.
     *
     * @return expedited incidents
     */
    public List<Incident> findExpedited() {
        return incidentService.getAllIncidents().stream()
                .filter(this::isExpedited)
                .toList();
    }

    /**
     * Checks an incident's class of service.
     *
     * @return {@code true} when the incident is expedited
     */
    public boolean isExpedited(Incident incident) {
        return incident.getClassOfService() == ClassOfService.EXPEDITE;
    }
}
