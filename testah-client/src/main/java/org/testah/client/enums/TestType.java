package org.testah.client.enums;


/**
 * The Enum TestType.
 */
public enum TestType {

    /** The retire. */
    RETIRE(10, "Test Should be Removed, no longer in use"),

    /** The manual. */
    MANUAL(6, "Test is only used manually, not for automated use"),

    /** The pending. */
    PENDING(3, "Test is inprogress not ready to be run"),

    /** The automated. */
    AUTOMATED(0, "Test is ready to be Automatically Run"),

    /** The default. */
    DEFAULT(-1, "Used For test cases to take test plans test type"),

    /** The perf test. */
    PERF_PIPE(0, "Performance Test to be used in the pipeline");

    /** The priority. */
    final int    priority;
    
    /** The description. */
    final String description;

    /**
     * Instantiates a new test type.
     *
     * @param priority the priority
     * @param description the description
     */
    TestType(final int priority, final String description) {
        this.priority = priority;
        this.description = description;
    }

}
