package org.testah.framework.dto;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.testah.client.dto.TestCaseDto;
import org.testah.client.enums.TestType;
import org.testah.framework.annotations.KnownProblem;
import org.testah.framework.annotations.TestCase;
import org.testah.framework.annotations.TestCaseJUnit5;
import org.testah.framework.annotations.TestPlan;
import org.testah.framework.annotations.TestPlanJUnit5;
import org.testah.framework.testPlan.TestSystem;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class TestPlanAnnotationDto {

    public static final boolean applyTestCaseAnnotations = true;

    Class testClass;
    private int id = -1;
    private String name = "";
    private String description = "";
    private String[] relatedLinks = new String[]{};
    private String[] relatedIds = new String[]{};
    private String[] tags = {};
    private TestType testType = TestType.DEFAULT;
    private String[] components = {};
    private String[] platforms = {};
    private String[] devices = {};
    private String[] runTypes = {};
    private String owner = "";

    public TestPlanAnnotationDto() {

    }

    private TestPlanAnnotationDto(TestPlan testPlan, Class testClass) {
        this.testClass = testClass;
        this.id = testPlan.id();
        this.name = testPlan.name();
        this.description = testPlan.description();
        this.relatedLinks = testPlan.relatedLinks();
        this.relatedIds = testPlan.relatedIds();
        this.testType = testPlan.testType();
        this.tags = testPlan.tags();
        this.components = testPlan.components();
        this.platforms = testPlan.platforms();
        this.devices = testPlan.devices();
        this.runTypes = testPlan.runTypes();
        this.owner = testPlan.owner();
    }

    private TestPlanAnnotationDto(TestPlanJUnit5 testPlan, Class testClass) {
        this.testClass = testClass;
        this.id = testPlan.id();
        this.name = testPlan.name();
        this.description = testPlan.description();
        this.relatedLinks = testPlan.relatedLinks();
        this.relatedIds = testPlan.relatedIds();
        this.testType = testPlan.testType();
        this.tags = testPlan.tags();
        this.components = testPlan.components();
        this.platforms = testPlan.platforms();
        this.devices = testPlan.devices();
        this.runTypes = testPlan.runTypes();
        this.owner = testPlan.owner();
    }

    /**
     * Create TestPlanAnnotationDto from a testClass.
     * Uses TestSystem.DEFAULT_TESTPLAN_JUNIT_VERSION as default for JUnit version.
     *
     * @param testClass testPlan Class.
     * @return return new TestPlanAnnotationDto.
     */
    public static TestPlanAnnotationDto create(final Class testClass) {
        if (testClass != null) {
            return create(TestSystem.DEFAULT_TESTPLAN_JUNIT_VERSION, testClass, testClass.getAnnotations());
        }
        return null;
    }

    /**
     * Create TestPlanAnnotationDto from a testClass.
     *
     * @param junitVersion junit version being used.
     * @param testClass    testPlan Class.
     * @return return new TestPlanAnnotationDto.
     */
    public static TestPlanAnnotationDto create(final int junitVersion, final Class testClass) {
        if (testClass != null) {
            return create(junitVersion, testClass, testClass.getAnnotations());
        }
        return null;
    }

    /**
     * Create TestPlanAnnotationDto from a testPlan annotation.
     * Uses TestSystem.DEFAULT_TESTPLAN_JUNIT_VERSION as default for JUnit version.
     *
     * @param testClass test class for the testplan.
     * @param testPlans testPlan annotation.
     * @return return new TestPlanAnnotationDto.
     */
    public static TestPlanAnnotationDto create(final Class testClass, Annotation... testPlans) {
        return create(TestSystem.DEFAULT_TESTPLAN_JUNIT_VERSION, testClass, testPlans);
    }

    /**
     * Create TestPlanAnnotationDto from a testPlan annotation.
     *
     * @param testClass    test class for the testplan.
     * @param junitVersion junit version being used.
     * @param testPlans    testPlan annotation.
     * @return return new TestPlanAnnotationDto.
     */
    public static TestPlanAnnotationDto create(int junitVersion, final Class testClass, Annotation... testPlans) {
        if (testPlans != null) {
            for (Annotation testPlan : testPlans) {
                if (testPlan instanceof TestPlan) {
                    return new TestPlanAnnotationDto((TestPlan) testPlan, testClass);
                } else if (testPlan instanceof TestPlanJUnit5) {
                    return new TestPlanAnnotationDto((TestPlanJUnit5) testPlan, testClass);
                }
            }
        }
        if (junitVersion > TestSystem.MIN_JUNIT_VERSION_WITHOUT_TESTPLAN_ANNOTATION) {
            throw new NotImplementedException(TestSystem.MSG_ERROR_MISSING_TESTPLAN_ANNOTATION);
        }
        return null;
    }

    public int id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    public String[] relatedLinks() {
        return Arrays.copyOf(relatedLinks, relatedLinks.length);
    }

    public String[] relatedIds() {
        return Arrays.copyOf(relatedIds, relatedIds.length);
    }

    public String[] tags() {
        return Arrays.copyOf(tags, tags.length);
    }

    public TestType testType() {
        return testType;
    }

    public String[] components() {
        return Arrays.copyOf(components, components.length);
    }

    public String[] platforms() {
        return Arrays.copyOf(platforms, platforms.length);
    }

    public String[] devices() {
        return Arrays.copyOf(devices, devices.length);
    }

    public String[] runTypes() {
        return Arrays.copyOf(runTypes, runTypes.length);
    }

    public String owner() {
        return owner;
    }

    /**
     * Merge testcase meta data into the testplan for filtering. JUnit4.
     *
     * @return returns the merged testplan meta.
     */
    public TestPlanAnnotationDto mergeWithTestCases() {
        if (testClass != null && testClass.getMethods().length > 0) {
            for (Method test : testClass.getMethods()) {
                TestCase testCase = test.getAnnotation(TestCase.class);
                if (testCase != null) {
                    this.components = appendAndDedupArray(this.components(), testCase.components());
                    this.devices = appendAndDedupArray(this.devices(), testCase.devices());
                    this.relatedIds = appendAndDedupArray(this.relatedIds(), testCase.relatedIds());
                    this.relatedLinks = appendAndDedupArray(this.relatedLinks(), testCase.relatedLinks());
                    this.runTypes = appendAndDedupArray(this.runTypes(), testCase.runTypes());
                    this.platforms = appendAndDedupArray(this.platforms(), testCase.platforms());
                    this.tags = appendAndDedupArray(this.tags(), testCase.tags());
                }
            }
        }
        return this;
    }

    /**
     * Get List of TestCaseAnnotationDto in the TestPlan.
     *
     * @return return list of testcases found.
     */
    public List<TestCaseDto> getTestCases() {
        List<TestCaseDto> testCases = new ArrayList<>();
        if (testClass != null && testClass.getMethods().length > 0) {
            for (Method test : testClass.getMethods()) {
                TestCaseAnnotationDto testCase = TestCaseAnnotationDto.create(test);
                if (testCase != null) {
                    testCases.add(TestDtoHelper.fill(new TestCaseDto(), testCase,
                            (KnownProblem) test.getAnnotation(KnownProblem.class), this));
                }
            }
        }
        return testCases;
    }

    /**
     * Merge testcase meta data into the testplan for filtering. JUnit5.
     *
     * @return returns the merged testplan meta.
     */
    public TestPlanAnnotationDto mergeWithTestCasesJUnit5() {
        if (testClass != null && testClass.getMethods().length > 0) {
            for (Method test : testClass.getMethods()) {
                TestCaseJUnit5 testCaseJUnit5 = test.getAnnotation(TestCaseJUnit5.class);
                if (testCaseJUnit5 != null) {
                    this.components = appendAndDedupArray(this.components(), testCaseJUnit5.components());
                    this.devices = appendAndDedupArray(this.devices(), testCaseJUnit5.devices());
                    this.relatedIds = appendAndDedupArray(this.relatedIds(), testCaseJUnit5.relatedIds());
                    this.relatedLinks = appendAndDedupArray(this.relatedLinks(), testCaseJUnit5.relatedLinks());
                    this.runTypes = appendAndDedupArray(this.runTypes(), testCaseJUnit5.runTypes());
                    this.platforms = appendAndDedupArray(this.components(), testCaseJUnit5.components());
                    this.tags = appendAndDedupArray(this.tags(), testCaseJUnit5.tags());
                    this.testType = testCaseJUnit5.testType() != null ? testCaseJUnit5.testType() : this.testType();
                }
            }
        }
        return this;
    }

    public static String[] appendAndDedupArray(final String[] array1, final String[] array2) {
        return new HashSet<String>(Arrays.asList(ArrayUtils.addAll(array1, array2))).toArray(new String[0]);
    }

}
