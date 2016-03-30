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
    public Step create() {
        return new Step();
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
