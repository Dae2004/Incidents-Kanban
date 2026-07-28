package com.helpdeskflow.view;

import com.helpdeskflow.controller.IncidentController;
import com.helpdeskflow.model.Incident;
import com.helpdeskflow.model.IncidentId;
import com.helpdeskflow.model.Priority;
import com.helpdeskflow.model.Status;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;
import java.util.function.Consumer;

public class IncidentListPanel extends JPanel {

    private final IncidentController incidentController;
    private final Consumer<Incident> onSelectionChanged;
    private final Runnable onDataChanged;
    private final JTextField searchField = new JTextField(16);
    private final JComboBox<Priority> priorityBox = new JComboBox<>();
    private final JComboBox<Status> statusBox = new JComboBox<>();
    private final JComboBox<String> scopeBox = new JComboBox<>(new String[]{"Todas", "Abiertas", "Cerradas"});
    private final JComboBox<Status> targetStatusBox = new JComboBox<>(Status.values());
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"ID", "Título", "Prioridad", "Estado", "Categoría"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(tableModel);
    private List<Incident> displayedIncidents = List.of();

    public IncidentListPanel(IncidentController incidentController,
                             Consumer<Incident> onSelectionChanged, Runnable onDataChanged) {
        this.incidentController = incidentController;
        this.onSelectionChanged = onSelectionChanged;
        this.onDataChanged = onDataChanged;
        setBorder(BorderFactory.createTitledBorder("Incidencias"));
        setLayout(new BorderLayout(6, 6));
        add(createToolbar(), BorderLayout.NORTH);
        configureTable();
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(createStatusToolbar(), BorderLayout.SOUTH);
        refresh();
    }

    private JPanel createToolbar() {
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEADING));
        toolbar.add(new JLabel("Buscar ID:"));
        toolbar.add(searchField);
        JButton searchButton = new JButton("Buscar");
        searchButton.addActionListener(event -> refresh());
        toolbar.add(searchButton);
        toolbar.add(new JLabel("Vista:"));
        toolbar.add(scopeBox);
        toolbar.add(new JLabel("Prioridad:"));
        priorityBox.addItem(null);
        for (Priority priority : Priority.values()) {
            priorityBox.addItem(priority);
        }
        toolbar.add(priorityBox);
        toolbar.add(new JLabel("Estado:"));
        statusBox.addItem(null);
        for (Status status : Status.values()) {
            statusBox.addItem(status);
        }
        toolbar.add(statusBox);
        JButton refreshButton = new JButton("Actualizar");
        refreshButton.addActionListener(event -> refresh());
        toolbar.add(refreshButton);
        return toolbar;
    }

    private JPanel createStatusToolbar() {
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        toolbar.add(new JLabel("Nuevo estado:"));
        toolbar.add(targetStatusBox);
        JButton changeButton = new JButton("Cambiar estado");
        changeButton.addActionListener(event -> changeSelectedStatus());
        toolbar.add(changeButton);
        return toolbar;
    }

    private void configureTable() {
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                onSelectionChanged.accept(displayedIncidents.get(table.getSelectedRow()));
            }
        });
    }

    public void refresh() {
        String search = searchField.getText().trim();
        if (!search.isEmpty()) {
            displayedIncidents = incidentController.findById(new IncidentId(search))
                    .map(List::of)
                    .orElseGet(List::of);
        } else if (priorityBox.getSelectedItem() instanceof Priority priority) {
            displayedIncidents = incidentController.findByPriority(priority);
        } else if (statusBox.getSelectedItem() instanceof Status status) {
            displayedIncidents = incidentController.findByStatus(status);
        } else if ("Abiertas".equals(scopeBox.getSelectedItem())) {
            displayedIncidents = incidentController.findOpen();
        } else if ("Cerradas".equals(scopeBox.getSelectedItem())) {
            displayedIncidents = incidentController.findClosed();
        } else {
            displayedIncidents = incidentController.findAll();
        }
        populateTable();
    }

    private void populateTable() {
        tableModel.setRowCount(0);
        for (Incident incident : displayedIncidents) {
            tableModel.addRow(new Object[]{incident.getId(), incident.getTitle(), incident.getPriority(),
                    incident.getStatus(), incident.getCategory()});
        }
    }

    private void changeSelectedStatus() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una incidencia primero.",
                    "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            incidentController.changeStatus(displayedIncidents.get(selectedRow),
                    (Status) targetStatusBox.getSelectedItem());
            JOptionPane.showMessageDialog(this, "Estado actualizado correctamente.",
                    "Operación exitosa", JOptionPane.INFORMATION_MESSAGE);
            refresh();
            onDataChanged.run();
        } catch (RuntimeException exception) {
            JOptionPane.showMessageDialog(this, exception.getMessage(),
                    "No se pudo cambiar el estado", JOptionPane.ERROR_MESSAGE);
        }
    }
}
