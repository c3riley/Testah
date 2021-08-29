package org.testah.client.dto;

import org.testah.client.enums.TestStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class TestStepDto.
 */
public class TestStepDto {

    /**
     * The step actions.
     */
    protected List<StepActionDto> stepActions = new ArrayList<StepActionDto>();

    /**
     * The run time.
     */
    private RunTimeDto runTime = new RunTimeDto();

    /**
     * The status.
     */
    private Boolean status = null;

    private TestStatus statusEnum = null;

    /**
     * The id.
     */
    private int id = 0;

    /**
     * The name.
     */
    private String name = "";

    /**
     * The description.
     */
    private String description = "";

    private String exceptionMessage = null;

    /**
     * Instantiates a new test step dto.
     */
    public TestStepDto() {

    }

    /**
     * Instantiates a new test step dto.
     *
     * @param name        the name
     * @param description the description
     */
    public TestStepDto(final String name, final String description) {
        setName(name);
        setDescription(description);
    }

    /**
     * Adds the step action.
     *
     * @param stepActions the step actions
     * @return the test step dto
     */
    public TestStepDto addStepAction(final StepActionDto stepActions) {
        if (null != stepActions) {
            getStepActions().add(stepActions);
        }
        return this;
    }

    /**
     * Gets the step actions.
     *
     * @return the step actions
     */
    public List<StepActionDto> getStepActions() {
        return stepActions;
    }

    /**
     * Sets the step actions.
     *
     * @param stepActions the step actions
     * @return the test step dto
     */
    public TestStepDto setStepActions(final List<StepActionDto> stepActions) {
        this.stepActions = stepActions;
        return this;
    }

    /**
     * Start.
     *
     * @return the test step dto
     */
    public TestStepDto start() {
        setStatus(null);
        getRunTime().start();
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
     * @return the test step dto
     */
    public TestStepDto setRunTime(final RunTimeDto runTime) {
        this.runTime = runTime;
        return this;
    }

    /**
     * Stop.
     *
     * @return the test step dto
     */
    public TestStepDto stop() {
        setStatus();
        setStatusEnum();
        getRunTime().stop();
        return this;
    }

    /**
     * Sets the status.
     *
     * @return the test step dto
     */
    public TestStepDto setStatusEnum() {
        for (final StepActionDto e : stepActions) {
            if (TestStatus.IGNORE.equals(e.getStatusEnum())) {
                statusEnum = TestStatus.IGNORE;
            }
        }
        return this;
    }

    /**
     * Sets the status.
     *
     * @return the test step dto
     */
    public TestStepDto setStatus() {
        for (final StepActionDto e : stepActions) {
            if (null != e.getStatus()) {
                if (e.getStatus() == false) {
                    status = false;
                    return this;
                } else if (e.getStatus() == true) {
                    status = true;
                }
            }
        }
        return this;
    }

    /**
     * Sets the status.
     *
     * @param status the status
     * @return the test step dto
     */
    public TestStepDto setStatus(final Boolean status) {
        this.status = status;
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
     * Gets the exception messages.
     *
     * @return the exception messages
     */
    public String getExceptionMessages() {
        if (null == this.exceptionMessage) {
            try {
                final StringBuffer sb = new StringBuffer("Step: " + this.getName() + "\n ");
                for (final StepActionDto a : getStepActions()) {
                    if (null != a.getException()) {
                        sb.append(a.getException().getMessage() + "\n ");
                        sb.append(a.getException() + "\n ");
                    }
                }
                this.exceptionMessage = sb.toString();
            } catch (final Exception e) {
                this.exceptionMessage = null;
            }
        }
        return this.exceptionMessage;
    }

    public TestStepDto setExceptionMessages(final String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
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
     * @return the test step dto
     */
    public TestStepDto setName(final String name) {
        this.name = name;
        return this;
    }

    /**
     * Adds the step actions.
     *
     * @param stepAction the step action
     * @return the test step dto
     */
    public TestStepDto addStepActions(final StepActionDto stepAction) {
        stepActions.add(stepAction);
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
     * @param id the id
     * @return the test step dto
     */
    public TestStepDto setId(final int id) {
        this.id = id;
        return this;
    }

    /**
     * Gets the status enum.
     *
     * @return the status enum
     */
    public TestStatus getStatusEnum() {
        if (null == statusEnum) {
            this.statusEnum = TestStatus.getStatus(status);
        }
        return this.statusEnum;
    }

    public TestStepDto setStatusEnum(final TestStatus statusEnum) {
        this.statusEnum = statusEnum;
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
     * @return the test step dto
     */
    public TestStepDto setDescription(final String description) {
        this.description = description;
        return this;
    }

}
