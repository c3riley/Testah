package org.testah.framework.dto;

import org.testah.client.enums.TestType;
import org.testah.framework.annotations.TestPlan;
import org.testah.framework.annotations.TestPlanJUnit5;

import java.lang.annotation.Annotation;
import java.util.Arrays;

public class TestPlanAnnotationDto {

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

    private TestPlanAnnotationDto(TestPlan testPlan) {
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

    private TestPlanAnnotationDto(TestPlanJUnit5 testPlan) {
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
     * @param testClass testPlan Class.
     * @return return new TestPlanAnnotationDto.
     */
    public static TestPlanAnnotationDto create(final Class testClass) {
        if (testClass != null) {
            return create(testClass.getAnnotations());
        }
        return null;
    }

    /**
     * Create TestPlanAnnotationDto from a testPlan annotation.
     * @param testPlans testPlan annotation.
     * @return return new TestPlanAnnotationDto.
     */
    public static TestPlanAnnotationDto create(Annotation... testPlans) {
        if (testPlans != null) {
            for (Annotation testPlan : testPlans) {
                if (testPlan instanceof TestPlan) {
                    return new TestPlanAnnotationDto((TestPlan) testPlan);
                } else if (testPlan instanceof TestPlanJUnit5) {
                    return new TestPlanAnnotationDto((TestPlanJUnit5) testPlan);
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

    public String owner() {
        return owner;
    }

}
