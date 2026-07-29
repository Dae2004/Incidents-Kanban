package com.helpdeskflow.view;

import com.helpdeskflow.controller.IncidentController;
import com.helpdeskflow.model.Incident;
import com.helpdeskflow.model.IncidentId;
import com.helpdeskflow.model.Priority;
import com.helpdeskflow.model.Status;
import com.helpdeskflow.model.ClassOfService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.function.Consumer;

/** Incident table with search, filter, and status-change controls. Displays color-coded priority rows and status badges. */
public class IncidentListPanel extends BorderPane {

    private final IncidentController incidentController;
    private final Consumer<Incident> onSelectionChanged;
    private final Runnable onDataChanged;
    private final TextField searchField = new TextField();
    private final ComboBox<Priority> priorityBox = new ComboBox<>();
    private final ComboBox<Status> statusBox = new ComboBox<>();
    private final ComboBox<String> scopeBox = new ComboBox<>();
    private final ComboBox<Status> targetStatusBox = new ComboBox<>();
    private final ObservableList<Incident> tableItems = FXCollections.observableArrayList();
    private final TableView<Incident> table = new TableView<>();
    private final Label emptyLabel = new Label("No hay incidencias registradas");
    private final Label resultCountLabel = new Label();
    private List<Incident> displayedIncidents = List.of();

    public IncidentListPanel(IncidentController incidentController,
                             Consumer<Incident> onSelectionChanged, Runnable onDataChanged) {
        this.incidentController = incidentController;
        this.onSelectionChanged = onSelectionChanged;
        this.onDataChanged = onDataChanged;
        setPadding(new Insets(8));
        getStyleClass().add("titled-panel");

        searchField.setPromptText("Buscar por ID de incidencia…");
        searchField.setPrefColumnCount(14);
        priorityBox.getItems().add(null);
        priorityBox.getItems().addAll(Priority.values());
        priorityBox.getSelectionModel().selectFirst();
        statusBox.getItems().add(null);
        statusBox.getItems().addAll(Status.values());
        statusBox.getSelectionModel().selectFirst();
        scopeBox.getItems().setAll("Todas", "Abiertas", "Cerradas");
        scopeBox.getSelectionModel().selectFirst();
        targetStatusBox.getItems().setAll(Status.values());
        targetStatusBox.getSelectionModel().selectFirst();
        EnumComboBoxConfigurer.configure(priorityBox, "Todas");
        EnumComboBoxConfigurer.configure(statusBox, "Todas");
        EnumComboBoxConfigurer.configure(targetStatusBox, "");
        emptyLabel.getStyleClass().add("empty-state");
        resultCountLabel.getStyleClass().add("toolbar-results");
        resultCountLabel.setPadding(new Insets(4, 4, 0, 4));

        configureTable();

        StackPane tableWrapper = new StackPane();
        tableWrapper.getChildren().addAll(table, emptyLabel);
        emptyLabel.setVisible(false);

        VBox centerBox = new VBox(2);
        centerBox.getChildren().addAll(resultCountLabel, tableWrapper);
        VBox.setVgrow(tableWrapper, javafx.scene.layout.Priority.ALWAYS);

        setTop(createToolbar());
        setCenter(centerBox);
        setBottom(createStatusToolbar());
        refresh();
    }

    private HBox createToolbar() {
        HBox toolbar = new HBox(8);
        toolbar.getStyleClass().add("toolbar");
        toolbar.setAlignment(Pos.CENTER_LEFT);

        toolbar.getChildren().add(new Label("Buscar ID:"));
        toolbar.getChildren().add(searchField);

        Button searchButton = new Button("Buscar");
        searchButton.getStyleClass().add("btn-primary");
        searchButton.setOnAction(event -> refresh());
        toolbar.getChildren().add(searchButton);

        toolbar.getChildren().add(new Label("Vista:"));
        toolbar.getChildren().add(scopeBox);

        toolbar.getChildren().add(new Label("Prioridad:"));
        toolbar.getChildren().add(priorityBox);

        toolbar.getChildren().add(new Label("Estado:"));
        toolbar.getChildren().add(statusBox);

        Button refreshButton = new Button("Actualizar");
        refreshButton.getStyleClass().add("btn-default");
        refreshButton.setOnAction(event -> refresh());
        toolbar.getChildren().add(refreshButton);

        return toolbar;
    }

    private HBox createStatusToolbar() {
        HBox toolbar = new HBox(8);
        toolbar.setAlignment(Pos.CENTER_RIGHT);
        toolbar.setPadding(new Insets(6, 0, 0, 0));

        toolbar.getChildren().add(new Label("Nuevo estado:"));
        toolbar.getChildren().add(targetStatusBox);

        Button changeButton = new Button("Cambiar estado");
        changeButton.getStyleClass().add("btn-success");
        changeButton.setOnAction(event -> changeSelectedStatus());
        toolbar.getChildren().add(changeButton);

        return toolbar;
    }

