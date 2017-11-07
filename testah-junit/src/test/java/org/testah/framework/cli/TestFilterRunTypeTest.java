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

public class TestFilterRunTypeTest {

    private static final String TEST_RUN_TYPE = "TEST_RunType";
    private Params paramsBeforeTest;

    @Before
    public void setUp() {
        paramsBeforeTest = TS.params();
        TS.setParams(new Params());
    }

    @Test
    public void testFilterTestCaseRunType() {
        final TestFilter filter = new TestFilter();
        TestCaseDto meta = new TestCaseDto().setRunTypes(new ArrayList<String>());
        TS.params().setFilterByTestType(null);
        meta.getRunTypes().add(TEST_RUN_TYPE);

        TS.params().setFilterByRunType(null);
        String testCaseName = "TestWithRunTypeOnly";
        Assert.assertEquals(true, filter.filterTestCase(meta, testCaseName));

        TS.params().setFilterByRunType("");
        Assert.assertEquals(true, filter.filterTestCase(meta, testCaseName));

        TS.params().setFilterByRunType(TEST_RUN_TYPE);
        Assert.assertEquals(true, filter.filterTestCase(meta, testCaseName));

        TS.params().setFilterByRunType("~TEST_RunType");
        Assert.assertEquals(false, filter.filterTestCase(meta, testCaseName));

        TS.params().setFilterByRunType("Test1, test2, TEST_RunType");
        Assert.assertEquals(true, filter.filterTestCase(meta, testCaseName));

        TS.params().setFilterByRunType("Test1, test2,~TEST_RunType");
        Assert.assertEquals(false, filter.filterTestCase(meta, testCaseName));
    }

    @Test
    public void testFilterTestPlanRunType() {

        testFilterMyRunType(1, 1, 0, 1, TestPlanWithRunType.class);

        testFilterMyRunType(1, 1, 0, 1, TestPlanWithManyRunTypes.class);

        testFilterMyRunType(5, 5, 3, 2, TestPlanWithRunType.class, TestPlanWithManyRunTypes.class,
            TestPlanWithRunTypeDefault.class, TestPlanWithRunTypeEmpty.class,
            TestPlanWithRunTypeEmptyString.class);

    }

    private void testFilterMyRunType(final int expectedTest1, final int expectedTest2, final int expectedTest3,
                                     final int expectedTest4, final Class<?>... classesToAdd)
    {
        final TestFilter filter = new TestFilter();
        Set<Class<?>> classes = ImmutableSet.copyOf(Arrays.asList(classesToAdd));

        TS.params().setFilterByRunType(null);
        Assert.assertEquals(expectedTest1, filter.resetTestClassesMetFilters().filterTestPlansToRun(classes)
            .size());

        TS.params().setFilterByRunType("");
        Assert.assertEquals(expectedTest2, filter.resetTestClassesMetFilters().filterTestPlansToRun(classes)
            .size());

        TS.params().setFilterByRunType("~TEST_RunType");
        Assert.assertEquals(expectedTest3, filter.resetTestClassesMetFilters().filterTestPlansToRun(classes)
            .size());

        TS.params().setFilterByRunType(TEST_RUN_TYPE);
        Assert.assertEquals(expectedTest4, filter.resetTestClassesMetFilters().filterTestPlansToRun(classes)
            .size());
    }

    @After
    public void tearDown() {
        TS.setParams(paramsBeforeTest);
    }
}