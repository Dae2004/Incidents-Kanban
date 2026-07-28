package com.helpdeskflow.service;

import com.helpdeskflow.exception.InvalidStateTransitionException;
import com.helpdeskflow.model.Category;
import com.helpdeskflow.model.Impact;
import com.helpdeskflow.model.Incident;
import com.helpdeskflow.model.Status;
import com.helpdeskflow.model.Urgency;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IncidentStateTransitionTest {

    private final IncidentService service = new IncidentService();

    @Test
    void transitionsRegisteredIncidentToReady() {
        Incident incident = registeredIncident();

        service.transitionIncident(incident, Status.READY);

        assertEquals(Status.READY, incident.getStatus());
    }

    @Test
    void transitionsReadyIncidentToInDevelopment() {
        Incident incident = registeredIncident();
        service.transitionIncident(incident, Status.READY);

        service.transitionIncident(incident, Status.IN_DEVELOPMENT);

        assertEquals(Status.IN_DEVELOPMENT, incident.getStatus());
    }

    @Test
    void transitionsInDevelopmentIncidentToInValidation() {
        Incident incident = registeredIncident();
        service.transitionIncident(incident, Status.READY);
        service.transitionIncident(incident, Status.IN_DEVELOPMENT);

        service.transitionIncident(incident, Status.IN_VALIDATION);

        assertEquals(Status.IN_VALIDATION, incident.getStatus());
    }

    @Test
    void transitionsInValidationIncidentToFinished() {
        Incident incident = registeredIncident();
        service.transitionIncident(incident, Status.READY);
        service.transitionIncident(incident, Status.IN_DEVELOPMENT);
        service.transitionIncident(incident, Status.IN_VALIDATION);

        service.transitionIncident(incident, Status.FINISHED);

        assertEquals(Status.FINISHED, incident.getStatus());
    }

    @Test
    void rejectsRegisteredToFinished() {
        assertInvalidTransition(Status.REGISTERED, Status.FINISHED);
    }

    @Test
    void rejectsReadyToRegistered() {
        assertInvalidTransition(Status.READY, Status.REGISTERED);
    }

    @Test
    void rejectsFinishedToAnyState() {
        for (Status target : new Status[]{Status.REGISTERED, Status.READY,
                Status.IN_DEVELOPMENT, Status.IN_VALIDATION, Status.FINISHED}) {
            assertInvalidTransition(Status.FINISHED, target);
        }
    }

    private void assertInvalidTransition(Status current, Status target) {
        Incident incident = registeredIncident();
        incident.setStatus(current);

        assertThrows(InvalidStateTransitionException.class,
                () -> service.transitionIncident(incident, target));
        assertEquals(current, incident.getStatus());
    }

    private Incident registeredIncident() {
        return service.registerIncident("Test incident", "Test description",
                Category.SOFTWARE, Impact.LOW, Urgency.LOW);
    }
}
