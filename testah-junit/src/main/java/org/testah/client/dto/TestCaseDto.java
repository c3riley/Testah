package org.testah.client.dto;

import java.util.ArrayList;
import java.util.List;

import org.testah.client.enums.TestStatus;
import org.testah.client.enums.TestType;

/**
 * The Class TestCaseDto.
 */
public class TestCaseDto {

	/** The run time. */
	private RunTimeDto runTime = new RunTimeDto();

	/** The test steps. */
	private List<TestStepDto> testSteps = new ArrayList<TestStepDto>();

	/** The status. */
	private Boolean status = null;

	/** The data value. */
	private String dataValue = "";

	/** The id. */
	private int id = -1;

	/** The name. */
	private String name = "";

	/** The description. */
	private String description = "";

	/** The related links. */
	private List<String> relatedLinks = new ArrayList<String>();

	/** The related ids. */
	private List<String> relatedIds = new ArrayList<String>();

	/** The tags. */
	private List<String> tags = new ArrayList<String>();

	/** The known problem. */
	private KnownProblemDto knownProblem = null;

	/** The test type. */
	private TestType testType;

	/** The source. */
	private String source = null;

	/** The components. */
	private List<String> components = new ArrayList<String>();

	/** The devices. */
	private List<String> devices = new ArrayList<String>();

	/** The platforms. */
	private List<String> platforms = new ArrayList<String>();

	/** The run types. */
	private List<String> runTypes = new ArrayList<String>();

	/** The owner. */
	private String owner = "NA";

	/**
	 * Instantiates a new test case dto.
	 */
	public TestCaseDto() {

	}

	/**
	 * Adds the test step.
	 *
	 * @param testStep
	 *            the test step
	 * @return the test case dto
	 */
	public TestCaseDto addTestStep(final TestStepDto testStep) {
		if (null != testStep) {
			getTestSteps().add(testStep.setId(getTestSteps().size() + 1));
		}
		return this;
	}

	/**
	 * Start.
	 *
	 * @return the test case dto
	 */
	public TestCaseDto start() {
		setStatus(null);
		getRunTime().start();
		return this;
	}

	/**
	 * Stop.
	 *
	 * @return the test case dto
	 */
	public TestCaseDto stop() {
		return stop(null);
	}

	/**
	 * Stop.
	 *
	 * @param status
	 *            the status
	 * @return the test case dto
	 */
	public TestCaseDto stop(final Boolean status) {
		if (null != status) {
			setStatus(status);
		} else {
			setStatus();
		}
		getRunTime().stop();
		return this;
	}

	/**
	 * Sets the status.
	 *
	 * @return the test case dto
	 */
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
	 * @param runTime
	 *            the run time
	 * @return the test case dto
	 */
	public TestCaseDto setRunTime(final RunTimeDto runTime) {
		this.runTime = runTime;
		return this;
	}

	/**
	 * Gets the test steps.
	 *
	 * @return the test steps
	 */
	public List<TestStepDto> getTestSteps() {
		return testSteps;
	}

	/**
	 * Sets the test steps.
	 *
	 * @param testSteps
	 *            the test steps
	 * @return the test case dto
	 */
	public TestCaseDto setTestSteps(final List<TestStepDto> testSteps) {
		this.testSteps = testSteps;
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
	 * @param status
	 *            the status
	 * @return the test case dto
	 */
	public TestCaseDto setStatus(final Boolean status) {
		this.status = status;
		return this;
	}

	/**
	 * Gets the exceptions.
	 *
	 * @return the exceptions
	 */
	public String getExceptions() {
		final StringBuffer sb = new StringBuffer();
		for (final TestStepDto e : testSteps) {
			if (null != e.getStatus() && e.getStatus() == false) {
				sb.append(e.getExceptionMessages());
			}
		}
		return sb.toString();
	}

	/**
	 * Gets the assertion error.
	 *
	 */
	public void getAssertionError() {
		if (null != status && !status) {
			throw new AssertionError("TestCase: " + getName() + " Failed!\n " + getExceptions());
		}
	}

	/**
	 * Gets the status enum.
	 *
	 * @return the status enum
	 */
	public TestStatus getStatusEnum() {
		return TestStatus.getStatus(status);
	}

	/**
	 * Gets the data value.
	 *
	 * @return the data value
	 */
	public String getDataValue() {
		return dataValue;
	}

	/**
	 * Sets the data value.
	 *
	 * @param dataValue
	 *            the data value
	 * @return the test case dto
	 */
	public TestCaseDto setDataValue(final String dataValue) {
		this.dataValue = dataValue;
		return this;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id
	 *            the id
	 * @return the test case dto
	 */
	public TestCaseDto setId(final int id) {
		this.id = id;
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
	 * @param name
	 *            the name
	 * @return the test case dto
	 */
	public TestCaseDto setName(final String name) {
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
	 * @param description
	 *            the description
	 * @return the test case dto
	 */
	public TestCaseDto setDescription(final String description) {
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
	 * @param relatedLinks
	 *            the related links
	 * @return the test case dto
	 */
	public TestCaseDto setRelatedLinks(final List<String> relatedLinks) {
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
	 * @param relatedIds
	 *            the related ids
	 * @return the test case dto
	 */
	public TestCaseDto setRelatedIds(final List<String> relatedIds) {
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
	 * @param tags
	 *            the tags
	 * @return the test case dto
	 */
	public TestCaseDto setTags(final List<String> tags) {
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
	 * Sets the known problem.
	 *
	 * @param knownProblem
	 *            the known problem
	 * @return the test case dto
	 */
	public TestCaseDto setKnownProblem(final KnownProblemDto knownProblem) {
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
	 * @param testType
	 *            the test type
	 * @return the test case dto
	 */
	public TestCaseDto setTestType(final TestType testType) {
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
	 * @param source
	 *            the source
	 * @return the test case dto
	 */
	public TestCaseDto setSource(final String source) {
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
	 * @param components
	 *            the components
	 * @return the test case dto
	 */
	public TestCaseDto setComponents(final List<String> components) {
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
	 * @param devices
	 *            the devices
	 * @return the test case dto
	 */
	public TestCaseDto setDevices(final List<String> devices) {
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
	 * @param platforms
	 *            the platforms
	 * @return the test case dto
	 */
	public TestCaseDto setPlatforms(final List<String> platforms) {
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
	 * @param runTypes
	 *            the run types
	 * @return the test case dto
	 */
	public TestCaseDto setRunTypes(final List<String> runTypes) {
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
	 * @param owner
	 *            the owner
	 * @return the test case dto
	 */
	public TestCaseDto setOwner(final String owner) {
		this.owner = owner;
		return this;
	}

}
