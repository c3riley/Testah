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

public class TestFilterComponentTest {

    private static final String TEST_WITH_COMPONENT_ONLY = "TestWithComponentOnly";
    private Params paramsBeforeTest;

    @Before
    public void setUp() {
        paramsBeforeTest = TS.params();
        TS.setParams(new Params());
    }

    @Test
    public void testFilterTestCaseComponent() {
        final TestFilter filter = new TestFilter();
        TestCaseDto meta = new TestCaseDto().setComponents(new ArrayList<String>());
        TS.params().setFilterByTestType(null);
        meta.getComponents().add("TEST_COMPONENT");

        TS.params().setFilterByComponent(null);
        Assert.assertEquals(true, filter.filterTestCase(meta, TEST_WITH_COMPONENT_ONLY));

        TS.params().setFilterByComponent("");
        Assert.assertEquals(true, filter.filterTestCase(meta, TEST_WITH_COMPONENT_ONLY));

        TS.params().setFilterByComponent("TEST_COMPONENT");
        Assert.assertEquals(true, filter.filterTestCase(meta, TEST_WITH_COMPONENT_ONLY));

        TS.params().setFilterByComponent("~TEST_COMPONENT");
        Assert.assertEquals(false, filter.filterTestCase(meta, TEST_WITH_COMPONENT_ONLY));

        TS.params().setFilterByComponent("Test1, test2, TEST_COMPONENT");
        Assert.assertEquals(true, filter.filterTestCase(meta, TEST_WITH_COMPONENT_ONLY));

        TS.params().setFilterByComponent("Test1, test2,~TEST_COMPONENT");
        Assert.assertEquals(false, filter.filterTestCase(meta, TEST_WITH_COMPONENT_ONLY));
    }

    @Test
    public void testFilterTestPlanComponent() {

        testFilterMyComponent(1, 1, 0, 1, TestPlanWithComponent.class);

        testFilterMyComponent(1, 1, 1, 1, TestPlanWithManyComponents.class);

        testFilterMyComponent(4, 4, 3, 2, TestPlanWithComponent.class, TestPlanWithManyComponents.class,
                TestPlanWithComponentDefault.class, TestPlanWithComponentEmpty.class,
                TestPlanWithComponentEmptyString.class);

    }

    private void testFilterMyComponent(final int expectedTest1, final int expectedTest2, final int expectedTest3,
                                       final int expectedTest4, final Class<?>... classesToAdd) {
        final TestFilter filter = new TestFilter();
        Set<Class<?>> classes = ImmutableSet.copyOf(Arrays.asList(classesToAdd));

        TS.params().setFilterByComponent(null);
        Assert.assertEquals(expectedTest1, filter.resetTestClassesMetFilters().filterTestPlansToRun(classes)
                .size());

        TS.params().setFilterByComponent("");
        Assert.assertEquals(expectedTest2, filter.resetTestClassesMetFilters().filterTestPlansToRun(classes)
                .size());

        TS.params().setFilterByComponent("~TEST_COMPONENT");
        Assert.assertEquals(expectedTest3, filter.resetTestClassesMetFilters().filterTestPlansToRun(classes)
                .size());

        TS.params().setFilterByComponent("TEST_COMPONENT");
        Assert.assertEquals(expectedTest4, filter.resetTestClassesMetFilters().filterTestPlansToRun(classes)
                .size());
    }

    @After
    public void tearDown() {
        TS.setParams(paramsBeforeTest);
    }

}
