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
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

public class IncidentService {

    private final PriorityCalculator priorityCalculator;
    private final StateTransitionValidator stateTransitionValidator;
    private final List<Incident> registeredIncidents = new ArrayList<>();
    private static final Set<Status> OPEN_STATUSES = Set.of(
            Status.REGISTERED,
            Status.READY,
            Status.IN_DEVELOPMENT,
            Status.IN_VALIDATION
    );

    public IncidentService() {
        this(new PriorityCalculator(), new StateTransitionValidator());
    }

    public IncidentService(PriorityCalculator priorityCalculator) {
        this(priorityCalculator, new StateTransitionValidator());
    }

    public IncidentService(PriorityCalculator priorityCalculator,
                           StateTransitionValidator stateTransitionValidator) {
        this.priorityCalculator = priorityCalculator;
        this.stateTransitionValidator = stateTransitionValidator;
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

    public void transitionIncident(Incident incident, Status targetStatus) {
        if (incident == null) {
            throw new IllegalArgumentException("Incident cannot be null");
        }
        stateTransitionValidator.validateTransition(incident.getStatus(), targetStatus);
        incident.setStatus(targetStatus);
    }

    public List<Incident> getRegisteredIncidents() {
        return getAllIncidents();
    }

    public Optional<Incident> findById(IncidentId incidentId) {
        return registeredIncidents.stream()
                .filter(incident -> Objects.equals(incident.getId(), incidentId))
                .findFirst();
    }

    public List<Incident> getAllIncidents() {
        return List.copyOf(registeredIncidents);
    }

    public List<Incident> getOpenIncidents() {
        return filter(incident -> OPEN_STATUSES.contains(incident.getStatus()));
    }

    public List<Incident> getClosedIncidents() {
        return findByStatus(Status.FINISHED);
    }

    public List<Incident> findByPriority(Priority priority) {
        return filter(incident -> incident.getPriority() == priority);
    }

    public List<Incident> findByStatus(Status status) {
        return filter(incident -> incident.getStatus() == status);
    }

    private List<Incident> filter(Predicate<Incident> condition) {
        return registeredIncidents.stream()
                .filter(condition)
                .toList();
    }
}
