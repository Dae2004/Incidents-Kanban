package com.helpdeskflow.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IncidentIdTest {

    @Test
    void storesIdentifierValue() {
        IncidentId incidentId = new IncidentId("INC-001");

        assertEquals("INC-001", incidentId.getValue());
    }

    @Test
    void identifiersWithSameValueAreEqual() {
        assertEquals(new IncidentId("INC-001"), new IncidentId("INC-001"));
    }
}
