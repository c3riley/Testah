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

public class TestFilterTagTest {

    private Params paramsBeforeTest;

    @Before
    public void setUp() {
        paramsBeforeTest = TS.params();
        TS.setParams(new Params());
    }

    @Test
    public void TestFilterTestCaseTag() {
        TestFilter filter = new TestFilter();
        TestCaseDto meta = new TestCaseDto().setTags(new ArrayList<String>());
        TS.params().setFilterByTestType(null);
        meta.getTags().add("TEST_Tag");

        TS.params().setFilterByTag(null);
        Assert.assertEquals(true, filter.filterTestCase(meta, "TestWithTagOnly"));

        TS.params().setFilterByTag("");
        Assert.assertEquals(true, filter.filterTestCase(meta, "TestWithTagOnly"));

        TS.params().setFilterByTag("TEST_Tag");
        Assert.assertEquals(true, filter.filterTestCase(meta, "TestWithTagOnly"));

        TS.params().setFilterByTag("~TEST_Tag");
        Assert.assertEquals(false, filter.filterTestCase(meta, "TestWithTagOnly"));

        TS.params().setFilterByTag("Test1, test2, TEST_Tag");
        Assert.assertEquals(true, filter.filterTestCase(meta, "TestWithTagOnly"));

        TS.params().setFilterByTag("Test1, test2,~TEST_Tag");
        Assert.assertEquals(false, filter.filterTestCase(meta, "TestWithTagOnly"));
    }

    @Test
    public void TestFilterTestPlanTag() {

        testFilterMyTag(1, 1, 0, 1, TestPlanWithTag.class);

        testFilterMyTag(1, 1, 0, 1, TestPlanWithManyTags.class);

        testFilterMyTag(5, 5, 3, 2, TestPlanWithTag.class, TestPlanWithManyTags.class,
                TestPlanWithTagDefault.class, TestPlanWithTagEmpty.class,
                TestPlanWithTagEmptyString.class);

    }

    private void testFilterMyTag(final int expectedTest1, final int expectedTest2, final int expectedTest3,
                                 final int expectedTest4, final Class<?>... classesToAdd) {
        TestFilter filter = new TestFilter();
        Set<Class<?>> classes = ImmutableSet.copyOf(Arrays.asList(classesToAdd));

        TS.params().setFilterByTag(null);
        Assert.assertEquals(expectedTest1, filter.resetTestClassesMetFilters().filterTestPlansToRun(classes)
                .size());

        TS.params().setFilterByTag("");
        Assert.assertEquals(expectedTest2, filter.resetTestClassesMetFilters().filterTestPlansToRun(classes)
                .size());

        TS.params().setFilterByTag("~TEST_Tag");
        Assert.assertEquals(expectedTest3, filter.resetTestClassesMetFilters().filterTestPlansToRun(classes)
                .size());

        TS.params().setFilterByTag("TEST_Tag");
        Assert.assertEquals(expectedTest4, filter.resetTestClassesMetFilters().filterTestPlansToRun(classes)
                .size());
    }

    @After
    public void tearDown() {
        TS.setParams(paramsBeforeTest);
    }

}

@TestPlan()
class TestPlanWithTagDefault {

}

@TestPlan(tags = {})
class TestPlanWithTagEmpty {
    @TestCase()
    public void test1() {
    }

    ;
}

@TestPlan(tags = {""})
class TestPlanWithTagEmptyString {

    @TestCase()
    public void test1() {
    }

    ;

}

@TestPlan(tags = {"TEST_Tag"})
class TestPlanWithTag {
    @TestCase()
    public void test1() {
    }

    ;

    @TestCase(tags = {"TEST_Tag"})
    public void test2() {
    }

    ;

}

@TestPlan(tags = {"TEST_Tag", "TEST_Tag1", "TEST_Tag2", "TEST_Tag3"})
class TestPlanWithManyTags {
    @TestCase(tags = {})
    public void test1() {
    }

    ;

    @TestCase(tags = {""})
    public void test2() {
    }

    ;

    @TestCase(tags = {"TEST_Tag"})
    public void test3() {
    }

    ;

    @TestCase(tags = {"TEST_Tag", "TEST_Tag1", "TEST_Tag2", "TEST_Tag3"})
    public void test4() {
    }

    ;

    @TestCase()
    public void test5() {
    }

    ;

}