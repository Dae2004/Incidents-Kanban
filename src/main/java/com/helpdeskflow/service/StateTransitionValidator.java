package com.helpdeskflow.service;

import com.helpdeskflow.exception.InvalidStateTransitionException;
import com.helpdeskflow.model.Status;

import java.util.Map;

/** Validates the ordered incident workflow transitions. */
public class StateTransitionValidator {

    private static final Map<Status, Status> NEXT_STATES = Map.of(
            Status.REGISTERED, Status.READY,
            Status.READY, Status.IN_DEVELOPMENT,
            Status.IN_DEVELOPMENT, Status.IN_VALIDATION,
            Status.IN_VALIDATION, Status.FINISHED
    );

    /**
     * Validates that the target is the immediate next workflow state.
     *
     * @throws InvalidStateTransitionException when the transition is not allowed
     */
    public void validateTransition(Status currentStatus, Status targetStatus) {
        Status expectedNextState = currentStatus == null ? null : NEXT_STATES.get(currentStatus);
        if (expectedNextState != targetStatus) {
            throw new InvalidStateTransitionException(
                    "Invalid state transition from " + currentStatus + " to " + targetStatus);
        }
    }
}
