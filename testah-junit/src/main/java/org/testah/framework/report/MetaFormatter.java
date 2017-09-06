package org.testah.framework.report;

import org.apache.velocity.VelocityContext;
import org.testah.client.dto.TestPlanDto;

/**
 * The Class HtmlFormatter.
 */
public class MetaFormatter extends AbstractTestPlanFormatter {

	/**
	 * Instantiates a new html formatter.
	 *
	 * @param testPlan
	 *            the test plan
	 */
	public MetaFormatter(final TestPlanDto testPlan) {
		super(testPlan, AbstractFormatter.DEFAULT_PACKAGE + "MetaReportV1.vm");

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.testah.framework.report.AbstractFormatter#getContext(org.apache.
	 * velocity.VelocityContext)
	 */
	public VelocityContext getContext(final VelocityContext context) {
		return context;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.testah.framework.report.AbstractFormatter#createReport()
	 */
	public AbstractFormatter createReport() {
		return createReport("meta.txt");
	}

}
