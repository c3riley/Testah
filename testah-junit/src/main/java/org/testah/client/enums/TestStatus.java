package org.testah.client.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The Enum TestStatus.
 */
public enum TestStatus {

    /**
     * The passed.
     */
    PASSED(true),
    /**
     * The failed.
     */
    FAILED(false),
    /**
     * JUnit initialization failure.
     */
    INIT_FAILURE(false),
    /**
     * The na.
     */
    NA(null),
    /**
     * The ignore.
     */
    IGNORE(null),
    /**
     * The critical.
     */
    CRITICAL(false);

    /**
     * The status.
     */
    private final Boolean status;

    /**
     * Instantiates a new test status.
     *
     * @param status the status
     */
    TestStatus(final Boolean status) {
        this.status = status;
    }

    /**
     * Gets the status.
     *
     * @param statusValue the status value
     * @return the status
     */
    public static TestStatus getStatus(final Boolean statusValue) {
        if (null == statusValue) {
            return NA;
        } else if (statusValue) {
            return PASSED;
        } else {
            return FAILED;
        }
    }

    /**
     * Gets the status.
     *
     * @return the status
     */
    public Boolean getStatus() {
        return status;
    }

    @Override
    @JsonValue
    public String toString() {
        return this.name();
    }

}
