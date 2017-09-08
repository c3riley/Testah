package org.testah.client.dto;

import org.testah.client.enums.TestStatus;
import org.testah.client.enums.TestType;
import org.testah.framework.dto.base.AbstractDtoBase;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class TestPlanDto.
 */
public class TestPlanDto extends AbstractDtoBase<TestPlanDto> {

    /**
     * The run time.
     */
    private RunTimeDto runTime = new RunTimeDto();

    /**
     * The test cases.
     */
    private List<TestCaseDto> testCases = new ArrayList<TestCaseDto>();

    /**
     * The status.
     */
    private Boolean status = null;

    private TestStatus statusEnum = null;

    /**
     * The run info.
     */
    private RunInfoDto runInfo = new RunInfoDto();

    /**
     * The id.
     */
    private int id = -1;

    /**
     * The name.
     */
    private String name = "";

    /**
     * The description.
     */
    private String description = "";

    /**
     * The related links.
     */
    private List<String> relatedLinks = new ArrayList<String>();

    /**
     * The related ids.
     */
    private List<String> relatedIds = new ArrayList<String>();

    /**
     * The tags.
     */
    private List<String> tags = new ArrayList<String>();

    /**
     * The known problem.
     */
    private KnownProblemDto knownProblem = null;

    /**
     * The test type.
     */
    private TestType testType;

    /**
     * The source.
     */
    private String source = null;

    /**
     * The components.
     */
    private List<String> components = new ArrayList<String>();

    /**
     * The devices.
     */
    private List<String> devices = new ArrayList<String>();

    /**
     * The platforms.
     */
    private List<String> platforms = new ArrayList<String>();

    /**
     * The run types.
     */
    private List<String> runTypes = new ArrayList<String>();

    /**
     * The owner.
     */
    private String owner = "NA";

    /**
     * Instantiates a new test plan dto.
     */
    public TestPlanDto() {

    }

    /**
     * Adds the test case.
     *
     * @param testCase the test case
     * @return the test plan dto
     */
    public TestPlanDto addTestCase(final TestCaseDto testCase) {
        if (null != testCase) {
            getTestCases().add(testCase);
        }
        return this;
    }

    /**
     * Start.
     *
     * @return the test plan dto
     */
    public TestPlanDto start() {
        setStatus(null);
        getRunTime().start();
        return this;
    }

    /**
     * Stop.
     *
     * @return the test plan dto
     */
    public TestPlanDto stop() {
        setStatus();
        getRunTime().stop();
        runInfo.recalc(this);
        return this;
    }

    /**
     * Gets the run info.
     *
     * @return the run info
     */
    public RunInfoDto getRunInfo() {
        return runInfo;
    }

    /**
     * Sets the status.
     *
     * @return the test plan dto
     */
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

    /**
     * Gets the run time.
     *
     * @return the run time
     */
    public RunTimeDto getRunTime() {
        return runTime;
    }

    /**
     * Sets the run time.
     *
     * @param runTime the run time
     * @return the test plan dto
     */
    public TestPlanDto setRunTime(final RunTimeDto runTime) {
        this.runTime = runTime;
        return this;
    }

    /**
     * Gets the status.
     *
     * @return the status
     */
    public Boolean getStatus() {
        return status;
    }

    /**
     * Sets the status.
     *
     * @param status the status
     * @return the test plan dto
     */
    public TestPlanDto setStatus(final Boolean status) {
        this.status = status;
        return this;
    }

    /**
     * Gets the test cases.
     *
     * @return the test cases
     */
    public List<TestCaseDto> getTestCases() {
        return testCases;
    }

    /**
     * Sets the test cases.
     *
     * @param testCases the test cases
     * @return the test plan dto
     */
    public TestPlanDto setTestCases(final List<TestCaseDto> testCases) {
        this.testCases = testCases;
        return this;
    }

    public TestStatus getStatusEnum() {
        if (null == statusEnum) {
            this.statusEnum = TestStatus.getStatus(status);
        }
        return this.statusEnum;
    }

