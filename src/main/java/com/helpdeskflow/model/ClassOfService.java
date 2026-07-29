package com.helpdeskflow.model;

public enum ClassOfService implements Displayable {
    STANDARD("Estándar"),
    EXPEDITE("Expeditivo");

    private final String displayName;

    ClassOfService(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
