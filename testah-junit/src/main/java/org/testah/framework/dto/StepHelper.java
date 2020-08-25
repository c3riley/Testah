package org.testah.framework.dto;

import org.testah.TS;
import org.testah.client.dto.TestStepDto;

public class StepHelper {

    public StepActionHelper action() {
        return new StepActionHelper();
    }

    public TestStepDto current() {
        return TS.testSystem().getTestStep();
    }

    public TestStepDto create() {
        return create("step", "");
    }

    public TestStepDto create(final String name, final String desc) {
        return TS.testSystem().startTestStep(
                new TestStepDto().setName(name).setDescription(desc));
    }

    public TestStepDto create(final String name) {
        return create(name, "");
    }

}
