package org.testah.framework.report;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

public class TestVerboseAssertNonTestCase {

    private VerboseAsserts verboseAsserts = new VerboseAsserts();

    private HashMap<Number, Number> getNumberData() {
        HashMap<Number, Number> numbers = new HashMap<>();
        numbers.put(3, 2);
        numbers.put(3.0, 2.0);
        numbers.put(3L, 2L);
        numbers.put(3f, 2f);
        return numbers;
    }

    @org.junit.Test
    public void testAssertGreaterThan() {
        getNumberData().forEach((key, value) -> {
            verboseAsserts.isGreaterThan("test " + key.getClass().getSimpleName(), key, value);
            verboseAsserts.isGreaterThanOrEqualTo("test " + key.getClass().getSimpleName(), key, key);
        });
    }

    @org.junit.Test()
    public void testAssertGreaterThanNegativeCase() {
        VerboseAsserts verboseAsserts = new VerboseAsserts();
        verboseAsserts.setThrowExceptionOnFail(false);
        getNumberData().forEach((key, value) -> {
            Assert.assertFalse("expecting to fail",
                    verboseAsserts.isGreaterThan("test " + key.getClass().getSimpleName(), value, key));
            Assert.assertFalse("expecting to fail",
                    verboseAsserts.isGreaterThan("test " + key.getClass().getSimpleName(), value, value));
        });
    }

    @org.junit.Test
    public void testAssertLessThan() {
        VerboseAsserts verboseAsserts = new VerboseAsserts();
        getNumberData().forEach((key, value) -> {
            verboseAsserts.isLessThan("test " + key.getClass().getSimpleName(), value, key);
            verboseAsserts.isLessThanOrEqualTo("test " + key.getClass().getSimpleName(), value, value);
        });
    }

    @Test()
    public void testAssertLessThanNegativeCase() {
        VerboseAsserts verboseAsserts = new VerboseAsserts();
        verboseAsserts.setThrowExceptionOnFail(false);
        getNumberData().forEach((key, value) -> {
            Assert.assertFalse("expecting to fail",
                    verboseAsserts.isLessThan("test " + key.getClass().getSimpleName(), key, value));
            Assert.assertFalse("expecting to fail",
                    verboseAsserts.isLessThan("test " + key.getClass().getSimpleName(), key, value));
        });
    }
}
