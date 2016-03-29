package org.testah.framework.report;

import org.apache.velocity.VelocityContext;
import org.testah.client.dto.TestPlanDto;

public class HtmlFormatter extends AbstractFormatter {

	public HtmlFormatter(final TestPlanDto testPlan) {
		super(testPlan, AbstractFormatter.DEFAULT_PACKAGE + "htmlReportV1.vm");

	}

	public VelocityContext getContext(final VelocityContext context) {
		context.put("GoogleChart",
				getGoogleChart(this.getTestPlan().getRunInfo().getFail(), this.getTestPlan().getRunInfo().getPass()));
		return context;
	}

	public AbstractFormatter createReport() {
		return createReport("results.html");
	}

	private String getGoogleChart(final int numFail, final int numPass) {
		return "http://chart.apis.google.com/chart?chs=400x100&chco=ff2233,00aa33&chd=t:" + numFail + "," + numPass
				+ "&cht=p3&chl=Failed [" + numFail + "]|Passed [" + numPass + "]&chtt=Run Results";
	}

}
