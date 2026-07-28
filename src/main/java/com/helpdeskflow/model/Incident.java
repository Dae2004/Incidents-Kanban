package com.helpdeskflow.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Incident {

    private IncidentId id;
    private String title;
    private String description;
    private Category category;
    private Impact impact;
    private Urgency urgency;
    private Priority priority;
    private Status status;
    private LocalDateTime creationDate;
    private LocalDateTime closingDate;
    private String solutionDescription;
    private ClassOfService classOfService;

    public Incident(IncidentId id, String title, String description, Category category,
                    Impact impact, Urgency urgency, Priority priority, Status status,
                    LocalDateTime creationDate, LocalDateTime closingDate,
                    String solutionDescription, ClassOfService classOfService) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.impact = impact;
        this.urgency = urgency;
        this.priority = priority;
        this.status = status;
        this.creationDate = creationDate;
        this.closingDate = closingDate;
        this.solutionDescription = solutionDescription;
        this.classOfService = classOfService;
    }

    public IncidentId getId() { return id; }
    public void setId(IncidentId id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    public Impact getImpact() { return impact; }
    public void setImpact(Impact impact) { this.impact = impact; }
    public Urgency getUrgency() { return urgency; }
    public void setUrgency(Urgency urgency) { this.urgency = urgency; }
    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public LocalDateTime getCreationDate() { return creationDate; }
    public void setCreationDate(LocalDateTime creationDate) { this.creationDate = creationDate; }
    public LocalDateTime getClosingDate() { return closingDate; }
    public void setClosingDate(LocalDateTime closingDate) { this.closingDate = closingDate; }
    public String getSolutionDescription() { return solutionDescription; }
    public void setSolutionDescription(String solutionDescription) { this.solutionDescription = solutionDescription; }
    public ClassOfService getClassOfService() { return classOfService; }
    public void setClassOfService(ClassOfService classOfService) { this.classOfService = classOfService; }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Incident)) {
            return false;
        }
        Incident incident = (Incident) other;
        return Objects.equals(id, incident.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Incident{" + "id=" + id + ", title='" + title + '\'' + '}';
    }
}
