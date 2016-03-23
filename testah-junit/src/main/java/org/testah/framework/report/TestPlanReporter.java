package org.testah.framework.report;

import org.testah.TS;
import org.testah.framework.cli.Params;
import org.testah.framework.dto.RunInfoDto;
import org.testah.framework.dto.TestPlanDto;
import org.testah.runner.testPlan.TestPlanActor;

public class TestPlanReporter {

    public static void reportResults(final TestPlanDto testPlan) {
        String filename = "results";
        if (TestPlanActor.isResultsInUse()) {
            filename += "_" + testPlan.getSource().replace(".", "_") + "_" + TS.util().nowUnique();
        }
        final RunInfoDto ri = testPlan.getRunInfo();
        System.out.println("\n\n\n");
        TS.log().info("###############################################################################");
        TS.log().info("# TestPlan[" + testPlan.getSource() + " (thread:" + Thread.currentThread().getId() + ") Status: "
                + testPlan.getStatusEnum());
        TS.log().info("# Passed: " + ri.getPass());
        TS.log().info("# Failed: " + ri.getFail());
        TS.log().info("# Ignore: " + ri.getIgnore());
        TS.log().info("# Total: " + ri.getTotal());
        TS.log().info("# Duration: " + testPlan.getRunTime().getDurationPretty());

        if (TS.params().isUseXunitFormatter()) {
            TS.log().info("# Report XUnit: "
                    + new JUnitFormatter(testPlan).createReport(filename + ".xml").getReportFile().getAbsolutePath());
        }
        if (TS.params().isUseHtmlFormatter()) {
            AbstractFormatter html = new HtmlFormatter(testPlan).createReport(filename + ".html");
            TS.log().info("# Report Html: " + html.getReportFile().getAbsolutePath());
            openReport(html.getReportFile().getAbsolutePath());
        }
        TS.log().info("###############################################################################");
    }

    public static void openReport(final String pathToReport) {
        if (TS.params().isAutoOpenHtmlReport()) {
            try {
                ProcessBuilder pb = null;
                if (Params.isMac()) {
                    pb = new ProcessBuilder("/usr/bin/open", pathToReport);
                }
                if (null != pb) {
                    Process p = pb.start();
                    p.waitFor();
                }
            } catch (Exception e) {
                throw new RuntimeException("Issue Opening Report", e);
            }
        }
    }

}
