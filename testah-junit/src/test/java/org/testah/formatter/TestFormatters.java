package org.testah.formatter;

import org.junit.Test;
import org.testah.TS;
import org.testah.framework.annotations.TestCase;
import org.testah.framework.annotations.TestPlan;
import org.testah.framework.testPlan.HttpTestPlan;

@TestPlan
public class TestFormatters extends HttpTestPlan {

    @Test
    @TestCase
    public void test1() {
        TS.asserts().isNull(null);
    }

}
