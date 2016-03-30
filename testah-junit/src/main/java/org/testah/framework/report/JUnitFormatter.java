package org.testah.framework.report;

import org.apache.velocity.VelocityContext;
import org.testah.client.dto.TestPlanDto;


/**
 * The Class JUnitFormatter.
 */
public class JUnitFormatter extends AbstractFormatter {

	/**
	 * Instantiates a new j unit formatter.
	 *
	 * @param testPlan the test plan
	 */
	public JUnitFormatter(final TestPlanDto testPlan) {
		super(testPlan, AbstractFormatter.DEFAULT_PACKAGE + "xmlReportV1.vm");

	}

	/* (non-Javadoc)
	 * @see org.testah.framework.report.AbstractFormatter#getContext(org.apache.velocity.VelocityContext)
	 */
	public VelocityContext getContext(final VelocityContext context) {
		return context;
	}

	/* (non-Javadoc)
	 * @see org.testah.framework.report.AbstractFormatter#createReport()
	 */
	public AbstractFormatter createReport() {
		return createReport("results.xml");
	}

}
