package org.testah.framework.report.asserts;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.testah.framework.report.VerboseAsserts;

public class AssertNumberTest {

    VerboseAsserts negAsserts;

    @Before
    public void setUp() {

        negAsserts = new VerboseAsserts().onlyVerify();
    }

    @After
    public void tearDown() {

    }

    @Test
    public void equalsTo() {

        equalsTo(0,0);
        equalsTo(0.0,0.0);
        equalsTo(0.0f,0.0f);
        equalsTo(0l,0l);

        equalsTo(10,10);
        equalsTo(1230.0123,1230.0123);
        equalsTo(230.0f,230.0f);
        equalsTo(345450l,345450l);

        equalsTo(10,34,false, null);
        equalsTo(1230.0123,1231.0123,false, null);
        equalsTo(230.0f,230.0001f,false, null);
        equalsTo(345450l,345451l,false, null);

        equalsTo(10,34,true, 23);
        equalsTo(1230.0123,1231.0123,true, 1);
        equalsTo(230.0f,230.0001f,true, 1);
        equalsTo(345450l,345451l,true, 10);

    }

    private <T extends Number & Comparable<T>> boolean equalsTo(T expected, T actual) {
        return equalsTo(expected, actual, true, null);
    }
    private <T extends Number & Comparable<T>> boolean equalsTo(T expected, T actual, boolean expectedPassResult, Integer delta) {
        if(delta==null) {
            Assert.assertTrue(new AssertNumber<T>(actual, negAsserts)
                    .equalsTo(expected).isPassed() == expectedPassResult);
        } else {
            Assert.assertTrue(new AssertNumber<T>(actual, negAsserts)
                    .equalsTo(expected, delta).isPassed() == expectedPassResult);
        }
        return true;
    }
}
