package org.testah.framework.dto;

import org.testah.TS;
import org.testah.client.dto.TestStepDto;

/**
 * The Class Step.
 *
 * @deprecated Use TS.step() instead
 */
@Deprecated
public class Step extends TestStepDto {

    /**
     * Creates the.
     *
     * @return the step
     */
    public static Step create() {
        return create("");
    }

    /**
     * Create step.
     *
     * @param name the name
     * @return the step
     */
    public static Step create(final String name) {
        return create("", "");
    }

    /**
     * Create step.
     *
     * @param name        the name
     * @param description the description
     * @return the step
     */
    public static Step create(final String name, final String description) {
        final Step step = new Step();
        step.setName(name);
        step.setDescription(description);
        return step;
    }

    /**
     * Adds the.
     *
     * @param step the step
     * @return the test step dto
     */
    public static TestStepDto add(final TestStepDto step) {
        if (null != TS.params() && TS.params().isRecordSteps()) {
            TS.testSystem().startTestStep(step);
        }
        return step;
    }

    /**
     * Adds the.
     *
     * @param step the step
     * @return the test step dto
     */
    public static TestStepDto add(final Step step) {
        if (null != TS.params() && TS.params().isRecordSteps()) {
            TS.testSystem().startTestStep(step);
        }
        return step;
    }

    /**
     * Adds the.
     *
     * @return the step
     */
    public Step add() {
        if (null != TS.params() && TS.params().isRecordSteps()) {
            TS.testSystem().startTestStep(this);
        }
        return this;
    }
}
