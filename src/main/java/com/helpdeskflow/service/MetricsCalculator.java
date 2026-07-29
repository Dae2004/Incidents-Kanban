package com.helpdeskflow.service;

import com.helpdeskflow.model.Incident;
import com.helpdeskflow.model.Priority;
import com.helpdeskflow.model.Status;

import java.time.Duration;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/** Calculates aggregate operational metrics for a collection of incidents. */
public class MetricsCalculator {

    private static final Set<Status> OPEN_STATUSES = Set.of(
            Status.REGISTERED,
            Status.READY,
            Status.IN_DEVELOPMENT,
            Status.IN_VALIDATION
    );

    /**
     * Calculates counts grouped by status and priority, plus average lead time.
     *
     * @param incidents incidents to include
     * @return calculated metrics
     * @throws NullPointerException when {@code incidents} is null
     */
    public MetricsSummary calculate(List<Incident> incidents) {
        Objects.requireNonNull(incidents, "Incidents cannot be null");

        long closedIncidents = incidents.stream()
                .filter(incident -> incident.getStatus() == Status.FINISHED)
                .count();
        long openIncidents = incidents.stream()
                .filter(incident -> OPEN_STATUSES.contains(incident.getStatus()))
                .count();

        Map<Priority, Long> incidentsByPriority = groupBy(incidents, Incident::getPriority,
                Priority.class);
        Map<Status, Long> incidentsByStatus = groupBy(incidents, Incident::getStatus,
                Status.class);

        return new MetricsSummary(
                incidents.size(),
                openIncidents,
                closedIncidents,
                incidentsByPriority,
                incidentsByStatus,
                closedIncidents,
                calculateAverageLeadTime(incidents)
        );
    }

    private <T extends Enum<T>> Map<T, Long> groupBy(List<Incident> incidents,
                                                       Function<Incident, T> classifier,
                                                       Class<T> enumType) {
        return incidents.stream()
                .collect(Collectors.groupingBy(
                        classifier,
                        () -> new EnumMap<>(enumType),
                        Collectors.counting()));
    }

    /**
     * Calculates lead time only for finished incidents with both timestamps available.
     * The current registration and transition flows do not assign a closing timestamp,
     * so incidents without one are intentionally excluded rather than given an invented value.
     */
    private Optional<Duration> calculateAverageLeadTime(List<Incident> incidents) {
        List<Duration> leadTimes = incidents.stream()
                .filter(incident -> incident.getStatus() == Status.FINISHED)
                .filter(incident -> incident.getCreationDate() != null
                        && incident.getClosingDate() != null)
                .map(incident -> Duration.between(incident.getCreationDate(), incident.getClosingDate()))
                .toList();

        if (leadTimes.isEmpty()) {
            return Optional.empty();
        }

        Duration totalLeadTime = leadTimes.stream().reduce(Duration.ZERO, Duration::plus);
        return Optional.of(totalLeadTime.dividedBy(leadTimes.size()));
    }
}
