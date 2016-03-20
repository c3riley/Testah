package org.testah.framework.report;

import org.testah.TS;
import org.testah.framework.dto.RunInfoDto;
import org.testah.framework.dto.TestPlanDto;
import org.testah.runner.testPlan.TestPlanActor;

public class TestPlanReporter {

	public static void reportResults(final TestPlanDto testPlan) {
		String filename = "results";
		if (TestPlanActor.isResultsInUse()) {
			filename += "_" + testPlan.getMeta().getSource().replace(".", "_") + "_" + TS.util().nowUnique();
		}
		final RunInfoDto ri = testPlan.getRunInfo();
		TS.log().info("###############################################################################");
		TS.log().info("# TestPlan[" + testPlan.getMeta().getSource() + " (thread:" + Thread.currentThread().getId()
				+ ") Status: " + testPlan.getStatusEnum());
		TS.log().info("# Failed: " + ri.getFail());
		TS.log().info("# Passed: " + ri.getPass());
		TS.log().info("# Ignore: " + ri.getIgnore());
		TS.log().info("# Total: " + ri.getTotal());
		TS.log().info("# Duration: " + testPlan.getRunTime().getDurationPretty());

		if (TS.params().isUseXunitFormatter()) {
			TS.log().info("# Report XUnit: "
					+ new JUnitFormatter(testPlan).createReport(filename + ".xml").getReportFile().getAbsolutePath());
		}
		if (TS.params().isUseHtmlFormatter()) {
			TS.log().info("# Report Html: "
					+ new HtmlFormatter(testPlan).createReport(filename + ".html").getReportFile().getAbsolutePath());
		}
		TS.log().info("###############################################################################");
	}

}
