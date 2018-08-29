package org.testah.framework.report.asserts;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testah.framework.report.VerboseAsserts;

import java.util.HashMap;
import java.util.Map;

class AssertMapsTest {

    VerboseAsserts negAsserts;

    @BeforeEach
    void setUp() {
        negAsserts = new VerboseAsserts().onlyVerify();
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    public void testKeys() {
        Map<String, String> data = new HashMap<>();
        Assert.assertTrue( new AssertMaps<String, String>(data, negAsserts).keySet()
                .size().equalsTo(0).isPassed());
    }

    @Test
    public void testValues() {
        Map<String, String> data = new HashMap<>();
        Assert.assertTrue( new AssertMaps<String, String>(data, negAsserts).values()
                .size().equalsTo(0).isPassed());
    }

    @Test
    public void isEmptyTest() {
        Map<String, String> data = new HashMap<>();
        Assert.assertTrue( new AssertMaps<String, String>(data, negAsserts).isEmpty().isPassed());
    }

    @Test
    public void isNotEmptyTest() {
        Map<String, String> data = new HashMap<>();
        Assert.assertTrue( new AssertMaps<String, String>(data, negAsserts).isNotEmpty().isFailed());
    }

}