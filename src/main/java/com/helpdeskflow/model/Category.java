package com.helpdeskflow.model;

public enum Category implements Displayable {
    SOFTWARE("Software"),
    HARDWARE("Hardware"),
    NETWORK("Red"),
    SECURITY("Seguridad"),
    OTHER("Otro");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
