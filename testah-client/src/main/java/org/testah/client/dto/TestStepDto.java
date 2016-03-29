package org.testah.client.dto;

import java.util.ArrayList;
import java.util.List;

import org.testah.client.enums.TestStatus;

public class TestStepDto {

    protected List<StepActionDto> stepActions = new ArrayList<StepActionDto>();
    private RunTimeDto            runTime     = new RunTimeDto();
    private Boolean               status      = null;
    private int                   id          = 0;
    private String                name        = "";
    private String                description = "";

    public TestStepDto() {

    }

    public TestStepDto(final String name, final String description) {
        setName(name);
        setDescription(description);
    }

    public TestStepDto addStepAction(final StepActionDto stepActions) {
        if (null != stepActions) {
            getStepActions().add(stepActions);
        }
        return this;
    }

    public TestStepDto start() {
        setStatus(null);
        getRunTime().start();
        return this;
    }

    public TestStepDto stop() {
        setStatus();
        getRunTime().stop();
        return this;
    }

    public TestStepDto setStatus() {
        for (final StepActionDto e : stepActions) {
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

    public TestStepDto setRunTime(final RunTimeDto runTime) {
        this.runTime = runTime;
        return this;
    }

    public Boolean getStatus() {
        return status;
    }

    public TestStepDto setStatus(final Boolean status) {
        this.status = status;
        return this;
    }

    public String getExceptionMessages() {
        try {
            final StringBuffer sb = new StringBuffer("Step: " + this.getName() + "\n ");
            for (final StepActionDto a : getStepActions()) {
                if (null != a.getException()) {
                    sb.append(a.getException().getMessage() + "\n ");
                    sb.append(a.getException() + "\n ");
                }
            }
            return sb.toString();
        } catch (final Exception e) {
            return null;
        }
    }

    public List<StepActionDto> getStepActions() {
        return stepActions;
    }

    public TestStepDto addStepActions(final StepActionDto stepAction) {
        stepActions.add(stepAction);
        return this;
    }

    public TestStepDto setStepActions(final List<StepActionDto> stepActions) {
        this.stepActions = stepActions;
        return this;
    }

    public int getId() {
        return id;
    }

    public TestStepDto setId(final int id) {
        this.id = id;
        return this;
    }

    public TestStatus getStatusEnum() {
        return TestStatus.getStatus(status);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
