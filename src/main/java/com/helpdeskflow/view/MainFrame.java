package com.helpdeskflow.view;

import com.helpdeskflow.controller.IncidentController;
import com.helpdeskflow.controller.MetricsController;
import com.helpdeskflow.persistence.DatabaseManager;
import com.helpdeskflow.repository.IncidentRepositoryJdbc;
import com.helpdeskflow.service.IncidentService;
import com.helpdeskflow.service.MetricsCalculator;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;

public class MainFrame extends JFrame {

    public MainFrame(IncidentService incidentService) {
        super("HelpDesk Flow");
        IncidentController incidentController = new IncidentController(incidentService);
        MetricsController metricsController = new MetricsController(incidentService, new MetricsCalculator());
        MetricsPanel metricsPanel = new MetricsPanel(metricsController);
        IncidentDetailPanel detailPanel = new IncidentDetailPanel();
        IncidentListPanel listPanel = new IncidentListPanel(incidentController,
                detailPanel::display, metricsPanel::refresh);
        IncidentFormPanel formPanel = new IncidentFormPanel(incidentController, () -> {
            listPanel.refresh();
            metricsPanel.refresh();
        });

        JTabbedPane tabs = new JTabbedPane();
        JSplitPane incidentContent = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                listPanel, detailPanel);
        incidentContent.setResizeWeight(0.78);
        JPanel incidentPanel = new JPanel(new BorderLayout(8, 8));
        incidentPanel.add(formPanel, BorderLayout.NORTH);
        incidentPanel.add(incidentContent, BorderLayout.CENTER);
        tabs.addTab("Incidencias", incidentPanel);
        tabs.addTab("Métricas", metricsPanel);

        setLayout(new BorderLayout());
        add(tabs, BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 760);
        setLocationRelativeTo(null);
    }

    public static MainFrame createDefault() {
        IncidentService service = new IncidentService(new IncidentRepositoryJdbc(new DatabaseManager()));
        return new MainFrame(service);
    }

    public static void showApplication() {
        SwingUtilities.invokeLater(() -> createDefault().setVisible(true));
    }

}
