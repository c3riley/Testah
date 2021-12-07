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

public class TestFilterTagTest {

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
        TestCaseDto meta = new TestCaseDto().setTags(new ArrayList<String>());
        TS.params().setFilterByTestType(null);
        meta.getTags().add("TEST_Tag");

        TS.params().setFilterByTag(null);
        Assert.assertEquals(true, filter.filterTestCase(meta, TEST_WITH_TAG_ONLY));

        TS.params().setFilterByTag("");
        Assert.assertEquals(true, filter.filterTestCase(meta, TEST_WITH_TAG_ONLY));

        TS.params().setFilterByTag("TEST_Tag");
        Assert.assertEquals(true, filter.filterTestCase(meta, TEST_WITH_TAG_ONLY));

        TS.params().setFilterByTag("~TEST_Tag");
        Assert.assertEquals(false, filter.filterTestCase(meta, TEST_WITH_TAG_ONLY));

        TS.params().setFilterByTag("Test1, test2, TEST_Tag");
        Assert.assertEquals(true, filter.filterTestCase(meta, TEST_WITH_TAG_ONLY));

        TS.params().setFilterByTag("Test1, test2,~TEST_Tag");
        Assert.assertEquals(false, filter.filterTestCase(meta, TEST_WITH_TAG_ONLY));
    }

    @Test
    public void testFilterTestPlanTag() {

        testFilterMyTag(1, 1, 0, 1, TestPlanWithTag.class);

        testFilterMyTag(1, 1, 1, 1, TestPlanWithManyTags.class);

        testFilterMyTag(4, 4, 3, 2, TestPlanWithTag.class, TestPlanWithManyTags.class,
                TestPlanWithTagDefault.class, TestPlanWithTagEmpty.class,
                TestPlanWithTagEmptyString.class);

    }

    private void testFilterMyTag(final int expectedTest1, final int expectedTest2, final int expectedTest3,
                                 final int expectedTest4, final Class<?>... classesToAdd) {
        final TestFilter filter = new TestFilter();
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
