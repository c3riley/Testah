package org.testah.framework.report.asserts;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.testah.framework.report.VerboseAsserts;

import java.math.BigDecimal;

public class AssertBigDecimalTest {

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
        BigDecimal equal = AssertBigDecimal.getBigDecimal("1.00");
        BigDecimal diff = AssertBigDecimal.getBigDecimal("1.00001");
        Assert.assertTrue(new AssertBigDecimal(equal, negAsserts).equalsTo(equal).isPassed());
        Assert.assertTrue(new AssertBigDecimal(equal, negAsserts).equalsTo(diff).isFailed());
        Assert.assertTrue(new AssertBigDecimal(diff, negAsserts).equalsTo(diff).isPassed());
    }

    @Test
    public void shouldNotHaveDecimal() {
        Assert.assertTrue(new AssertBigDecimal(new BigDecimal("0"), negAsserts).shouldNotHaveDecimalValue().isPassed());
        Assert.assertTrue(new AssertBigDecimal(new BigDecimal("10"), negAsserts).shouldNotHaveDecimalValue().isPassed());
        Assert.assertTrue(new AssertBigDecimal(new BigDecimal("1323332534250"), negAsserts).shouldNotHaveDecimalValue().isPassed());
        Assert.assertTrue(new AssertBigDecimal(new BigDecimal("1.0"), negAsserts).shouldNotHaveDecimalValue().isPassed());
        Assert.assertTrue(new AssertBigDecimal(new BigDecimal("1."), negAsserts).shouldNotHaveDecimalValue().isPassed());
        Assert.assertTrue(new AssertBigDecimal(new BigDecimal(".03"), negAsserts).shouldNotHaveDecimalValue().isFailed());
    }

    @Test
    public void shouldHaveDecimal() {
        Assert.assertTrue(new AssertBigDecimal(new BigDecimal("0"), negAsserts).shouldHaveDecimalValue().isFailed());
        Assert.assertTrue(new AssertBigDecimal(new BigDecimal("10"), negAsserts).shouldHaveDecimalValue().isFailed());
        Assert.assertTrue(new AssertBigDecimal(new BigDecimal("1323332534250"), negAsserts).shouldHaveDecimalValue().isFailed());
        Assert.assertTrue(new AssertBigDecimal(new BigDecimal("1.0"), negAsserts).shouldHaveDecimalValue().isPassed());
        Assert.assertTrue(new AssertBigDecimal(new BigDecimal("1."), negAsserts).shouldHaveDecimalValue().isFailed());
        Assert.assertTrue(new AssertBigDecimal(new BigDecimal(".03"), negAsserts).shouldHaveDecimalValue().isPassed());
    }

    @Test
    public void isWithinRange() {
        Assert.assertTrue(new AssertBigDecimal(new BigDecimal("1.2"), negAsserts)
                .isWithinRange(0,2,false).isPassed());
        Assert.assertTrue(new AssertBigDecimal(new BigDecimal("2"), negAsserts)
                .isWithinRange(0,2,true).isPassed());
        Assert.assertTrue(new AssertBigDecimal(new BigDecimal("0.0"), negAsserts)
                .isWithinRange(0,2,true).isPassed());
        Assert.assertTrue(new AssertBigDecimal(new BigDecimal("1"), negAsserts)
                .isWithinRange(0,2,false).isPassed());
        Assert.assertTrue(new AssertBigDecimal(new BigDecimal("1"), negAsserts)
                .isWithinRange(0L,2l,false).isPassed());
        Assert.assertTrue(new AssertBigDecimal(new BigDecimal("0.4"), negAsserts)
                .isWithinRange(0.3,0.5,false).isPassed());
        Assert.assertTrue(new AssertBigDecimal(new BigDecimal("0.4"), negAsserts)
                .isWithinRange(0.3f,0.5f,false).isPassed());
        Assert.assertTrue(new AssertBigDecimal(new BigDecimal("1"), negAsserts)
                .isWithinRange(0,2,false).isPassed());


        Assert.assertTrue(new AssertBigDecimal(new BigDecimal("2.2"), negAsserts)
                .isWithinRange(0,2,false).isFailed());
        Assert.assertTrue(new AssertBigDecimal(new BigDecimal("2.00002"), negAsserts)
                .isWithinRange(0,2,true).isFailed());
        Assert.assertTrue(new AssertBigDecimal(new BigDecimal("-0.1"), negAsserts)
                .isWithinRange(0,2,true).isFailed());
        Assert.assertTrue(new AssertBigDecimal(new BigDecimal("2"), negAsserts)
                .isWithinRange(0,2,false).isFailed());
        Assert.assertTrue(new AssertBigDecimal(new BigDecimal("4"), negAsserts)
                .isWithinRange(0L,2l,false).isFailed());
        Assert.assertTrue(new AssertBigDecimal(new BigDecimal("0.66"), negAsserts)
                .isWithinRange(0.3,0.5,false).isFailed());
        Assert.assertTrue(new AssertBigDecimal(new BigDecimal("0.75534532"), negAsserts)
                .isWithinRange(0.3f,0.5f,false).isFailed());
        Assert.assertTrue(new AssertBigDecimal(new BigDecimal("545423646435"), negAsserts)
                .isWithinRange(0,2,false).isFailed());


    }


    @SuppressWarnings("BC_VACUOUS_INSTANCEOF")
    @Test
    public void getBigDecimal() {

        BigDecimal expected = new BigDecimal("1");
        Assert.assertTrue(AssertBigDecimal.getBigDecimal(1).compareTo(expected)==0);
        Assert.assertTrue(AssertBigDecimal.getBigDecimal(1f).compareTo(expected)==0);
        Assert.assertTrue(AssertBigDecimal.getBigDecimal(1l).compareTo(expected)==0);
        Assert.assertTrue(AssertBigDecimal.getBigDecimal(1.0).compareTo(expected)==0);
        Assert.assertTrue(AssertBigDecimal.getBigDecimal("1").compareTo(expected)==0);
        Assert.assertTrue(AssertBigDecimal.getBigDecimal("") == null);
        Assert.assertTrue(AssertBigDecimal.getBigDecimal("test") == null);
        Assert.assertTrue(AssertBigDecimal.getBigDecimal("1c") == null);

    }

}