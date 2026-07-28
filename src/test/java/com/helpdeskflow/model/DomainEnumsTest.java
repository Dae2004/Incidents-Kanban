package com.helpdeskflow.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class DomainEnumsTest {

    @Test
    void impactHasExpectedValues() {
        assertArrayEquals(new Impact[]{Impact.LOW, Impact.MEDIUM, Impact.HIGH}, Impact.values());
    }

    @Test
    void urgencyHasExpectedValues() {
        assertArrayEquals(new Urgency[]{Urgency.LOW, Urgency.MEDIUM, Urgency.HIGH}, Urgency.values());
    }

    @Test
    void priorityHasExpectedValues() {
        assertArrayEquals(new Priority[]{Priority.NORMAL, Priority.HIGH, Priority.CRITICAL}, Priority.values());
    }

    @Test
    void statusHasExpectedValues() {
        assertArrayEquals(new Status[]{Status.REGISTERED, Status.READY, Status.IN_DEVELOPMENT,
                Status.IN_VALIDATION, Status.FINISHED}, Status.values());
    }

    @Test
    void classOfServiceHasExpectedValues() {
        assertArrayEquals(new ClassOfService[]{ClassOfService.STANDARD, ClassOfService.EXPEDITE},
                ClassOfService.values());
    }

    @Test
    void categoryHasExpectedValues() {
        assertArrayEquals(new Category[]{Category.SOFTWARE, Category.HARDWARE, Category.NETWORK,
                Category.SECURITY, Category.OTHER}, Category.values());
    }
}
