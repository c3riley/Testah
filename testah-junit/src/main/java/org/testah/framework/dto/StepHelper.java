package org.testah.framework.dto;

import org.testah.client.dto.TestStepDto;
import org.testah.framework.testPlan.AbstractTestPlan;

public class StepHelper {

    public StepActionHelper action() {
        return new StepActionHelper();
    }

    public TestStepDto create() {
        return create("step", "");
    }

    public TestStepDto current() {
        return AbstractTestPlan.getTestStep();
    }

    public TestStepDto create(final String name) {
        return create(name, "");
    }

    public TestStepDto create(final String name, final String desc) {
        return AbstractTestPlan.startTestStep(
                new TestStepDto().setName(name).setDescription(desc));
    }

}
