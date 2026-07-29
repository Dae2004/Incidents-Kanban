package com.helpdeskflow.model;

public enum Priority implements Displayable {
    NORMAL("Normal"),
    HIGH("Alta"),
    CRITICAL("Crítica");

    private final String displayName;

    Priority(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
