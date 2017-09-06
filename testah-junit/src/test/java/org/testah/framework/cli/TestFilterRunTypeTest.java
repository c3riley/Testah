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

public class TestFilterRunTypeTest {

    private Params paramsBeforeTest;

    @Before
    public void setUp() {
        paramsBeforeTest = TS.params();
        TS.setParams(new Params());
    }

    @Test
    public void TestFilterTestCaseRunType() {
        TestFilter filter = new TestFilter();
        TestCaseDto meta = new TestCaseDto().setRunTypes(new ArrayList<String>());
        TS.params().setFilterByTestType(null);
        meta.getRunTypes().add("TEST_RunType");

        TS.params().setFilterByRunType(null);
        Assert.assertEquals(true, filter.filterTestCase(meta, "TestWithRunTypeOnly"));

        TS.params().setFilterByRunType("");
        Assert.assertEquals(true, filter.filterTestCase(meta, "TestWithRunTypeOnly"));

        TS.params().setFilterByRunType("TEST_RunType");
        Assert.assertEquals(true, filter.filterTestCase(meta, "TestWithRunTypeOnly"));

        TS.params().setFilterByRunType("~TEST_RunType");
        Assert.assertEquals(false, filter.filterTestCase(meta, "TestWithRunTypeOnly"));

        TS.params().setFilterByRunType("Test1, test2, TEST_RunType");
        Assert.assertEquals(true, filter.filterTestCase(meta, "TestWithRunTypeOnly"));

        TS.params().setFilterByRunType("Test1, test2,~TEST_RunType");
        Assert.assertEquals(false, filter.filterTestCase(meta, "TestWithRunTypeOnly"));
    }

    @Test
    public void TestFilterTestPlanRunType() {

        testFilterMyRunType(1, 1, 0, 1, TestPlanWithRunType.class);

        testFilterMyRunType(1, 1, 0, 1, TestPlanWithManyRunTypes.class);

        testFilterMyRunType(5, 5, 3, 2, TestPlanWithRunType.class, TestPlanWithManyRunTypes.class,
                TestPlanWithRunTypeDefault.class, TestPlanWithRunTypeEmpty.class,
                TestPlanWithRunTypeEmptyString.class);

    }

    private void testFilterMyRunType(final int expectedTest1, final int expectedTest2, final int expectedTest3,
                                     final int expectedTest4, final Class<?>... classesToAdd) {
        TestFilter filter = new TestFilter();
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

        TS.params().setFilterByRunType("TEST_RunType");
        Assert.assertEquals(expectedTest4, filter.resetTestClassesMetFilters().filterTestPlansToRun(classes)
                .size());
    }

    @After
    public void tearDown() {
        TS.setParams(paramsBeforeTest);
    }

}

@TestPlan()
class TestPlanWithRunTypeDefault {

}

@TestPlan(runTypes = {})
class TestPlanWithRunTypeEmpty {
    @TestCase()
    public void test1() {
    }

    ;
}

@TestPlan(runTypes = {""})
class TestPlanWithRunTypeEmptyString {

    @TestCase()
    public void test1() {
    }

    ;

}

@TestPlan(runTypes = {"TEST_RunType"})
class TestPlanWithRunType {
    @TestCase()
    public void test1() {
    }

    ;

    @TestCase(runTypes = {"TEST_RunType"})
    public void test2() {
    }

    ;

}

@TestPlan(runTypes = {"TEST_RunType", "TEST_RunType1", "TEST_RunType2", "TEST_RunType3"})
class TestPlanWithManyRunTypes {
    @TestCase(runTypes = {})
    public void test1() {
    }

    ;

    @TestCase(runTypes = {""})
    public void test2() {
    }

    ;

    @TestCase(runTypes = {"TEST_RunType"})
    public void test3() {
    }

    ;

    @TestCase(runTypes = {"TEST_RunType", "TEST_RunType1", "TEST_RunType2", "TEST_RunType3"})
    public void test4() {
    }

    ;

    @TestCase()
    public void test5() {
    }

    ;

}