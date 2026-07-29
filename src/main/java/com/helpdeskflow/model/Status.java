package com.helpdeskflow.model;

public enum Status implements Displayable {
    REGISTERED("Registrado"),
    READY("Listo"),
    IN_DEVELOPMENT("En desarrollo"),
    IN_VALIDATION("En validación"),
    FINISHED("Finalizado");

    private final String displayName;

    Status(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
