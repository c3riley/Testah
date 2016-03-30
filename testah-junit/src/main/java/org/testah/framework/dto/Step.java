package org.testah.framework.dto;

import org.testah.TS;
import org.testah.client.dto.TestStepDto;
import org.testah.framework.testPlan.AbstractTestPlan;

public class Step extends TestStepDto {

    public Step create() {
        return new Step();
    }

    public Step add() {
        if (TS.params().isRecordSteps()) {
            AbstractTestPlan.startTestStep(this);
        }
        return this;
    }
}
