package com.helpdeskflow.controller;

import com.helpdeskflow.model.Incident;
import com.helpdeskflow.service.IncidentService;
import com.helpdeskflow.service.MetricsCalculator;
import com.helpdeskflow.service.MetricsSummary;

/** Adapts metrics calculations to the metrics view. */
public class MetricsController {

    private final IncidentService incidentService;
    private final MetricsCalculator metricsCalculator;

    public MetricsController(IncidentService incidentService, MetricsCalculator metricsCalculator) {
        this.incidentService = incidentService;
        this.metricsCalculator = metricsCalculator;
    }

    /** Calculates metrics for the incidents currently held by the service. */
    public MetricsSummary calculate() {
        return metricsCalculator.calculate(incidentService.getAllIncidents());
    }
}
