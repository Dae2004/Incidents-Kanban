package com.helpdeskflow.model;

public enum Impact implements Displayable {
    LOW("Bajo"),
    MEDIUM("Medio"),
    HIGH("Alto");

    private final String displayName;

    Impact(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
