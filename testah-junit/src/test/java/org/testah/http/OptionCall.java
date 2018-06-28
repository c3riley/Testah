package org.testah.http;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.testah.TS;
import org.testah.driver.http.requests.OptionRequestDto;
import org.testah.framework.annotations.TestCase;
import org.testah.framework.annotations.TestPlan;
import org.testah.framework.testPlan.HttpTestPlan;

public class OptionCall {

    @Test
    public void test() throws Exception {

        TS.http().addCustomTestHeader("option-test").doRequest(new OptionRequestDto("http://www.testah.com")).assertStatus();

    }

    @TestPlan
    public static class TestFailingInBeforeMethod extends HttpTestPlan {

        @Before
        public void setUp() {
            throw new RuntimeException("Failed in Before");
        }

        @Ignore
        @TestCase
        @Test
        public void test() {
            TS.asserts().isTrue(true);
        }
    }
}
