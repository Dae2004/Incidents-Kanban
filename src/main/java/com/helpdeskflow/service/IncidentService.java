package com.helpdeskflow.service;

import com.helpdeskflow.model.Category;
import com.helpdeskflow.model.ClassOfService;
import com.helpdeskflow.model.Impact;
import com.helpdeskflow.model.Incident;
import com.helpdeskflow.model.IncidentId;
import com.helpdeskflow.model.Priority;
import com.helpdeskflow.model.Status;
import com.helpdeskflow.model.Urgency;
import com.helpdeskflow.repository.IncidentRepository;
import com.helpdeskflow.repository.InMemoryIncidentRepository;
import com.helpdeskflow.validator.IncidentInputValidator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

/**
 * Coordinates incident registration, workflow transitions, and repository queries.
 */
public class IncidentService {

    private final PriorityCalculator priorityCalculator;
    private final StateTransitionValidator stateTransitionValidator;
    private final IncidentRepository incidentRepository;
    private static final Set<Status> OPEN_STATUSES = Set.of(
            Status.REGISTERED,
            Status.READY,
            Status.IN_DEVELOPMENT,
            Status.IN_VALIDATION
    );

    public IncidentService() {
        this(new PriorityCalculator(), new StateTransitionValidator(), new InMemoryIncidentRepository());
    }

    public IncidentService(PriorityCalculator priorityCalculator) {
        this(priorityCalculator, new StateTransitionValidator(), new InMemoryIncidentRepository());
    }

    public IncidentService(IncidentRepository incidentRepository) {
        this(new PriorityCalculator(), new StateTransitionValidator(), incidentRepository);
    }

    public IncidentService(PriorityCalculator priorityCalculator,
                           StateTransitionValidator stateTransitionValidator) {
        this(priorityCalculator, stateTransitionValidator, new InMemoryIncidentRepository());
    }

    public IncidentService(PriorityCalculator priorityCalculator,
                           StateTransitionValidator stateTransitionValidator,
                           IncidentRepository incidentRepository) {
        this.priorityCalculator = priorityCalculator;
        this.stateTransitionValidator = stateTransitionValidator;
        this.incidentRepository = Objects.requireNonNull(incidentRepository);
    }

    /**
     * Registers a standard incident and calculates its initial priority.
     *
     * @return the newly registered incident
     * @throws IllegalArgumentException when a required field is invalid
     */
    public Incident registerIncident(String title, String description, Category category,
                                     Impact impact, Urgency urgency) {
        return registerIncident(title, description, category, impact, urgency, ClassOfService.STANDARD);
    }

    /**
     * Registers an incident with an explicit class of service.
     *
     * @return the newly registered incident
     * @throws IllegalArgumentException when a required field is invalid
     */
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
        incidentRepository.save(incident);
        return incident;
    }

    /**
     * Moves an incident to its next valid workflow state and persists the change.
     *
     * @throws IllegalArgumentException when the incident is null
     * @throws com.helpdeskflow.exception.InvalidStateTransitionException when the transition is invalid
     */
    public void transitionIncident(Incident incident, Status targetStatus) {
        if (incident == null) {
            throw new IllegalArgumentException("Incident cannot be null");
        }
        stateTransitionValidator.validateTransition(incident.getStatus(), targetStatus);
        incident.setStatus(targetStatus);
        incidentRepository.update(incident);
    }

    /**
     * Returns all incidents currently managed by the service.
     *
     * @return all incidents
     */
    public List<Incident> getRegisteredIncidents() {
        return getAllIncidents();
    }

    /**
     * Looks up an incident by its identifier.
     *
     * @return the matching incident, if present
     */
    public Optional<Incident> findById(IncidentId incidentId) {
        return incidentRepository.findById(incidentId);
    }

    /**
     * Returns a snapshot of all incidents.
     *
     * @return all incidents
     */
    public List<Incident> getAllIncidents() {
        return incidentRepository.findAll();
    }

    /**
     * Returns incidents that have not reached the finished state.
     *
     * @return open incidents
     */
    public List<Incident> getOpenIncidents() {
        return filter(incident -> OPEN_STATUSES.contains(incident.getStatus()));
    }

    /**
     * Returns incidents in the finished state.
     *
     * @return closed incidents
     */
    public List<Incident> getClosedIncidents() {
        return findByStatus(Status.FINISHED);
    }

    /**
     * Filters incidents by calculated priority.
     *
     * @return incidents with the requested priority
     */
    public List<Incident> findByPriority(Priority priority) {
        return filter(incident -> incident.getPriority() == priority);
    }

    /**
     * Filters incidents by workflow status.
     *
     * @return incidents with the requested status
     */
    public List<Incident> findByStatus(Status status) {
        return filter(incident -> incident.getStatus() == status);
    }

    private List<Incident> filter(Predicate<Incident> condition) {
        return getAllIncidents().stream()
                .filter(condition)
                .toList();
    }
}
