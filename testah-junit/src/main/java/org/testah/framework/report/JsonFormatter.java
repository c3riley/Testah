package org.testah.framework.report;

import org.apache.velocity.VelocityContext;
import org.testah.client.dto.TestPlanDto;

public class JsonFormatter extends AbstractFormatter {

    public JsonFormatter(final TestPlanDto testPlan) {
        super(testPlan, null);
    }

    @Override
    public VelocityContext getContext(final VelocityContext context) {
        return null;
    }

    @Override
    public AbstractFormatter createReport() {
        return createReport("results.json");
    }

}
