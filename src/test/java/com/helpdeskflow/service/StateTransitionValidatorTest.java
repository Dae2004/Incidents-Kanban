package com.helpdeskflow.service;

import com.helpdeskflow.exception.InvalidStateTransitionException;
import com.helpdeskflow.model.Status;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StateTransitionValidatorTest {

    private final StateTransitionValidator validator = new StateTransitionValidator();

    @Test
    void acceptsOnlyTheNextLifecycleState() {
        assertDoesNotThrow(() -> validator.validateTransition(Status.REGISTERED, Status.READY));
        assertDoesNotThrow(() -> validator.validateTransition(Status.READY, Status.IN_DEVELOPMENT));
        assertDoesNotThrow(() -> validator.validateTransition(Status.IN_DEVELOPMENT, Status.IN_VALIDATION));
        assertDoesNotThrow(() -> validator.validateTransition(Status.IN_VALIDATION, Status.FINISHED));
    }

    @Test
    void rejectsTransitionsNotDefinedByTheLifecycle() {
        assertThrows(InvalidStateTransitionException.class,
                () -> validator.validateTransition(Status.REGISTERED, Status.FINISHED));
        assertThrows(InvalidStateTransitionException.class,
                () -> validator.validateTransition(Status.READY, Status.REGISTERED));
        assertThrows(InvalidStateTransitionException.class,
                () -> validator.validateTransition(Status.FINISHED, Status.READY));
    }
}
