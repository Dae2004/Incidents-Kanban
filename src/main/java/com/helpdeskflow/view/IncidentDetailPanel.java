package com.helpdeskflow.view;

import com.helpdeskflow.model.Incident;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.GridLayout;

public class IncidentDetailPanel extends JPanel {

    private final JLabel idLabel = new JLabel("-");
    private final JLabel titleLabel = new JLabel("-");
    private final JLabel statusLabel = new JLabel("-");
    private final JLabel priorityLabel = new JLabel("-");
    private final JLabel datesLabel = new JLabel("-");

    public IncidentDetailPanel() {
        setBorder(BorderFactory.createTitledBorder("Detalle"));
        setLayout(new GridLayout(5, 1, 4, 4));
        add(idLabel);
        add(titleLabel);
        add(statusLabel);
        add(priorityLabel);
        add(datesLabel);
    }

    public void display(Incident incident) {
        if (incident == null) {
            idLabel.setText("ID: -");
            titleLabel.setText("Título: -");
            statusLabel.setText("Estado: -");
            priorityLabel.setText("Prioridad: -");
            datesLabel.setText("Creación: -");
            return;
        }
        idLabel.setText("ID: " + incident.getId());
        titleLabel.setText("Título: " + incident.getTitle());
        statusLabel.setText("Estado: " + incident.getStatus());
        priorityLabel.setText("Prioridad: " + incident.getPriority());
        datesLabel.setText("Creación: " + incident.getCreationDate()
                + " | Cierre: " + (incident.getClosingDate() == null ? "-" : incident.getClosingDate()));
    }
}
