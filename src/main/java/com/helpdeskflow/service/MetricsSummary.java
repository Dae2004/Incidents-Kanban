package com.helpdeskflow.service;

import com.helpdeskflow.model.Priority;
import com.helpdeskflow.model.Status;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;

public final class MetricsSummary {

    private final long totalIncidents;
    private final long totalOpenIncidents;
    private final long totalClosedIncidents;
    private final Map<Priority, Long> incidentsByPriority;
    private final Map<Status, Long> incidentsByStatus;
    private final long throughput;
    private final Optional<Duration> averageLeadTime;

    public MetricsSummary(long totalIncidents, long totalOpenIncidents, long totalClosedIncidents,
                          Map<Priority, Long> incidentsByPriority,
                          Map<Status, Long> incidentsByStatus, long throughput,
                          Optional<Duration> averageLeadTime) {
        this.totalIncidents = totalIncidents;
        this.totalOpenIncidents = totalOpenIncidents;
        this.totalClosedIncidents = totalClosedIncidents;
        this.incidentsByPriority = Map.copyOf(incidentsByPriority);
        this.incidentsByStatus = Map.copyOf(incidentsByStatus);
        this.throughput = throughput;
        this.averageLeadTime = averageLeadTime;
    }

    public long getTotalIncidents() {
        return totalIncidents;
    }

    public long getTotalOpenIncidents() {
        return totalOpenIncidents;
    }

    public long getTotalClosedIncidents() {
        return totalClosedIncidents;
    }

    public Map<Priority, Long> getIncidentsByPriority() {
        return incidentsByPriority;
    }

    public Map<Status, Long> getIncidentsByStatus() {
        return incidentsByStatus;
    }

    public long getThroughput() {
        return throughput;
    }

    public Optional<Duration> getAverageLeadTime() {
        return averageLeadTime;
    }
}
