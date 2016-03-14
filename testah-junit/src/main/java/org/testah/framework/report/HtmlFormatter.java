package org.testah.framework.report;

import org.apache.velocity.VelocityContext;
import org.testah.framework.dto.TestPlanDto;

public class HtmlFormatter extends AbstractFormatter {
    
    public HtmlFormatter(final TestPlanDto testPlan) {
        super(testPlan, AbstractFormatter.DEFAULT_PACKAGE + "htmlReportV1");

    }
    
    
    public VelocityContext getContext(final VelocityContext context) {
        // TODO Auto-generated method stub
        return context;
    }
    
}
