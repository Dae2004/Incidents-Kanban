package com.helpdeskflow.service;

import com.helpdeskflow.exception.InvalidStateTransitionException;
import com.helpdeskflow.model.Status;

import java.util.Map;

public class StateTransitionValidator {

    private static final Map<Status, Status> NEXT_STATES = Map.of(
            Status.REGISTERED, Status.READY,
            Status.READY, Status.IN_DEVELOPMENT,
            Status.IN_DEVELOPMENT, Status.IN_VALIDATION,
            Status.IN_VALIDATION, Status.FINISHED
    );

    public void validateTransition(Status currentStatus, Status targetStatus) {
        Status expectedNextState = currentStatus == null ? null : NEXT_STATES.get(currentStatus);
        if (expectedNextState != targetStatus) {
            throw new InvalidStateTransitionException(
                    "Invalid state transition from " + currentStatus + " to " + targetStatus);
        }
    }
}
