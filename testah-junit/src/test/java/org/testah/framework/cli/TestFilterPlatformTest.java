package org.testah.framework.cli;

import com.google.common.collect.ImmutableSet;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.testah.TS;
import org.testah.client.dto.TestCaseDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class TestFilterPlatformTest {

    private Params paramsBeforeTest;

    @Before
    public void setUp() {
        paramsBeforeTest = TS.params();
        TS.setParams(new Params());
    }

    @Test
    public void testFilterTestCasePlatform() {
        final TestFilter filter = new TestFilter();
        TestCaseDto meta = new TestCaseDto().setPlatforms(new ArrayList<String>());
        TS.params().setFilterByTestType(null);
        meta.getPlatforms().add("TEST_Platform");

        TS.params().setFilterByPlatform(null);
        String testCaseName = "TestWithPlatformOnly";
        Assert.assertEquals(true, filter.filterTestCase(meta, testCaseName));

        TS.params().setFilterByPlatform("");
        Assert.assertEquals(true, filter.filterTestCase(meta, testCaseName));

        TS.params().setFilterByPlatform("TEST_Platform");
        Assert.assertEquals(true, filter.filterTestCase(meta, testCaseName));

        TS.params().setFilterByPlatform("~TEST_Platform");
        Assert.assertEquals(false, filter.filterTestCase(meta, testCaseName));

        TS.params().setFilterByPlatform("Test1, test2, TEST_Platform");
        Assert.assertEquals(true, filter.filterTestCase(meta, testCaseName));

        TS.params().setFilterByPlatform("Test1, test2,~TEST_Platform");
        Assert.assertEquals(false, filter.filterTestCase(meta, testCaseName));
    }

    @Test
    public void testFilterTestPlanPlatform() {

        testFilterMyPlatform(1, 1, 0, 1, TestPlanWithPlatform.class);
        testFilterMyPlatform(1, 1, 0, 1, TestPlanWithManyPlatforms.class);
        testFilterMyPlatform(5, 5, 3, 2, TestPlanWithPlatform.class, TestPlanWithManyPlatforms.class,
                TestPlanWithPlatformDefault.class, TestPlanWithPlatformEmpty.class,
                TestPlanWithPlatformEmptyString.class);

    }

    private void testFilterMyPlatform(final int expectedTest1, final int expectedTest2, final int expectedTest3,
                                      final int expectedTest4, final Class<?>... classesToAdd) {
        final TestFilter filter = new TestFilter();
        Set<Class<?>> classes = ImmutableSet.copyOf(Arrays.asList(classesToAdd));

        TS.params().setFilterByPlatform(null);
        Assert.assertEquals("Test 1", expectedTest1, filter.resetTestClassesMetFilters().filterTestPlansToRun(classes)
                .size());
        TS.params().setFilterByPlatform("");
        Assert.assertEquals("Test 2", expectedTest2, filter.resetTestClassesMetFilters().filterTestPlansToRun(classes)
                .size());
        TS.params().setFilterByPlatform("~TEST_Platform");
        Assert.assertEquals("Test 3", expectedTest3, filter.resetTestClassesMetFilters().filterTestPlansToRun(classes)
                .size());
        TS.params().setFilterByPlatform("TEST_Platform");
        Assert.assertEquals("Test 4", expectedTest4, filter.resetTestClassesMetFilters().filterTestPlansToRun(classes)
                .size());
    }

    @After
    public void tearDown() {
        TS.setParams(paramsBeforeTest);
    }
}