    public TestPlanDto setStatusEnum(final TestStatus statusEnum) {
        this.statusEnum = statusEnum;
        return this;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name the name
     * @return the test plan dto
     */
    public TestPlanDto setName(final String name) {
        this.name = name;
        return this;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description.
     *
     * @param description the description
     * @return the test plan dto
     */
    public TestPlanDto setDescription(final String description) {
        this.description = description;
        return this;
    }

    /**
     * Gets the related links.
     *
     * @return the related links
     */
    public List<String> getRelatedLinks() {
        return relatedLinks;
    }

    /**
     * Sets the related links.
     *
     * @param relatedLinks the related links
     * @return the test plan dto
     */
    public TestPlanDto setRelatedLinks(final List<String> relatedLinks) {
        this.relatedLinks = relatedLinks;
        return this;
    }

    /**
     * Gets the related ids.
     *
     * @return the related ids
     */
    public List<String> getRelatedIds() {
        return relatedIds;
    }

    /**
     * Sets the related ids.
     *
     * @param relatedIds the related ids
     * @return the test plan dto
     */
    public TestPlanDto setRelatedIds(final List<String> relatedIds) {
        this.relatedIds = relatedIds;
        return this;
    }

    /**
     * Gets the tags.
     *
     * @return the tags
     */
    public List<String> getTags() {
        return tags;
    }

    /**
     * Sets the tags.
     *
     * @param tags the tags
     * @return the test plan dto
     */
    public TestPlanDto setTags(final List<String> tags) {
        this.tags = tags;
        return this;
    }

    /**
     * Gets the known problem.
     *
     * @return the known problem
     */
    public KnownProblemDto getKnownProblem() {
        return knownProblem;
    }

    /**
     * Gets the test type.
     *
     * @return the test type
     */
    public TestType getTestType() {
        return testType;
    }

    /**
     * Sets the test type.
     *
     * @param testType the test type
     * @return the test plan dto
     */
    public TestPlanDto setTestType(final TestType testType) {
        this.testType = testType;
        return this;
    }

    /**
     * Gets the source.
     *
     * @return the source
     */
    public String getSource() {
        return source;
    }

    /**
     * Sets the source.
     *
     * @param source the source
     * @return the test plan dto
     */
    public TestPlanDto setSource(final String source) {
        this.source = source;
        return this;
    }

    /**
     * Gets the components.
     *
     * @return the components
     */
    public List<String> getComponents() {
        return components;
    }

    /**
     * Sets the components.
     *
     * @param components the components
     * @return the test plan dto
     */
    public TestPlanDto setComponents(final List<String> components) {
        this.components = components;
        return this;
    }

    /**
     * Gets the devices.
     *
     * @return the devices
     */
    public List<String> getDevices() {
        return devices;
    }

    /**
     * Sets the devices.
     *
     * @param devices the devices
     * @return the test plan dto
     */
    public TestPlanDto setDevices(final List<String> devices) {
        this.devices = devices;
        return this;
    }

    /**
     * Gets the platforms.
     *
     * @return the platforms
     */
    public List<String> getPlatforms() {
        return platforms;
    }

    /**
     * Sets the platforms.
     *
     * @param platforms the platforms
     * @return the test plan dto
     */
    public TestPlanDto setPlatforms(final List<String> platforms) {
        this.platforms = platforms;
        return this;
    }

    /**
     * Gets the run types.
     *
     * @return the run types
     */
    public List<String> getRunTypes() {
        return runTypes;
    }

    /**
     * Sets the run types.
     *
     * @param runTypes the run types
     * @return the test plan dto
     */
    public TestPlanDto setRunTypes(final List<String> runTypes) {
        this.runTypes = runTypes;
        return this;
    }

    /**
     * Gets the owner.
     *
     * @return the owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Sets the owner.
     *
     * @param owner the owner
     * @return the test plan dto
     */
    public TestPlanDto setOwner(final String owner) {
        this.owner = owner;
        return this;
    }

    /**
     * Sets the known problem.
     *
     * @param knownProblem the known problem
     * @return the test plan dto
     */
    public TestPlanDto setKnownProblem(final KnownProblemDto knownProblem) {
        this.knownProblem = knownProblem;
        return this;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id the id
     * @return the test plan dto
     */
    public TestPlanDto setId(final int id) {
        this.id = id;
        return this;
    }

    /**
     * Sets the run info.
     *
     * @param runInfo the new run info
     * @return the test plan dto
     */
    public TestPlanDto setRunInfo(final RunInfoDto runInfo) {
        this.runInfo = runInfo;
        return this;
    }

}
