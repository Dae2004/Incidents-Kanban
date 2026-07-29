package com.helpdeskflow.view;

import com.helpdeskflow.controller.IncidentController;
import com.helpdeskflow.model.Category;
import com.helpdeskflow.model.ClassOfService;
import com.helpdeskflow.model.Impact;
import com.helpdeskflow.model.Incident;
import com.helpdeskflow.model.Urgency;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

/** Incident registration form with validated fields. Notifies callbacks on successful registration. */
public class IncidentFormPanel extends VBox {

    private final IncidentController incidentController;
    private final Runnable onRegistered;
    private final TextField titleField = new TextField();
    private final TextArea descriptionArea = new TextArea();
    private final ComboBox<Category> categoryBox = new ComboBox<>();
    private final ComboBox<Impact> impactBox = new ComboBox<>();
    private final ComboBox<Urgency> urgencyBox = new ComboBox<>();
    private final ComboBox<ClassOfService> serviceBox = new ComboBox<>();

    public IncidentFormPanel(IncidentController incidentController, Runnable onRegistered) {
        this.incidentController = incidentController;
        this.onRegistered = onRegistered;
        setPadding(new Insets(8));
        setSpacing(8);
        getStyleClass().add("titled-panel");

        titleField.setPromptText("Título de la incidencia *");
        titleField.setPrefColumnCount(20);
        descriptionArea.setPrefRowCount(3);
        descriptionArea.setWrapText(true);
        descriptionArea.setPromptText("Descripción detallada del problema…");
        categoryBox.getItems().setAll(Category.values());
        impactBox.getItems().setAll(Impact.values());
        urgencyBox.getItems().setAll(Urgency.values());
        serviceBox.getItems().setAll(ClassOfService.values());
        categoryBox.getSelectionModel().selectFirst();
        impactBox.getSelectionModel().selectFirst();
        urgencyBox.getSelectionModel().selectFirst();
        serviceBox.getSelectionModel().selectFirst();
        EnumComboBoxConfigurer.configure(categoryBox, "");
        EnumComboBoxConfigurer.configure(impactBox, "");
        EnumComboBoxConfigurer.configure(urgencyBox, "");
        EnumComboBoxConfigurer.configure(serviceBox, "");

        Label sectionHeader = new Label("Nueva Incidencia");
        sectionHeader.getStyleClass().add("section-header");
        getChildren().add(sectionHeader);
        getChildren().add(createForm());
    }

    private GridPane createForm() {
        GridPane grid = new GridPane();
        grid.setVgap(4);
        grid.setHgap(8);
        grid.setPadding(new Insets(4));

        ColumnConstraints labelCol = new ColumnConstraints();
        labelCol.setHalignment(HPos.RIGHT);
        ColumnConstraints fieldCol = new ColumnConstraints();
        fieldCol.setHgrow(javafx.scene.layout.Priority.ALWAYS);
        grid.getColumnConstraints().addAll(labelCol, fieldCol);

        addField(grid, "Título", titleField, 0);
        addField(grid, "Descripción", descriptionArea, 1);
        addField(grid, "Categoría", categoryBox, 2);
        addField(grid, "Impacto", impactBox, 3);
        addField(grid, "Urgencia", urgencyBox, 4);
        addField(grid, "Clase de servicio", serviceBox, 5);

        Button registerButton = new Button("Registrar");
        registerButton.getStyleClass().add("btn-primary");
        registerButton.setOnAction(event -> register());
        GridPane.setHalignment(registerButton, HPos.RIGHT);
        GridPane.setMargin(registerButton, new Insets(8, 0, 0, 0));
        grid.add(registerButton, 1, 6);

        return grid;
    }

    private void addField(GridPane grid, String label, javafx.scene.Node component, int row) {
        Label fieldLabel = new Label(label + ":");
        fieldLabel.getStyleClass().add("form-label");
        fieldLabel.setAlignment(Pos.CENTER_RIGHT);
        grid.add(fieldLabel, 0, row);
        GridPane.setMargin(fieldLabel, new Insets(3, 8, 3, 0));

        grid.add(component, 1, row);
        GridPane.setMargin(component, new Insets(3, 0, 3, 0));
        GridPane.setHgrow(component, Priority.ALWAYS);
    }

    private void register() {
        try {
            Incident incident = incidentController.register(
                    titleField.getText(),
                    descriptionArea.getText(),
                    categoryBox.getValue(),
                    impactBox.getValue(),
                    urgencyBox.getValue(),
                    serviceBox.getValue());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Registro exitoso");
            alert.setHeaderText(null);
            alert.setContentText("Incidencia registrada con prioridad " + incident.getPriority());
            alert.showAndWait();
            clearFields();
            onRegistered.run();
        } catch (RuntimeException exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No se pudo registrar");
            alert.setHeaderText(null);
            alert.setContentText(exception.getMessage());
            alert.showAndWait();
            titleField.getStyleClass().add("field-error");
            PauseTransition clear = new PauseTransition(Duration.seconds(2));
            clear.setOnFinished(e -> titleField.getStyleClass().remove("field-error"));
            clear.play();
        }
    }

    private void clearFields() {
        titleField.clear();
        descriptionArea.clear();
        categoryBox.getSelectionModel().selectFirst();
        impactBox.getSelectionModel().selectFirst();
        urgencyBox.getSelectionModel().selectFirst();
        serviceBox.getSelectionModel().selectFirst();
    }
}
