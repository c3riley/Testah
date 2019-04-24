package org.testah.framework.report.asserts.base;

public class AssertNotAllowedWithNullActual extends AssertionError {

    private static final String DEFAULT_MESSAGE = "Unable to run the assert[%s] attempted, " +
            "an actual with null is not allowed";

    AssertNotAllowedWithNullActual(final String assertName) {
        super(String.format(DEFAULT_MESSAGE, assertName));
    }

}
