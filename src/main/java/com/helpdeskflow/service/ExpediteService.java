package com.helpdeskflow.service;

import com.helpdeskflow.model.Category;
import com.helpdeskflow.model.ClassOfService;
import com.helpdeskflow.model.Impact;
import com.helpdeskflow.model.Incident;
import com.helpdeskflow.model.Urgency;

import java.util.List;

public class ExpediteService {

    private final IncidentService incidentService;

    public ExpediteService(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    public Incident registerExpedited(String title, String description, Category category,
                                      Impact impact, Urgency urgency) {
        return incidentService.registerIncident(title, description, category, impact, urgency,
                ClassOfService.EXPEDITE);
    }

    public List<Incident> findExpedited() {
        return incidentService.getAllIncidents().stream()
                .filter(this::isExpedited)
                .toList();
    }

    public boolean isExpedited(Incident incident) {
        return incident.getClassOfService() == ClassOfService.EXPEDITE;
    }
}
