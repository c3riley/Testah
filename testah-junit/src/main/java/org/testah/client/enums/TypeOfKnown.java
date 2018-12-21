package org.testah.client.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The Enum TypeOfKnown.
 */
public enum TypeOfKnown {

    /**
     * The flaky.
     */
    FLAKY("Test has issues causing it to fail sometimes requires investigation"),

    /**
     * The refactor required.
     */
    REFACTOR_REQUIRED("Automation related issues, requires code to be refactored"),

    /**
     * The defect.
     */
    DEFECT("Application Under Test Defect results in fail"),

    /**
     * The app changed.
     */
    APP_CHANGED("Application Under Test has changed causes test to fail and need refactor"),

    /**
     * The envir.
     */
    ENVIR("Issue with the Testing environment"),

    /**
     * The other.
     */
    OTHER("Other type of issue see related linked ids");

    /**
     * The description.
     */
    final String description;

    /**
     * Instantiates a new type of known.
     *
     * @param description the description
     */
    TypeOfKnown(final String description) {
        this.description = description;
    }

    @Override
    @JsonValue
    public String toString() {
        return this.name();
    }

}
