package org.testah.client.dto;

import java.util.ArrayList;
import java.util.List;

import org.testah.client.enums.TestStatus;
import org.testah.client.enums.TestType;

public class TestPlanDto {
    private RunTimeDto        runTime      = new RunTimeDto();
    private List<TestCaseDto> testCases    = new ArrayList<TestCaseDto>();
    private Boolean           status       = null;
    private RunInfoDto        runInfo      = new RunInfoDto();
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

    public TestPlanDto() {

    }

    public TestPlanDto addTestCase(final TestCaseDto testCase) {
        if (null != testCase) {
            getTestCases().add(testCase);
        }
        return this;
    }

    public TestPlanDto start() {
        setStatus(null);
        getRunTime().start();
        return this;
    }

    public TestPlanDto stop() {
        setStatus();
        getRunTime().stop();
        runInfo.recalc(this);
        return this;
    }

    public RunInfoDto getRunInfo() {
        return runInfo;
    }

    public TestPlanDto setStatus() {
        for (final TestCaseDto e : testCases) {
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

    public TestPlanDto setRunTime(final RunTimeDto runTime) {
        this.runTime = runTime;
        return this;
    }

    public Boolean getStatus() {
        return status;
    }

    public TestPlanDto setStatus(final Boolean status) {
        this.status = status;
        return this;
    }

    public List<TestCaseDto> getTestCases() {
        return testCases;
    }

    public TestPlanDto setTestCases(final List<TestCaseDto> testCases) {
        this.testCases = testCases;
        return this;
    }

    public TestStatus getStatusEnum() {
        return TestStatus.getStatus(status);
    }

    public String getName() {
        return name;
    }

    public TestPlanDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public TestPlanDto setDescription(String description) {
        this.description = description;
        return this;
    }

    public List<String> getRelatedLinks() {
        return relatedLinks;
    }

    public TestPlanDto setRelatedLinks(List<String> relatedLinks) {
        this.relatedLinks = relatedLinks;
        return this;
    }

    public List<String> getRelatedIds() {
        return relatedIds;
    }

    public TestPlanDto setRelatedIds(List<String> relatedIds) {
        this.relatedIds = relatedIds;
        return this;
    }

    public List<String> getTags() {
        return tags;
    }

    public TestPlanDto setTags(List<String> tags) {
        this.tags = tags;
        return this;
    }

    public KnownProblemDto getKnownProblem() {
        return knownProblem;
    }

    public TestType getTestType() {
        return testType;
    }

    public TestPlanDto setTestType(TestType testType) {
        this.testType = testType;
        return this;
    }

    public String getSource() {
        return source;
    }

    public TestPlanDto setSource(String source) {
        this.source = source;
        return this;
    }

    public List<String> getComponents() {
        return components;
    }

    public TestPlanDto setComponents(List<String> components) {
        this.components = components;
        return this;
    }

    public List<String> getDevices() {
        return devices;
    }

    public TestPlanDto setDevices(List<String> devices) {
        this.devices = devices;
        return this;
    }

    public List<String> getPlatforms() {
        return platforms;
    }

    public TestPlanDto setPlatforms(List<String> platforms) {
        this.platforms = platforms;
        return this;
    }

    public List<String> getRunTypes() {
        return runTypes;
    }

    public TestPlanDto setRunTypes(List<String> runTypes) {
        this.runTypes = runTypes;
        return this;
    }

    public String getOwner() {
        return owner;
    }

    public TestPlanDto setOwner(String owner) {
        this.owner = owner;
        return this;
    }

    public TestPlanDto setKnownProblem(final KnownProblemDto knownProblem) {
        this.knownProblem = knownProblem;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public TestPlanDto setId(Integer id) {
        this.id = id;
        return this;
    }

    public TestPlanDto setId(int id) {
        this.id = id;
        return this;
    }

    public void setRunInfo(RunInfoDto runInfo) {
        this.runInfo = runInfo;
    }

}
