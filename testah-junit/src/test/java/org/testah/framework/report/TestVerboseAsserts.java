package org.testah.framework.report;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.Matchers.lessThanOrEqualTo;

public class TestVerboseAsserts {

    private static final String TEST_MESSAGE = "test message";
    private static final Double doubleObject = Double.valueOf(0.13);
    private static final double doublePrimitive = 17.1;
    private static final int intPrimitive = 111;
    private VerboseAsserts verboseAsserts = new VerboseAsserts();


    @Test
    public void testAssertThatMatcherWithMessage() {
        verboseAsserts.assertThat(TEST_MESSAGE, doubleObject, lessThanOrEqualTo(doubleObject));
        verboseAsserts.assertThat(TEST_MESSAGE, doublePrimitive, lessThanOrEqualTo(doublePrimitive));
        verboseAsserts.assertThat(TEST_MESSAGE, intPrimitive, lessThanOrEqualTo(intPrimitive));
    }

    @Test
    public void testAssertThatMatcher() {
        verboseAsserts.assertThat(doubleObject, lessThanOrEqualTo(doubleObject));
        verboseAsserts.assertThat(doublePrimitive, lessThanOrEqualTo(doublePrimitive));
        verboseAsserts.assertThat(intPrimitive, lessThanOrEqualTo(intPrimitive));
    }

    @Test
    public void testThatMatcherWithMessage() {
        verboseAsserts.that(TEST_MESSAGE, doubleObject, lessThanOrEqualTo(doubleObject));
        verboseAsserts.that(TEST_MESSAGE, doublePrimitive, lessThanOrEqualTo(doublePrimitive));
        verboseAsserts.that(TEST_MESSAGE, intPrimitive, lessThanOrEqualTo(intPrimitive));
    }

    @Test
    public void testThatMatcher() {
        verboseAsserts.that(doubleObject, lessThanOrEqualTo(doubleObject));
        verboseAsserts.that(doublePrimitive, lessThanOrEqualTo(doublePrimitive));
        verboseAsserts.that(intPrimitive, lessThanOrEqualTo(intPrimitive));
    }

    @Test
    public void testCustomAssertPass() {
        verboseAsserts.customAssert(() -> {
            Assert.assertTrue("expecting to pass", true);
        });

        verboseAsserts.customAssert("custom assert desc", () -> {
            Assert.assertTrue("expecting to pass", true);
        });
    }

    @Test(expected = AssertionError.class)
    public void testCustomAssertFail() {
        verboseAsserts.customAssert(() -> {
            Assert.assertTrue("expecting to fail", false);
        });
    }

    @Test(expected = AssertionError.class)
    public void testCustomAssertFailWithMessage() {
        verboseAsserts.customAssert("custom assert desc", () -> {
            Assert.assertTrue("expecting to fail", false);
        });
    }

}
