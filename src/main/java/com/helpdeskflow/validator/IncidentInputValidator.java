package com.helpdeskflow.validator;

import com.helpdeskflow.model.Category;
import com.helpdeskflow.model.Impact;
import com.helpdeskflow.model.Urgency;

public final class IncidentInputValidator {

    private IncidentInputValidator() {
    }

    public static void validate(String title, String description, Category category,
                                Impact impact, Urgency urgency) {
        validateText(title, "Title");
        validateText(description, "Description");
        validateRequired(category, "Category");
        validateRequired(impact, "Impact");
        validateRequired(urgency, "Urgency");
    }

    private static void validateText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or blank");
        }
    }

    private static void validateRequired(Object value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " cannot be null");
        }
    }
}
