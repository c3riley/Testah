package org.testah.framework.dto;

import org.apache.commons.lang3.StringUtils;
import org.testah.client.enums.TestType;
import org.testah.framework.annotations.TestCase;
import org.testah.framework.annotations.TestCaseJUnit5;
import org.testah.framework.annotations.TestCaseWithParamsJUnit5;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

public class TestCaseAnnotationDto {

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

    public TestCaseAnnotationDto() {

    }

    private TestCaseAnnotationDto(Method method, TestCase testCase) {
        this.id = testCase.id();
        if (null == method || StringUtils.isNotEmpty(testCase.name())) {
            this.name = testCase.name();
        } else {
            this.name = method.getName();
        }
        this.description = testCase.description();
        this.relatedLinks = testCase.relatedLinks();
        this.relatedIds = testCase.relatedIds();
        this.testType = testCase.testType();
        this.tags = testCase.tags();
        this.components = testCase.components();
        this.platforms = testCase.platforms();
        this.devices = testCase.devices();
        this.runTypes = testCase.runTypes();
    }

    private TestCaseAnnotationDto(TestCase testCase) {
        this(null, testCase);
    }

    private TestCaseAnnotationDto(Method method, TestCaseJUnit5 testCase) {
        this.id = testCase.id();
        if (null == method || StringUtils.isNotEmpty(testCase.name())) {
            this.name = testCase.name();
        } else {
            this.name = method.getName();
        }
        this.description = testCase.description();
        this.relatedLinks = testCase.relatedLinks();
        this.relatedIds = testCase.relatedIds();
        this.testType = testCase.testType();
        this.tags = testCase.tags();
        this.components = testCase.components();
        this.platforms = testCase.platforms();
        this.devices = testCase.devices();
        this.runTypes = testCase.runTypes();
    }

    private TestCaseAnnotationDto(TestCaseJUnit5 testCase) {
        this(null, testCase);
    }

    private TestCaseAnnotationDto(Method method, TestCaseWithParamsJUnit5 testCase) {
        this.id = testCase.id();
        if (null == method || StringUtils.isNotEmpty(testCase.name())) {
            this.name = testCase.name();
        } else {
            this.name = method.getName();
        }
        this.description = testCase.description();
        this.relatedLinks = testCase.relatedLinks();
        this.relatedIds = testCase.relatedIds();
        this.testType = testCase.testType();
        this.tags = testCase.tags();
        this.components = testCase.components();
        this.platforms = testCase.platforms();
        this.devices = testCase.devices();
        this.runTypes = testCase.runTypes();
    }

    private TestCaseAnnotationDto(TestCaseWithParamsJUnit5 testCase) {
        this(null, testCase);
    }

    /**
     * Create a TestCaseAnnotationDto from a test method.
     *
     * @param testMethod testCase method.
     * @return return new TestCaseAnnotationDto or null if it can't.
     */
    public static TestCaseAnnotationDto create(final Method testMethod) {
        if (testMethod != null) {
            return create(testMethod, testMethod.getAnnotations());
        }
        return null;
    }

    /**
     * Create a TestCaseAnnotationDto from testCase annotation.
     *
     * @param testCases testCase annotations.
     * @return return new TestCaseAnnotationDto or null if it can't.
     */
    public static TestCaseAnnotationDto create(Annotation... testCases) {
        if (testCases != null) {
            for (Annotation testCase : testCases) {
                if (testCase instanceof TestCase) {
                    return new TestCaseAnnotationDto((TestCase) testCase);
                } else if (testCase instanceof TestCaseJUnit5) {
                    return new TestCaseAnnotationDto((TestCaseJUnit5) testCase);
                } else if (testCase instanceof TestCaseWithParamsJUnit5) {
                    return new TestCaseAnnotationDto((TestCaseWithParamsJUnit5) testCase);
                }
            }
        }
        return null;
    }


    /**
     * Create a TestCaseAnnotationDto from test method and testCase annotation.
     *
     * @param method testCase method
     * @param testCases testCase annotations
     * @return return new TestCaseAnnotationDto or null
     */
    public static TestCaseAnnotationDto create(Method method, Annotation... testCases) {
        if (testCases != null) {
            for (Annotation testCase : testCases) {
                if (testCase instanceof TestCase) {
                    return new TestCaseAnnotationDto(method, (TestCase) testCase);
                } else if (testCase instanceof TestCaseJUnit5) {
                    return new TestCaseAnnotationDto(method, (TestCaseJUnit5) testCase);
                } else if (testCase instanceof TestCaseWithParamsJUnit5) {
                    return new TestCaseAnnotationDto(method, (TestCaseWithParamsJUnit5) testCase);
                }
            }
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

}
