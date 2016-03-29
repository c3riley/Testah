package org.testah.client.dto;

import java.util.ArrayList;
import java.util.List;

import org.testah.client.enums.TestStatus;
import org.testah.client.enums.TestType;

public class TestCaseDto {

    private RunTimeDto        runTime      = new RunTimeDto();
    private List<TestStepDto> testSteps    = new ArrayList<TestStepDto>();
    private Boolean           status       = null;
    private String            dataValue    = "";
    private int               id           = -1;
    private String            name         = "";
    private String            description  = "";
    private List<String>      relatedLinks = new ArrayList<String>();
    private List<String>      relatedIds   = new ArrayList<String>();
    private List<String>      tags         = new ArrayList<String>();
    private KnownProblemDto   knownProblem = null;
    private TestType          testType;
    private String            source       = null;
    private List<String>      components   = new ArrayList<String>();
    private List<String>      devices      = new ArrayList<String>();
    private List<String>      platforms    = new ArrayList<String>();
    private List<String>      runTypes     = new ArrayList<String>();
    private String            owner        = "NA";

    public TestCaseDto() {

    }

    public TestCaseDto addTestStep(final TestStepDto testStep) {
        if (null != testStep) {
            getTestSteps().add(testStep.setId(getTestSteps().size() + 1));
        }
        return this;
    }

    public TestCaseDto start() {
        setStatus(null);
        getRunTime().start();
        return this;
    }

    public TestCaseDto stop() {
        return stop(null);
    }

    public TestCaseDto stop(final Boolean status) {
        if (null != status) {
            setStatus(status);
        } else {
            setStatus();
        }
        getRunTime().stop();
        return this;
    }

    public TestCaseDto setStatus() {
        for (final TestStepDto e : testSteps) {
            if (null == e.getStatus()) {

            } else if (e.getStatus() == false) {
                status = false;
                return this;
            } else if (e.getStatus() == true) {
                status = true;
            }
        }
        return this;
    }

    public RunTimeDto getRunTime() {
        return runTime;
    }

    public TestCaseDto setRunTime(final RunTimeDto runTime) {
        this.runTime = runTime;
        return this;
    }

    public List<TestStepDto> getTestSteps() {
        return testSteps;
    }

    public TestCaseDto setTestSteps(final List<TestStepDto> testSteps) {
        this.testSteps = testSteps;
        return this;
    }

    public Boolean getStatus() {
        return status;
    }

    public TestCaseDto setStatus(final Boolean status) {
        this.status = status;
        return this;
    }

    public String getExceptions() {
        final StringBuffer sb = new StringBuffer();
        for (final TestStepDto e : testSteps) {
            if (null != e.getStatus() && e.getStatus() == false) {
                sb.append(e.getExceptionMessages());
            }
        }
        return sb.toString();
    }

    public TestCaseDto getAssertionError() {
        if (null != status && !status) {
            throw new AssertionError("TestCase: " + getName() + " Failed!\n " + getExceptions());
        }
        return this;
    }

    public TestStatus getStatusEnum() {
        return TestStatus.getStatus(status);
    }

    public String getDataValue() {
        return dataValue;
    }

    public TestCaseDto setDataValue(final String dataValue) {
        this.dataValue = dataValue;
        return this;
    }

    public int getId() {
        return id;
    }

    public TestCaseDto setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public TestCaseDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public TestCaseDto setDescription(String description) {
        this.description = description;
        return this;
    }

    public List<String> getRelatedLinks() {
        return relatedLinks;
    }

    public TestCaseDto setRelatedLinks(List<String> relatedLinks) {
        this.relatedLinks = relatedLinks;
        return this;
    }

    public List<String> getRelatedIds() {
        return relatedIds;
    }

    public TestCaseDto setRelatedIds(List<String> relatedIds) {
        this.relatedIds = relatedIds;
        return this;
    }

    public List<String> getTags() {
        return tags;
    }

    public TestCaseDto setTags(List<String> tags) {
        this.tags = tags;
        return this;
    }

    public KnownProblemDto getKnownProblem() {
        return knownProblem;
    }

    public TestCaseDto setKnownProblem(KnownProblemDto knownProblem) {
        this.knownProblem = knownProblem;
        return this;
    }

    public TestType getTestType() {
        return testType;
    }

    public TestCaseDto setTestType(TestType testType) {
        this.testType = testType;
        return this;
    }

    public String getSource() {
        return source;
    }

    public TestCaseDto setSource(String source) {
        this.source = source;
        return this;
    }

    public List<String> getComponents() {
        return components;
    }

    public TestCaseDto setComponents(List<String> components) {
        this.components = components;
        return this;
    }

    public List<String> getDevices() {
        return devices;
    }

    public TestCaseDto setDevices(List<String> devices) {
        this.devices = devices;
        return this;
    }

    public List<String> getPlatforms() {
        return platforms;
    }

    public TestCaseDto setPlatforms(List<String> platforms) {
        this.platforms = platforms;
        return this;
    }

    public List<String> getRunTypes() {
        return runTypes;
    }

    public TestCaseDto setRunTypes(List<String> runTypes) {
        this.runTypes = runTypes;
        return this;
    }

    public String getOwner() {
        return owner;
    }

    public TestCaseDto setOwner(String owner) {
        this.owner = owner;
        return this;
    }

}
