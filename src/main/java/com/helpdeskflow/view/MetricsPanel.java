package com.helpdeskflow.view;

import com.helpdeskflow.controller.MetricsController;
import com.helpdeskflow.service.MetricsSummary;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.GridLayout;

public class MetricsPanel extends JPanel {

    private final MetricsController metricsController;
    private final JLabel totalLabel = new JLabel();
    private final JLabel openLabel = new JLabel();
    private final JLabel closedLabel = new JLabel();
    private final JLabel throughputLabel = new JLabel();
    private final JLabel priorityLabel = new JLabel();
    private final JLabel statusLabel = new JLabel();
    private final JLabel leadTimeLabel = new JLabel();

    public MetricsPanel(MetricsController metricsController) {
        this.metricsController = metricsController;
        setBorder(BorderFactory.createTitledBorder("Métricas del sistema"));
        setLayout(new GridLayout(8, 1, 6, 6));
        add(totalLabel);
        add(openLabel);
        add(closedLabel);
        add(throughputLabel);
        add(priorityLabel);
        add(statusLabel);
        add(leadTimeLabel);
        JButton refreshButton = new JButton("Actualizar métricas");
        refreshButton.addActionListener(event -> refresh());
        add(refreshButton);
        refresh();
    }

    public void refresh() {
        MetricsSummary summary = metricsController.calculate();
        totalLabel.setText("Total: " + summary.getTotalIncidents());
        openLabel.setText("Abiertas: " + summary.getTotalOpenIncidents());
        closedLabel.setText("Cerradas: " + summary.getTotalClosedIncidents());
        throughputLabel.setText("Throughput: " + summary.getThroughput());
        priorityLabel.setText("Por prioridad: " + summary.getIncidentsByPriority());
        statusLabel.setText("Por estado: " + summary.getIncidentsByStatus());
        leadTimeLabel.setText("Lead time promedio: "
                + summary.getAverageLeadTime().map(Object::toString).orElse("No disponible"));
    }
}