    private void configureTable() {
        TableColumn<Incident, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId().toString()));
        idCol.setPrefWidth(80);
        idCol.setMinWidth(70);
        idCol.setMaxWidth(100);

        TableColumn<Incident, String> titleCol = new TableColumn<>("Título");
        titleCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));

        TableColumn<Incident, Priority> priorityCol = new TableColumn<>("Prioridad");
        priorityCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPriority()));
        priorityCol.setPrefWidth(100);
        priorityCol.setMinWidth(90);
        priorityCol.setMaxWidth(120);
        priorityCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Priority priority, boolean empty) {
                super.updateItem(priority, empty);
                if (empty || priority == null) {
                    setText(null);
                    getStyleClass().removeAll("priority-critical-text", "priority-high-text", "priority-normal-text");
                } else {
                    setText(priority.getDisplayName());
                    getStyleClass().removeAll("priority-critical-text", "priority-high-text", "priority-normal-text");
                    switch (priority) {
                        case CRITICAL -> getStyleClass().add("priority-critical-text");
                        case HIGH -> getStyleClass().add("priority-high-text");
                        default -> getStyleClass().add("priority-normal-text");
                    }
                }
            }
        });

        TableColumn<Incident, Status> statusCol = new TableColumn<>("Estado");
        statusCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getStatus()));
        statusCol.setPrefWidth(115);
        statusCol.setMinWidth(105);
        statusCol.setMaxWidth(140);
        statusCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Status status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setGraphic(null);
                    getStyleClass().removeAll("badge", "badge-registered", "badge-ready",
                            "badge-in-development", "badge-in-validation", "badge-finished");
                } else {
                    Label badge = new Label(status.getDisplayName());
                    badge.getStyleClass().add("badge");
                    switch (status) {
                        case REGISTERED -> badge.getStyleClass().add("badge-registered");
                        case READY -> badge.getStyleClass().add("badge-ready");
                        case IN_DEVELOPMENT -> badge.getStyleClass().add("badge-in-development");
                        case IN_VALIDATION -> badge.getStyleClass().add("badge-in-validation");
                        case FINISHED -> badge.getStyleClass().add("badge-finished");
                    }
                    setGraphic(badge);
                    setText(null);
                }
            }
        });

        TableColumn<Incident, String> categoryCol = new TableColumn<>("Categoría");
        categoryCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategory().getDisplayName()));
        categoryCol.setPrefWidth(100);
        categoryCol.setMinWidth(90);
        categoryCol.setMaxWidth(130);

        TableColumn<Incident, String> classOfServiceCol = new TableColumn<>("Clase de servicio");
        classOfServiceCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getClassOfService().getDisplayName()));
        classOfServiceCol.setPrefWidth(125);
        classOfServiceCol.setMinWidth(115);
        classOfServiceCol.setMaxWidth(150);
        classOfServiceCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String cos, boolean empty) {
                super.updateItem(cos, empty);
                if (empty || cos == null) {
                    setText(null);
                    setGraphic(null);
                    getStyleClass().removeAll("badge", "badge-expedite");
                } else {
                    Label badge = new Label(cos);
                    if (ClassOfService.EXPEDITE.getDisplayName().equals(cos)) {
                        badge.getStyleClass().addAll("badge", "badge-expedite");
                        setGraphic(badge);
                        setText(null);
                    } else {
                        setText(cos);
                        setGraphic(null);
                    }
                }
            }
        });

        table.getColumns().add(idCol);
        table.getColumns().add(titleCol);
        table.getColumns().add(priorityCol);
        table.getColumns().add(statusCol);
        table.getColumns().add(categoryCol);
        table.getColumns().add(classOfServiceCol);
        table.setItems(tableItems);
        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                onSelectionChanged.accept(newVal);
            }
        });
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        VBox.setVgrow(table, javafx.scene.layout.Priority.ALWAYS);

        table.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Incident incident, boolean empty) {
                super.updateItem(incident, empty);
                getStyleClass().removeAll("priority-critical", "priority-high");
                if (!empty && incident != null) {
                    switch (incident.getPriority()) {
                        case CRITICAL -> getStyleClass().add("priority-critical");
                        case HIGH -> getStyleClass().add("priority-high");
                    }
                }
            }
        });
    }

    public void refresh() {
        String search = searchField.getText().trim();
        if (!search.isEmpty()) {
            displayedIncidents = incidentController.findById(new IncidentId(search))
                    .map(List::of)
                    .orElseGet(List::of);
        } else if (priorityBox.getValue() != null) {
            displayedIncidents = incidentController.findByPriority(priorityBox.getValue());
        } else if (statusBox.getValue() != null) {
            displayedIncidents = incidentController.findByStatus(statusBox.getValue());
        } else if ("Abiertas".equals(scopeBox.getValue())) {
            displayedIncidents = incidentController.findOpen();
        } else if ("Cerradas".equals(scopeBox.getValue())) {
            displayedIncidents = incidentController.findClosed();
        } else {
            displayedIncidents = incidentController.findAll();
        }
        populateTable();
    }

    private void populateTable() {
        tableItems.setAll(displayedIncidents);
        emptyLabel.setVisible(displayedIncidents.isEmpty());
        table.setVisible(!displayedIncidents.isEmpty());
        boolean isSearching = !searchField.getText().trim().isEmpty();
        emptyLabel.setText(isSearching ? "No se encontraron incidencias" : "No hay incidencias registradas");
        resultCountLabel.setText(displayedIncidents.size() > 0
                ? "Mostrando " + displayedIncidents.size() + " resultado"
                        + (displayedIncidents.size() != 1 ? "s" : "")
                : "");
    }

    private void changeSelectedStatus() {
        Incident selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Sin selección");
            alert.setHeaderText(null);
            alert.setContentText("Seleccione una incidencia primero.");
            alert.showAndWait();
            return;
        }
        try {
            incidentController.changeStatus(selected, targetStatusBox.getValue());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Operación exitosa");
            alert.setHeaderText(null);
            alert.setContentText("Estado actualizado correctamente.");
            alert.showAndWait();
            refresh();
            onDataChanged.run();
        } catch (RuntimeException exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No se pudo cambiar el estado");
            alert.setHeaderText(null);
            alert.setContentText(exception.getMessage());
            alert.showAndWait();
        }
    }
}
