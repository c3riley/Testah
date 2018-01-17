package org.testah.framework.cli;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.testah.TS;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;



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
        assertThat(filter.resetTestClassesMetFilters().filterTestPlansToRun()
                .size(), is(greaterThanOrEqualTo(30)));

        TS.params().setFilterByTestPlanNameStartsWith("org.");
        assertThat(filter.resetTestClassesMetFilters().filterTestPlansToRun()
                .size(), is(greaterThanOrEqualTo(48)));

        TS.params().setFilterByTestPlanNameStartsWith("org.testah.framework.cli");
        assertThat(filter.resetTestClassesMetFilters().filterTestPlansToRun()
                .size(), is(greaterThanOrEqualTo(30)));

        TS.params().setFilterByTestPlanNameStartsWith("TestPlanWith");
        assertThat(filter.resetTestClassesMetFilters().filterTestPlansToRun()
                .size(), is(greaterThanOrEqualTo(28)));


        TS.params().setFilterByTestPlanNameStartsWith("Test");
        assertThat(filter.resetTestClassesMetFilters().filterTestPlansToRun()
                .size(), is(greaterThanOrEqualTo(28)));

        TS.params().setFilterByTestPlanNameStartsWith("zNoTest");
        assertThat(filter.resetTestClassesMetFilters().filterTestPlansToRun()
                .size(), is(equalTo(0)));

    }

    @Test
    public void testFilterTestPlanName() {

        testFilterName(1, 1, 0, 1, TestPlanWithTag.class);
        testFilterName(1, 1, 0, 1, TestPlanWithTag.class);

        testFilterName(1, 1, 0, 1, TestPlanWithManyTags.class);

        testFilterName(4, 4, 4, 4, TestPlanWithTag.class, TestPlanWithManyTags.class,
                TestPlanWithTagDefault.class, TestPlanWithTagEmpty.class,
                TestPlanWithTagEmptyString.class, CliTest.class);

    }

    private void testFilterName(final int expectedTest1, final int expectedTest2, final int expectedTest3,
                                final int expectedTest4, final Class<?>... classesToAdd) {
        final TestFilter filter = new TestFilter();
        Set<Class<?>> classes = ImmutableSet.copyOf(Arrays.asList(classesToAdd));
        List<Class<?>> classesList = ImmutableList.copyOf(Arrays.asList(classesToAdd));

        Class<?> firstClass = classesList.get(0);
        Class<?> secondClass = classesList.size() > 1 ? classesList.get(1) : null;

        TS.params().setFilterByTestPlanNameStartsWith(firstClass.getName());
        Assert.assertEquals(expectedTest1, filter.resetTestClassesMetFilters().filterTestPlansToRun(classes)
                .size());

        TS.params().setFilterByTestPlanNameStartsWith(firstClass.getSimpleName());
        Assert.assertEquals(expectedTest2, filter.resetTestClassesMetFilters().filterTestPlansToRun(classes)
                .size());

        if (null != secondClass) {
            TS.params().setFilterByTestPlanNameStartsWith(firstClass.getName() + ", " + secondClass.getName());
            Assert.assertEquals(expectedTest3, filter.resetTestClassesMetFilters().filterTestPlansToRun(classes)
                    .size());

            TS.params().setFilterByTestPlanNameStartsWith(firstClass.getSimpleName() + ", " + secondClass.getSimpleName());
            Assert.assertEquals(expectedTest4, filter.resetTestClassesMetFilters().filterTestPlansToRun(classes)
                    .size());

            TS.params().setFilterByTestPlanNameStartsWith(firstClass.getName() + ", " + secondClass.getSimpleName());
            Assert.assertEquals(expectedTest4, filter.resetTestClassesMetFilters().filterTestPlansToRun(classes)
                    .size());
        }
    }

    @After
    public void tearDown() {
        TS.setParams(paramsBeforeTest);
    }
}