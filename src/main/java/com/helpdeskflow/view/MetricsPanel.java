package com.helpdeskflow.view;

import com.helpdeskflow.controller.MetricsController;
import com.helpdeskflow.model.Displayable;
import com.helpdeskflow.service.MetricsSummary;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.List;
import java.util.Map;

/** Auto-refreshing dashboard with metric cards and priority/status breakdowns. Refreshes every 5 seconds. */
public class MetricsPanel extends VBox {

    private final MetricsController metricsController;
    private final Label totalValue = new Label();
    private final Label openValue = new Label();
    private final Label closedValue = new Label();
    private final Label throughputValue = new Label();
    private final Label leadTimeValue = new Label();
    private final VBox priorityBreakdown = new VBox(3);
    private final VBox statusBreakdown = new VBox(3);

    public MetricsPanel(MetricsController metricsController) {
        this.metricsController = metricsController;
        setPadding(new Insets(12));
        setSpacing(12);
        getStyleClass().add("titled-panel");

        Label sectionHeader = new Label("Métricas del Sistema");
        sectionHeader.getStyleClass().add("section-header");
        getChildren().add(sectionHeader);

        FlowPane cardsGrid = new FlowPane();
        cardsGrid.getStyleClass().add("metrics-grid");
        cardsGrid.setHgap(12);
        cardsGrid.setVgap(12);

        cardsGrid.getChildren().add(createMetricCard("Total", totalValue, "metric-total"));
        cardsGrid.getChildren().add(createMetricCard("Abiertas", openValue, "metric-open"));
        cardsGrid.getChildren().add(createMetricCard("Cerradas", closedValue, "metric-closed"));
        cardsGrid.getChildren().add(createMetricCard("Rendimiento", throughputValue, "metric-throughput"));
        cardsGrid.getChildren().add(createMetricCard("Tiempo de entrega", leadTimeValue, "metric-leadtime"));
        cardsGrid.getChildren().add(createBreakdownCard("Por prioridad", priorityBreakdown));
        cardsGrid.getChildren().add(createBreakdownCard("Por estado", statusBreakdown));

        getChildren().add(cardsGrid);

        Timeline autoRefresh = new Timeline(
                new KeyFrame(Duration.seconds(5), event -> refresh()));
        autoRefresh.setCycleCount(Timeline.INDEFINITE);
        autoRefresh.play();

        refresh();
    }

    private VBox createMetricCard(String title, Label valueLabel, String styleClass) {
        VBox card = new VBox(4);
        card.getStyleClass().addAll("metric-card", styleClass);
        card.setPadding(new Insets(12));
        card.setMinWidth(160);

        valueLabel.getStyleClass().add("metric-value");

        Label titleLabel = new Label(title.toUpperCase());
        titleLabel.getStyleClass().add("metric-label");

        card.getChildren().addAll(valueLabel, titleLabel);
        return card;
    }

    private VBox createBreakdownCard(String title, VBox content) {
        VBox card = new VBox(8);
        card.getStyleClass().add("metric-card");
        card.setPadding(new Insets(12));
        card.setMinWidth(180);
        card.setAlignment(Pos.CENTER);

        Label titleLabel = new Label(title.toUpperCase());
        titleLabel.getStyleClass().add("metric-label");

        card.getChildren().addAll(titleLabel, content);
        return card;
    }

    public void refresh() {
        MetricsSummary summary = metricsController.calculate();
        totalValue.setText(String.valueOf(summary.getTotalIncidents()));
        openValue.setText(String.valueOf(summary.getTotalOpenIncidents()));
        closedValue.setText(String.valueOf(summary.getTotalClosedIncidents()));
        throughputValue.setText(String.valueOf(summary.getThroughput()));
        leadTimeValue.setText(summary.getAverageLeadTime()
                .map(Object::toString)
                .orElse("No disponible"));
        priorityBreakdown.getChildren().setAll(buildBreakdownRows(summary.getIncidentsByPriority()));
        statusBreakdown.getChildren().setAll(buildBreakdownRows(summary.getIncidentsByStatus()));
    }

    private <T extends Displayable> List<Node> buildBreakdownRows(Map<T, Long> data) {
        return data.entrySet().stream()
                .map(e -> createBreakdownRow(e.getKey().getDisplayName(), e.getValue()))
                .toList();
    }

    private Node createBreakdownRow(String name, Long count) {
        HBox row = new HBox(8);
        row.setAlignment(Pos.CENTER_LEFT);

        Label nameLabel = new Label(name);
        nameLabel.getStyleClass().add("breakdown-name");

        Label countLabel = new Label(String.valueOf(count));
        countLabel.getStyleClass().add("breakdown-count");

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        row.getChildren().addAll(nameLabel, spacer, countLabel);
        if (count == 0) {
            row.getStyleClass().add("breakdown-zero");
        }
        return row;
    }
}
