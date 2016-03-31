package org.testah.framework.dto;

import org.testah.TS;
import org.testah.client.dto.TestStepDto;
import org.testah.framework.testPlan.AbstractTestPlan;

/**
 * The Class Step.
 */
public class Step extends TestStepDto {

    /**
     * Creates the.
     *
     * @return the step
     */
    public static Step create() {
        return new Step();
    }

    /**
     * Adds the.
     *
     * @param step
     *            the step
     * @return the test step dto
     */
    public static TestStepDto add(TestStepDto step) {
        if (TS.params().isRecordSteps()) {
            AbstractTestPlan.startTestStep(step);
        }
        return step;
    }

    /**
     * Adds the.
     *
     * @param step
     *            the step
     * @return the test step dto
     */
    public static TestStepDto add(Step step) {
        if (TS.params().isRecordSteps()) {
            AbstractTestPlan.startTestStep(step);
        }
        return step;
    }

    /**
     * Adds the.
     *
     * @return the step
     */
    public Step add() {
        if (TS.params().isRecordSteps()) {
            AbstractTestPlan.startTestStep(this);
        }
        return this;
    }
}
