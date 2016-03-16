package org.testah.framework.report;

import org.apache.velocity.VelocityContext;
import org.testah.framework.dto.TestPlanDto;

public class JUnitFormatter extends AbstractFormatter {

	public JUnitFormatter(final TestPlanDto testPlan) {
		super(testPlan, AbstractFormatter.DEFAULT_PACKAGE + "xmlReportV1.vm");

	}

	public VelocityContext getContext(final VelocityContext context) {
		return context;
	}

	public AbstractFormatter createReport() {
		return createReport("results.xml");
	}

}
