package org.testah.framework.cli;

import com.google.common.collect.ImmutableSet;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.testah.TS;
import org.testah.client.dto.TestCaseDto;
import org.testah.client.enums.TestType;
import org.testah.framework.annotations.TestCase;
import org.testah.framework.annotations.TestPlan;
import org.testah.framework.testPlan.HttpTestPlan;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

@TestPlan
public class TestFilterTestTypeTest extends HttpTestPlan {
    private Params paramsBeforeTest;

    @Before
    public void setUp() {
        paramsBeforeTest = TS.params();
        TS.setParams(new Params());
    }

    @TestCase
    @Test
    public void testFilterTestCaseTestType() {
        TS.asserts().isTrue(true);
        TestFilter filter = new TestFilter();
        TestCaseDto meta = new TestCaseDto().setTestType(TestType.PENDING);

        TS.params().setFilterByTestType(null);
        Assert.assertEquals(true, filter.filterTestCase(meta, "TestWithTestTypeOnly"));

        for (TestType testType : TestType.values()) {
            TS.params().setFilterByTestType(testType);
            meta = new TestCaseDto().setTestType(testType);
            Assert.assertEquals(true, filter.filterTestCase(meta, "TestWithTestTypeOnly"));
            for (TestType testType2 : TestType.values()) {
                if (testType2 != testType) {
                    meta = new TestCaseDto().setTestType(testType2);
                    Assert.assertEquals(false, filter.filterTestCase(meta, testType.name() + "!=" + testType2.name()));
                } else {
                    meta = new TestCaseDto().setTestType(testType2);
                    Assert.assertEquals(true, filter.filterTestCase(meta, testType.name() + "=" + testType2.name()));
                }
            }

        }

    }

    @TestCase(testType = TestType.PENDING)
    @Test
    public void testShouldGetFilteredOut() {
        TS.asserts().isTrue(true);
        Assert.assertTrue("This testcase should always get filtered out from running!", false);
    }

    @TestCase
    @Test
    public void testFilterTestPlanTestType() {
        TS.asserts().isTrue(true);
        HashMap<TestType, Integer> expected = new HashMap<TestType, Integer>();
        expected.put(TestType.AUTOMATED, 1);
        expected.put(null, 1);
        testFilterMyTestType(expected, TestPlanWithTestType.class);

        expected.clear();
        expected.put(null, 1);
        expected.put(TestType.AUTOMATED, 1);
        testFilterMyTestType(expected, TestPlanWithManyTestTypes.class);

        expected.clear();
        expected.put(TestType.AUTOMATED, 3);
        expected.put(TestType.PENDING, 1);
        expected.put(TestType.DEFAULT, 0);
        expected.put(TestType.MANUAL, 2);
        expected.put(TestType.RETIRE, 1);
        expected.put(null, 7);
        testFilterMyTestType(expected, TestPlanWithTestType.class, TestPlanWithManyTestTypes.class,
            TestPlanWithTestTypeDefault.class, TestPlanWithTestTypeEmpty.class,
            TestPlanWithTestTypeEmptyString.class, TestPlanWithTestTypeRetire.class, TestPlanWithTestTypeManual.class);
    }

    private void testFilterMyTestType(final HashMap<TestType, Integer> expected, final Class<?>... classesToAdd) {
        TestFilter filter = new TestFilter();
        Set<Class<?>> classes = ImmutableSet.copyOf(Arrays.asList(classesToAdd));

        TS.params().setFilterByTestType(null);
        Assert.assertEquals(expected.getOrDefault(null, 0).intValue(),
            filter.resetTestClassesMetFilters().filterTestPlansToRun(classes).size());
        for (TestType testType : TestType.values()) {
            TS.params().setFilterByTestType(testType);
            Assert.assertEquals(expected.getOrDefault(testType, 0).intValue(),
                filter.resetTestClassesMetFilters().filterTestPlansToRun(classes).size());
        }
    }

    @After
    public void tearDown() {
        TS.setParams(paramsBeforeTest);
    }
}