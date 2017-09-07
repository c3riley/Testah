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

public class TestFilterDeviceTest {

    private Params paramsBeforeTest;

    @Before
    public void setUp() {
        paramsBeforeTest = TS.params();
        TS.setParams(new Params());
    }

    @Test
    public void TestFilterTestCaseDevice() {
        TestFilter filter = new TestFilter();
        TestCaseDto meta = new TestCaseDto().setDevices(new ArrayList<String>());
        TS.params().setFilterByTestType(null);
        meta.getDevices().add("TEST_Device");

        TS.params().setFilterByDevice(null);
        Assert.assertEquals(true, filter.filterTestCase(meta, "TestWithDeviceOnly"));

        TS.params().setFilterByDevice("");
        Assert.assertEquals(true, filter.filterTestCase(meta, "TestWithDeviceOnly"));

        TS.params().setFilterByDevice("TEST_Device");
        Assert.assertEquals(true, filter.filterTestCase(meta, "TestWithDeviceOnly"));

        TS.params().setFilterByDevice("~TEST_Device");
        Assert.assertEquals(false, filter.filterTestCase(meta, "TestWithDeviceOnly"));

        TS.params().setFilterByDevice("Test1, test2, TEST_Device");
        Assert.assertEquals(true, filter.filterTestCase(meta, "TestWithDeviceOnly"));

        TS.params().setFilterByDevice("Test1, test2,~TEST_Device");
        Assert.assertEquals(false, filter.filterTestCase(meta, "TestWithDeviceOnly"));
    }

    @Test
    public void TestFilterTestPlanDevice() {

        testFilterMyDevice(1, 1, 0, 1, TestPlanWithDevice.class);

        testFilterMyDevice(1, 1, 0, 1, TestPlanWithManyDevices.class);

        testFilterMyDevice(5, 5, 3, 2, TestPlanWithDevice.class, TestPlanWithManyDevices.class,
                TestPlanWithDeviceDefault.class, TestPlanWithDeviceEmpty.class,
                TestPlanWithDeviceEmptyString.class);

    }

    private void testFilterMyDevice(final int expectedTest1, final int expectedTest2, final int expectedTest3,
                                    final int expectedTest4, final Class<?>... classesToAdd) {
        TestFilter filter = new TestFilter();
        Set<Class<?>> classes = ImmutableSet.copyOf(Arrays.asList(classesToAdd));

        TS.params().setFilterByDevice(null);
        Assert.assertEquals(expectedTest1, filter.resetTestClassesMetFilters().filterTestPlansToRun(classes)
                .size());

        TS.params().setFilterByDevice("");
        Assert.assertEquals(expectedTest2, filter.resetTestClassesMetFilters().filterTestPlansToRun(classes)
                .size());

        TS.params().setFilterByDevice("~TEST_Device");
        Assert.assertEquals(expectedTest3, filter.resetTestClassesMetFilters().filterTestPlansToRun(classes)
                .size());

        TS.params().setFilterByDevice("TEST_Device");
        Assert.assertEquals(expectedTest4, filter.resetTestClassesMetFilters().filterTestPlansToRun(classes)
                .size());
    }

    @After
    public void tearDown() {
        TS.setParams(paramsBeforeTest);
    }

}

@TestPlan()
class TestPlanWithDeviceDefault {

}

@TestPlan(devices = {})
class TestPlanWithDeviceEmpty {
    @TestCase()
    public void test1() {
    }

    ;
}

@TestPlan(devices = {""})
class TestPlanWithDeviceEmptyString {

    @TestCase()
    public void test1() {
    }

    ;

}

@TestPlan(devices = {"TEST_Device"})
class TestPlanWithDevice {
    @TestCase()
    public void test1() {
    }

    ;

    @TestCase(devices = {"TEST_Device"})
    public void test2() {
    }

    ;

}

@TestPlan(devices = {"TEST_Device", "TEST_Device1", "TEST_Device2", "TEST_Device3"})
class TestPlanWithManyDevices {
    @TestCase(devices = {})
    public void test1() {
    }

    ;

    @TestCase(devices = {""})
    public void test2() {
    }

    ;

    @TestCase(devices = {"TEST_Device"})
    public void test3() {
    }

    ;

    @TestCase(devices = {"TEST_Device", "TEST_Device1", "TEST_Device2", "TEST_Device3"})
    public void test4() {
    }

    ;

    @TestCase()
    public void test5() {
    }

    ;

}