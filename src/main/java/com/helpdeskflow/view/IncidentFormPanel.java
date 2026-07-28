package com.helpdeskflow.view;

import com.helpdeskflow.controller.IncidentController;
import com.helpdeskflow.model.Category;
import com.helpdeskflow.model.ClassOfService;
import com.helpdeskflow.model.Impact;
import com.helpdeskflow.model.Incident;
import com.helpdeskflow.model.Urgency;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class IncidentFormPanel extends JPanel {

    private final IncidentController incidentController;
    private final Runnable onRegistered;
    private final JTextField titleField = new JTextField();
    private final JTextArea descriptionArea = new JTextArea(3, 24);
    private final JComboBox<Category> categoryBox = new JComboBox<>(Category.values());
    private final JComboBox<Impact> impactBox = new JComboBox<>(Impact.values());
    private final JComboBox<Urgency> urgencyBox = new JComboBox<>(Urgency.values());
    private final JComboBox<ClassOfService> serviceBox = new JComboBox<>(ClassOfService.values());

    public IncidentFormPanel(IncidentController incidentController, Runnable onRegistered) {
        this.incidentController = incidentController;
        this.onRegistered = onRegistered;
        setBorder(BorderFactory.createTitledBorder("Registrar incidencia"));
        setLayout(new BorderLayout());
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        add(createForm(), BorderLayout.CENTER);
    }

    private JPanel createForm() {
        JPanel form = new JPanel(new GridBagLayout());
        addField(form, "Título", titleField, 0);
        addField(form, "Descripción", new JScrollPane(descriptionArea), 1);
        addField(form, "Categoría", categoryBox, 2);
        addField(form, "Impacto", impactBox, 3);
        addField(form, "Urgencia", urgencyBox, 4);
        addField(form, "Clase de servicio", serviceBox, 5);

        JButton registerButton = new JButton("Registrar");
        registerButton.addActionListener(event -> register());
        GridBagConstraints buttonConstraints = new GridBagConstraints();
        buttonConstraints.gridx = 1;
        buttonConstraints.gridy = 6;
        buttonConstraints.anchor = GridBagConstraints.LINE_END;
        buttonConstraints.insets = new Insets(4, 4, 4, 4);
        form.add(registerButton, buttonConstraints);
        return form;
    }

    private void addField(JPanel form, String label, java.awt.Component component, int row) {
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.gridy = row;
        labelConstraints.anchor = GridBagConstraints.LINE_END;
        labelConstraints.insets = new Insets(4, 4, 4, 8);
        form.add(new JLabel(label + ":"), labelConstraints);

        GridBagConstraints fieldConstraints = new GridBagConstraints();
        fieldConstraints.gridx = 1;
        fieldConstraints.gridy = row;
        fieldConstraints.weightx = 1;
        fieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        fieldConstraints.insets = new Insets(4, 4, 4, 4);
        form.add(component, fieldConstraints);
    }

    private void register() {
        try {
            Incident incident = incidentController.register(
                    titleField.getText(),
                    descriptionArea.getText(),
                    (Category) categoryBox.getSelectedItem(),
                    (Impact) impactBox.getSelectedItem(),
                    (Urgency) urgencyBox.getSelectedItem(),
                    (ClassOfService) serviceBox.getSelectedItem()
            );
            JOptionPane.showMessageDialog(this,
                    "Incidencia registrada con prioridad " + incident.getPriority(),
                    "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
            onRegistered.run();
        } catch (RuntimeException exception) {
            JOptionPane.showMessageDialog(this, exception.getMessage(),
                    "No se pudo registrar", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        titleField.setText("");
        descriptionArea.setText("");
        categoryBox.setSelectedIndex(0);
        impactBox.setSelectedIndex(0);
        urgencyBox.setSelectedIndex(0);
        serviceBox.setSelectedIndex(0);
    }
}
