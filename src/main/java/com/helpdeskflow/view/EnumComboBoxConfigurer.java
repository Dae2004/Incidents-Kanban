package com.helpdeskflow.view;

import com.helpdeskflow.model.Displayable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;

final class EnumComboBoxConfigurer {

    private EnumComboBoxConfigurer() {
    }

    static <T extends Enum<T> & Displayable> void configure(ComboBox<T> comboBox, String nullDisplayText) {
        comboBox.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item == null ? nullDisplayText : item.getDisplayName());
            }
        });
        comboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item == null ? nullDisplayText : item.getDisplayName());
            }
        });
    }
}
