package com.helpdeskflow.view;

import com.helpdeskflow.controller.IncidentController;
import com.helpdeskflow.controller.MetricsController;
import com.helpdeskflow.persistence.DatabaseManager;
import com.helpdeskflow.repository.IncidentRepositoryJdbc;
import com.helpdeskflow.service.IncidentService;
import com.helpdeskflow.service.MetricsCalculator;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/** Composes the main application window with header, tabbed content, and footer. Loads the stylesheet. */
public class MainFrame {

    private MainFrame() {
    }

    public static Scene createDefault() {
        IncidentService service = new IncidentService(new IncidentRepositoryJdbc(new DatabaseManager()));
        IncidentController incidentController = new IncidentController(service);
        MetricsController metricsController = new MetricsController(service, new MetricsCalculator());

        MetricsPanel metricsPanel = new MetricsPanel(metricsController);
        IncidentDetailPanel detailPanel = new IncidentDetailPanel();
        IncidentListPanel listPanel = new IncidentListPanel(incidentController,
                detailPanel::display, metricsPanel::refresh);
        IncidentFormPanel formPanel = new IncidentFormPanel(incidentController, () -> {
            listPanel.refresh();
            metricsPanel.refresh();
        });

        SplitPane incidentContent = new SplitPane();
        incidentContent.getItems().addAll(listPanel, detailPanel);
        incidentContent.setDividerPositions(0.78);

        VBox incidentPanel = new VBox(8);
        incidentPanel.getChildren().addAll(formPanel, incidentContent);
        VBox.setVgrow(incidentContent, javafx.scene.layout.Priority.ALWAYS);

        TabPane tabs = new TabPane();
        Tab incidentsTab = new Tab("\uD83D\uDCCB Incidencias", incidentPanel);
        incidentsTab.setClosable(false);
        Tab metricsTab = new Tab("\uD83D\uDCCA Métricas", metricsPanel);
        metricsTab.setClosable(false);
        tabs.getTabs().addAll(incidentsTab, metricsTab);

        HBox header = new HBox();
        header.getStyleClass().add("header-bar");
        VBox headerText = new VBox();
        headerText.setSpacing(2);
        Label title = new Label("HelpDesk Flow");
        title.getStyleClass().add("header-title");
        Label subtitle = new Label("Gestión de Incidencias");
        subtitle.getStyleClass().add("header-subtitle");
        headerText.getChildren().addAll(title, subtitle);
        header.getChildren().add(headerText);

        HBox footer = new HBox();
        footer.getStyleClass().add("footer-bar");
        Label footerLabel = new Label("v1.0-SNAPSHOT  |  JavaFX 21  |  SQLite");
        footerLabel.getStyleClass().add("footer-text");
        footer.getChildren().add(footerLabel);

        BorderPane root = new BorderPane();
        root.getStyleClass().add("app-root");
        root.setTop(header);
        root.setCenter(tabs);
        root.setBottom(footer);

        Scene scene = new Scene(root, 1200, 760);
        root.setSnapToPixel(true);
        scene.getStylesheets().add(
                MainFrame.class.getResource("/css/helpdesk-flow.css").toExternalForm());
        return scene;
    }

    public static void showApplication(Stage primaryStage) {
        Scene scene = createDefault();
        primaryStage.setScene(scene);
        primaryStage.setTitle("HelpDesk Flow");
        primaryStage.show();
    }
}
