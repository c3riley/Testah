package org.testah.client.dto;

import org.testah.TS;
import org.testah.client.enums.TestStatus;
import org.testah.client.enums.TestType;
import org.testah.framework.dto.base.AbstractDtoBase;

import java.util.ArrayList;
import java.util.List;

import static org.testah.framework.cli.TestFilter.isFilterOn;

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
    private List<TestCaseDto> testCases = new ArrayList<>();

    /**
     * The status.
     */
    private Boolean status = null;

    /**
     * Status enum to describe the specific test plan status.
     */
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
    private List<String> relatedLinks = new ArrayList<>();

    /**
     * The related ids.
     */
    private List<String> relatedIds = new ArrayList<>();

    /**
     * The tags.
     */
    private List<String> tags = new ArrayList<>();

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
    private List<String> components = new ArrayList<>();

    /**
     * The devices.
     */
    private List<String> devices = new ArrayList<>();

    /**
     * The platforms.
     */
    private List<String> platforms = new ArrayList<>();

    /**
     * The run types.
     */
    private List<String> runTypes = new ArrayList<>();

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
     * Update test case DTO with known test problem.
     * @param testCaseDto test case DTO
     */
    public void fillTestCaseKnownProblem(TestCaseDto testCaseDto) {
        if (hasKnownProblem()) {
            testCaseDto.setKnownProblem(getKnownProblem());
        }
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
     * Sets the status.
     *
     * @return the test plan dto
     */
    public TestPlanDto setStatus() {
        status = null;
        if (!hasKnownProblem()) {
            for (final TestCaseDto e : testCases) {
                //fillTestCaseKnownProblem(e);
                if (null != e.getStatus() && !TestStatus.IGNORE.equals(e.getStatusEnum())) {
                    if (!e.getStatus()) {
                        status = false;
                        return this;
                    } else if (e.getStatus()) {
                        status = true;
                    }
                }
            }
        }
        return this;
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
     * Gets the run info.
     *
     * @return the run info
     */
    public RunInfoDto getRunInfo() {
        return runInfo;
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

    /**
     * Gets the status.
     *
     * @return the status
     */
    public Boolean getStatus() {
        return status;
    }

    /**
     * Get test status enum.
     *
     * @return test status enum
     */
    public TestStatus getStatusEnum() {
        if (statusEnum == null) {
            setStatusEnum();
        }
        return statusEnum;
    }

    /**
     * Set the specific test plan status from an enum.
     *
     * @return the test plan dto
     */
    public TestPlanDto setStatusEnum() {
        // Unless it is ignored in its entirety, the test plan status derives from the states of its parts/cases.
        // Not all test cases count, some can be marked as ignored. Keep track of the ones are counted in talliedTestCases.
        List<TestCaseDto> talliedTestCases = new ArrayList<>();
        for (TestCaseDto testCaseDto : testCases)
        {
            // if the @KnownProblem annotation exists on the test plan, propagate to the test cases
            fillTestCaseKnownProblem(testCaseDto);
            // skip the test case if it is marked as known problem
            if (testCaseDto.hasKnownProblem() && isFilterOn(TS.params().getFilterIgnoreKnownProblem())) {
                continue;
            }
            // also skip if the test case is marked as IGNORE
            if (TestStatus.IGNORE.equals(testCaseDto.getStatusEnum())) {
                continue;
            }
            // otherwise, make the test case count
            talliedTestCases.add(testCaseDto);
        }

        if (talliedTestCases.size() == 0) {
            return setStatusEnum(TestStatus.IGNORE);
        }
        if (talliedTestCases.stream().anyMatch(testCaseDto ->
            testCaseDto.getStatus() != null && !testCaseDto.getStatus())) {
            return setStatusEnum(TestStatus.FAILED);
        } else if (talliedTestCases.stream().anyMatch(testCaseDto ->
            testCaseDto.getStatus() != null && testCaseDto.getStatus())) {
            return setStatusEnum(TestStatus.PASSED);
        } else {
            return setStatusEnum(TestStatus.NA);
        }
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
     * Check if the test plan is marked with known problem.
     *
     * @return true if the test plan has the @KnownProblem annotation, false otherwise
     */
    public boolean hasKnownProblem() {
        return null != knownProblem;
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
}
