package com.helpdeskflow.model;

public enum Urgency implements Displayable {
    LOW("Baja"),
    MEDIUM("Media"),
    HIGH("Alta");

    private final String displayName;

    Urgency(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
