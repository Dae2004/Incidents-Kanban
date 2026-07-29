package com.helpdeskflow.view;

import com.helpdeskflow.model.ClassOfService;
import com.helpdeskflow.model.Incident;
import com.helpdeskflow.model.Priority;
import com.helpdeskflow.model.Status;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/** Read-only detail view for a selected incident. Shows all fields including description, with styled badges. */
public class IncidentDetailPanel extends VBox {

    private final Label idLabel = new Label("-");
    private final Label titleLabel = new Label("-");
    private final Label descriptionLabel = new Label("-");
    private final Label statusLabel = new Label("-");
    private final Label priorityLabel = new Label("-");
    private final Label classOfServiceLabel = new Label("-");
    private final Label datesLabel = new Label("-");

    private final Label placeholderLabel = new Label("Seleccione una incidencia para ver sus detalles");
    private final VBox contentBox = new VBox(8);

    public IncidentDetailPanel() {
        setPadding(new Insets(8));
        setSpacing(8);
        getStyleClass().add("titled-panel");

        Label sectionHeader = new Label("Detalle de Incidencia");
        sectionHeader.getStyleClass().add("section-header");
        getChildren().add(sectionHeader);

        placeholderLabel.getStyleClass().add("placeholder-text");
        placeholderLabel.setMaxWidth(Double.MAX_VALUE);
        placeholderLabel.setAlignment(Pos.CENTER);
        placeholderLabel.setPadding(new Insets(40, 10, 40, 10));
        VBox.setVgrow(placeholderLabel, javafx.scene.layout.Priority.ALWAYS);

        buildContent();
        contentBox.setVisible(false);
        placeholderLabel.setVisible(true);

        getChildren().addAll(contentBox, placeholderLabel);
    }

    private void buildContent() {
        titleLabel.setWrapText(true);
        titleLabel.getStyleClass().addAll("detail-value", "detail-title");

        descriptionLabel.setWrapText(true);
        descriptionLabel.getStyleClass().addAll("detail-value", "detail-description");

        GridPane grid = new GridPane();
        grid.setVgap(6);
        grid.setHgap(12);
        grid.setAlignment(Pos.TOP_LEFT);

        int row = 0;

        Label idLbl = new Label("ID:");
        idLbl.getStyleClass().add("detail-label");
        grid.add(idLbl, 0, row);
        idLabel.getStyleClass().add("detail-value");
        grid.add(idLabel, 1, row);
        row++;

        Label titleLbl = new Label("Título:");
        titleLbl.getStyleClass().add("detail-label");
        grid.add(titleLbl, 0, row);
        grid.add(titleLabel, 1, row);
        row++;

        Label descLbl = new Label("Descripción:");
        descLbl.getStyleClass().add("detail-label");
        grid.add(descLbl, 0, row);
        grid.add(descriptionLabel, 1, row);
        row++;

        Label statusLbl = new Label("Estado:");
        statusLbl.getStyleClass().add("detail-label");
        grid.add(statusLbl, 0, row);
        grid.add(statusLabel, 1, row);
        row++;

        Label priorityLbl = new Label("Prioridad:");
        priorityLbl.getStyleClass().add("detail-label");
        grid.add(priorityLbl, 0, row);
        grid.add(priorityLabel, 1, row);
        row++;

        Label cosLbl = new Label("Clase de servicio:");
        cosLbl.getStyleClass().add("detail-label");
        grid.add(cosLbl, 0, row);
        grid.add(classOfServiceLabel, 1, row);
        row++;

        Label datesLbl = new Label("Creación / Cierre:");
        datesLbl.getStyleClass().add("detail-label");
        grid.add(datesLbl, 0, row);
        datesLabel.getStyleClass().add("detail-value");
        grid.add(datesLabel, 1, row);

        contentBox.getChildren().add(grid);
    }

    public void display(Incident incident) {
        if (incident == null) {
            contentBox.setVisible(false);
            placeholderLabel.setVisible(true);
            return;
        }

        contentBox.setVisible(true);
        placeholderLabel.setVisible(false);

        idLabel.setText(incident.getId().toString());
        titleLabel.setText(incident.getTitle());
        descriptionLabel.setText(incident.getDescription() != null && !incident.getDescription().isBlank()
                ? incident.getDescription() : "Sin descripción");
        applyStatusBadge(incident.getStatus());
        applyPriorityStyle(incident.getPriority());
        applyClassOfServiceBadge(incident.getClassOfService());
        datesLabel.setText(incident.getCreationDate()
                + " | " + (incident.getClosingDate() == null ? "-" : incident.getClosingDate()));
    }

    private void applyStatusBadge(Status status) {
        statusLabel.getStyleClass().removeAll(
                "badge", "badge-registered", "badge-ready",
                "badge-in-development", "badge-in-validation", "badge-finished");
        statusLabel.setGraphic(null);
        statusLabel.setText(status.getDisplayName());
        statusLabel.getStyleClass().add("badge");
        switch (status) {
            case REGISTERED -> statusLabel.getStyleClass().add("badge-registered");
            case READY -> statusLabel.getStyleClass().add("badge-ready");
            case IN_DEVELOPMENT -> statusLabel.getStyleClass().add("badge-in-development");
            case IN_VALIDATION -> statusLabel.getStyleClass().add("badge-in-validation");
            case FINISHED -> statusLabel.getStyleClass().add("badge-finished");
        }
    }

    private void applyPriorityStyle(Priority priority) {
        priorityLabel.getStyleClass().removeAll(
                "priority-critical-text", "priority-high-text", "priority-normal-text");
        priorityLabel.setText(priority.getDisplayName());
        switch (priority) {
            case CRITICAL -> priorityLabel.getStyleClass().add("priority-critical-text");
            case HIGH -> priorityLabel.getStyleClass().add("priority-high-text");
            default -> priorityLabel.getStyleClass().add("priority-normal-text");
        }
    }

    private void applyClassOfServiceBadge(ClassOfService cos) {
        classOfServiceLabel.getStyleClass().removeAll("badge", "badge-expedite");
        classOfServiceLabel.setGraphic(null);
        classOfServiceLabel.setText(cos.getDisplayName());
        if (cos == ClassOfService.EXPEDITE) {
            classOfServiceLabel.getStyleClass().addAll("badge", "badge-expedite");
        }
    }
}
