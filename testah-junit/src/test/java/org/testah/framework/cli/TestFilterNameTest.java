package org.testah.framework.cli;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.testah.TS;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class TestFilterNameTest {

    private static final String TEST_WITH_TAG_ONLY = "TestWithTagOnly";
    private Params paramsBeforeTest;

    @Before
    public void setUp() {
        paramsBeforeTest = TS.params();
        TS.setParams(new Params());
    }

    @Test
    public void testFilterTestCaseTag() {
        final TestFilter filter = new TestFilter();

        TS.params().setFilterByTestPlanNameStartsWith("TestPlanWith, TestFilterTestTypeTest, TestResultIgnoredIfNoAssertsFound");
        MatcherAssert.assertThat(filter.resetTestClassesMetFilters().filterTestPlansToRun()
                .size(), Matchers.is(Matchers.greaterThanOrEqualTo(30)));

        TS.params().setFilterByTestPlanNameStartsWith("org.");
        MatcherAssert.assertThat(filter.resetTestClassesMetFilters().filterTestPlansToRun()
                .size(), Matchers.is(Matchers.greaterThanOrEqualTo(48)));

        TS.params().setFilterByTestPlanNameStartsWith("org.testah.framework.cli");
        MatcherAssert.assertThat(filter.resetTestClassesMetFilters().filterTestPlansToRun()
                .size(), Matchers.is(Matchers.greaterThanOrEqualTo(30)));

        TS.params().setFilterByTestPlanNameStartsWith("TestPlanWith");
        MatcherAssert.assertThat(filter.resetTestClassesMetFilters().filterTestPlansToRun()
                .size(), Matchers.is(Matchers.greaterThanOrEqualTo(28)));

        TS.params().setFilterByTestPlanNameStartsWith("Test");
        MatcherAssert.assertThat(filter.resetTestClassesMetFilters().filterTestPlansToRun()
                .size(), Matchers.is(Matchers.greaterThanOrEqualTo(28)));

        TS.params().setFilterByTestPlanNameStartsWith("zNoTest");
        MatcherAssert.assertThat(filter.resetTestClassesMetFilters().filterTestPlansToRun()
                .size(), Matchers.is(Matchers.equalTo(0)));

    }

    @Test
    public void testFilterTestPlanName() {

        testFilterName(1, 1, 0, 1, TestPlanWithTag.class);
        testFilterName(1, 1, 0, 1, TestPlanWithTag.class);

        testFilterName(1, 1, 0, 1, TestPlanWithManyTags.class);

        testFilterName(4, 4, 5, 5, TestPlanWithTag.class, TestPlanWithManyTags.class,
                TestPlanWithTagDefault.class, TestPlanWithTagEmpty.class,
                TestPlanWithTagEmptyString.class, CliTest.class);

    }

    private void testFilterName(final int expectedTest1, final int expectedTest2, final int expectedTest3,
                                final int expectedTest4, final Class<?>... classesToAdd) {
        final TestFilter filter = new TestFilter();
        Set<Class<?>> classes = ImmutableSet.copyOf(Arrays.asList(classesToAdd));
        List<Class<?>> classesList = ImmutableList.copyOf(Arrays.asList(classesToAdd));

        Class<?> firstClass = classesList.get(0);


        TS.params().setFilterByTestPlanNameStartsWith(firstClass.getCanonicalName());
        Assert.assertEquals(expectedTest1, filter.resetTestClassesMetFilters().filterTestPlansToRun(classes)
                .size());

        TS.params().setFilterByTestPlanNameStartsWith(firstClass.getSimpleName());
        Assert.assertEquals(expectedTest2, filter.resetTestClassesMetFilters().filterTestPlansToRun(classes)
                .size());

        Class<?> secondClass = classesList.size() > 1 ? classesList.get(1) : null;
        if (null != secondClass) {
            TS.params().setFilterByTestPlanNameStartsWith(firstClass.getCanonicalName() + ", " + secondClass.getName());
            Assert.assertEquals(expectedTest3, filter.resetTestClassesMetFilters().filterTestPlansToRun(classes)
                    .size());

            TS.params().setFilterByTestPlanNameStartsWith(firstClass.getSimpleName() + ", " + secondClass.getSimpleName());
            Assert.assertEquals(expectedTest4, filter.resetTestClassesMetFilters().filterTestPlansToRun(classes)
                    .size());
            TS.log().info(firstClass.getCanonicalName() + ", " + secondClass.getSimpleName());
            TS.params().setFilterByTestPlanNameStartsWith(firstClass.getCanonicalName() + ", " + secondClass.getSimpleName());
            Assert.assertEquals(expectedTest4, filter.resetTestClassesMetFilters().filterTestPlansToRun(classes)
                    .size());
        }
    }

    @After
    public void tearDown() {
        TS.setParams(paramsBeforeTest);
    }
}