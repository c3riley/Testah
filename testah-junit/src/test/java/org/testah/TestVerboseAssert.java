package org.testah;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.testah.framework.annotations.TestCase;
import org.testah.framework.annotations.TestPlan;
import org.testah.framework.testPlan.HttpTestPlan;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@TestPlan()
public class TestVerboseAssert extends HttpTestPlan {

    @Before
    public void setup() {

    }

    @Ignore
    @TestCase()
    @Test
    public void testDontStoponFirstFail() throws JsonGenerationException, JsonMappingException, IOException {
        TS.verify().same("", "cool", "cool");
        TS.verify().same("", "cool", "cll");
        getTestPlan();
    }

    @Ignore
    @TestCase()
    @Test(expected = AssertionError.class)
    public void testStopOnZFirstFail() throws JsonGenerationException, JsonMappingException, IOException {
        TS.asserts().setThrowExceptionOnFail(true);
        TS.asserts().same("", "cool", "cool");
        TS.asserts().same("", "cool", "cll");

    }

    @TestCase()
    @Test
    public void testIsEmpty() throws JsonGenerationException, JsonMappingException, IOException {
        TS.asserts().isEmpty("", new String[]{});
        TS.asserts().isEmpty("", new Integer[]{});
        TS.asserts().isEmpty("", new ArrayList<String>());
        TS.asserts().isEmpty("", new HashMap<String, String>());
        TS.asserts().isEmpty("", new HashMap<String, String>().keySet());

    }

}
