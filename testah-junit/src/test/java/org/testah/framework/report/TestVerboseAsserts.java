package org.testah.framework.report;

import static org.hamcrest.Matchers.lessThanOrEqualTo;

import org.junit.Test;
import org.testah.TS;

public class TestVerboseAsserts {

    private static final String TEST_MESSAGE = "test message";
    private static final Double doubleObject = Double.valueOf(0.13);
    private static final double doublePrimitive = 17.1;
    private static final int intPrimitive = 111;

    @Test
    public void testAssertThatMatcherWithMessage() {
        TS.asserts().assertThat(TEST_MESSAGE, doubleObject, lessThanOrEqualTo(doubleObject));
        TS.asserts().assertThat(TEST_MESSAGE, doublePrimitive, lessThanOrEqualTo(doublePrimitive));
        TS.asserts().assertThat(TEST_MESSAGE, intPrimitive, lessThanOrEqualTo(intPrimitive));
    }

    @Test
    public void testAssertThatMatcher() {
        TS.asserts().assertThat(doubleObject, lessThanOrEqualTo(doubleObject));
        TS.asserts().assertThat(doublePrimitive, lessThanOrEqualTo(doublePrimitive));
        TS.asserts().assertThat(intPrimitive, lessThanOrEqualTo(intPrimitive));
    }

    @Test
    public void testThatMatcherWithMessage() {
        TS.asserts().that(TEST_MESSAGE, doubleObject, lessThanOrEqualTo(doubleObject));
        TS.asserts().that(TEST_MESSAGE, doublePrimitive, lessThanOrEqualTo(doublePrimitive));
        TS.asserts().that(TEST_MESSAGE, intPrimitive, lessThanOrEqualTo(intPrimitive));
    }

    @Test
    public void testThatMatcher() {
        TS.asserts().that(doubleObject, lessThanOrEqualTo(doubleObject));
        TS.asserts().that(doublePrimitive, lessThanOrEqualTo(doublePrimitive));
        TS.asserts().that(intPrimitive, lessThanOrEqualTo(intPrimitive));
    }
}
