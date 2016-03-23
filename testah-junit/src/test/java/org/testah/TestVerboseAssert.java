package org.testah;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.Before;
import org.junit.Test;
import org.testah.framework.annotations.TestCase;
import org.testah.framework.annotations.TestPlan;
import org.testah.framework.testPlan.AbstractTestPlan;

@TestPlan()
public class TestVerboseAssert extends AbstractTestPlan {

    @Before
    public void setup() {

    }

    @TestCase()
    @Test
    public void testDontStoponFirstFail() throws JsonGenerationException, JsonMappingException, IOException {
        TS.verify().same("", "cool", "cool");
        TS.verify().same("", "cool", "cll");
        getTestPlan();
    }

    @TestCase()
    @Test(expected = AssertionError.class)
    public void testStopOnZFirstFail() throws JsonGenerationException, JsonMappingException, IOException {
        TS.asserts().setThrowExceptionOnFail(true);
        TS.asserts().same("", "cool", "cool");
        TS.asserts().same("", "cool", "cll");

    }

    @TestCase()
    @Test()
    public void testStopOnZFirstFail2() throws JsonGenerationException, JsonMappingException, IOException {
        TS.asserts().setThrowExceptionOnFail(false);
        TS.asserts().same("", "cool", "cool");
        TS.asserts().same("", "cool", "cll");
    }

    public void initlizeTest() {
        // TODO Auto-generated method stub

    }

    public void tearDownTest() {
        // TODO Auto-generated method stub

    }

    public void doOnFail() {
        // TODO Auto-generated method stub

    }

    public void doOnPass() {
        // TODO Auto-generated method stub

    }

}
