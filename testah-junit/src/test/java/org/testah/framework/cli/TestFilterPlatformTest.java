package org.testah.framework.cli;

import com.google.common.collect.ImmutableSet;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.testah.TS;
import org.testah.client.dto.TestCaseDto;
import org.testah.framework.annotations.TestCase;
import org.testah.framework.annotations.TestPlan;

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
        TestFilter filter = new TestFilter();
        TestCaseDto meta = new TestCaseDto().setPlatforms(new ArrayList<String>());
        TS.params().setFilterByTestType(null);
        meta.getPlatforms().add("TEST_Platform");

        TS.params().setFilterByPlatform(null);
        Assert.assertEquals(true, filter.filterTestCase(meta, "TestWithPlatformOnly"));

        TS.params().setFilterByPlatform("");
        Assert.assertEquals(true, filter.filterTestCase(meta, "TestWithPlatformOnly"));

        TS.params().setFilterByPlatform("TEST_Platform");
        Assert.assertEquals(true, filter.filterTestCase(meta, "TestWithPlatformOnly"));

        TS.params().setFilterByPlatform("~TEST_Platform");
        Assert.assertEquals(false, filter.filterTestCase(meta, "TestWithPlatformOnly"));

        TS.params().setFilterByPlatform("Test1, test2, TEST_Platform");
        Assert.assertEquals(true, filter.filterTestCase(meta, "TestWithPlatformOnly"));

        TS.params().setFilterByPlatform("Test1, test2,~TEST_Platform");
        Assert.assertEquals(false, filter.filterTestCase(meta, "TestWithPlatformOnly"));
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
        TestFilter filter = new TestFilter();
        Set<Class<?>> classes = ImmutableSet.copyOf(Arrays.asList(classesToAdd));

        TS.params().setFilterByPlatform(null);
        Assert.assertEquals(expectedTest1, filter.resetTestClassesMetFilters().filterTestPlansToRun(classes)
                .size());

        TS.params().setFilterByPlatform("");
        Assert.assertEquals(expectedTest2, filter.resetTestClassesMetFilters().filterTestPlansToRun(classes)
                .size());

        TS.params().setFilterByPlatform("~TEST_Platform");
        Assert.assertEquals(expectedTest3, filter.resetTestClassesMetFilters().filterTestPlansToRun(classes)
                .size());

        TS.params().setFilterByPlatform("TEST_Platform");
        Assert.assertEquals(expectedTest4, filter.resetTestClassesMetFilters().filterTestPlansToRun(classes)
                .size());
    }

    @After
    public void tearDown() {
        TS.setParams(paramsBeforeTest);
    }

}

@TestPlan()
class TestPlanWithPlatformDefault {

}

@TestPlan(platforms = {})
class TestPlanWithPlatformEmpty {
    @TestCase()
    public void test1() {
    }

    ;
}

@TestPlan(platforms = {""})
class TestPlanWithPlatformEmptyString {

    @TestCase()
    public void test1() {
    }

    ;

}

@TestPlan(platforms = {"TEST_Platform"})
class TestPlanWithPlatform {
    @TestCase()
    public void test1() {
    }

    ;

    @TestCase(platforms = {"TEST_Platform"})
    public void test2() {
    }

    ;

}

@TestPlan(platforms = {"TEST_Platform", "TEST_Platform1", "TEST_Platform2", "TEST_Platform3"})
class TestPlanWithManyPlatforms {
    @TestCase(platforms = {})
    public void test1() {
    }

    ;

    @TestCase(platforms = {""})
    public void test2() {
    }

    ;

    @TestCase(platforms = {"TEST_Platform"})
    public void test3() {
    }

    ;

    @TestCase(platforms = {"TEST_Platform", "TEST_Platform1", "TEST_Platform2", "TEST_Platform3"})
    public void test4() {
    }

    ;

    @TestCase()
    public void test5() {
    }

    ;

}