package com.helpdeskflow.service;

import com.helpdeskflow.model.Category;
import com.helpdeskflow.model.ClassOfService;
import com.helpdeskflow.model.Impact;
import com.helpdeskflow.model.Incident;
import com.helpdeskflow.model.IncidentId;
import com.helpdeskflow.model.Priority;
import com.helpdeskflow.model.Status;
import com.helpdeskflow.model.Urgency;
import com.helpdeskflow.validator.IncidentInputValidator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class IncidentService {

    private final PriorityCalculator priorityCalculator;
    private final List<Incident> registeredIncidents = new ArrayList<>();

    public IncidentService() {
        this(new PriorityCalculator());
    }

    public IncidentService(PriorityCalculator priorityCalculator) {
        this.priorityCalculator = priorityCalculator;
    }

    public Incident registerIncident(String title, String description, Category category,
                                     Impact impact, Urgency urgency) {
        return registerIncident(title, description, category, impact, urgency, ClassOfService.STANDARD);
    }

    public Incident registerIncident(String title, String description, Category category,
                                     Impact impact, Urgency urgency, ClassOfService classOfService) {
        IncidentInputValidator.validate(title, description, category, impact, urgency);

        Priority priority = priorityCalculator.calculate(impact, urgency);
        Incident incident = new Incident(
                new IncidentId(UUID.randomUUID().toString()),
                title,
                description,
                category,
                impact,
                urgency,
                priority,
                Status.REGISTERED,
                LocalDateTime.now(),
                null,
                null,
                classOfService
        );
        registeredIncidents.add(incident);
        return incident;
    }

    public List<Incident> getRegisteredIncidents() {
        return List.copyOf(registeredIncidents);
    }
}
