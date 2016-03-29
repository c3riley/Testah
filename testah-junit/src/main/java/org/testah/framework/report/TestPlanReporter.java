package org.testah.framework.report;

import org.testah.TS;
import org.testah.client.dto.TestPlanDto;
import org.testah.driver.http.requests.PostRequestDto;
import org.testah.framework.cli.Params;
import org.testah.framework.testPlan.AbstractTestPlan;
import org.testah.runner.testPlan.TestPlanActor;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestPlanReporter {

    public static void reportResults(final TestPlanDto testPlan) {
        String filename = "results";
        if (TestPlanActor.isResultsInUse()) {
            filename += "_" + testPlan.getSource().replace(".", "_") + "_" + TS.util().nowUnique();
        }
        final org.testah.client.dto.RunInfoDto ri = testPlan.getRunInfo();
        System.out.println("\n\n\n");
        TS.log().info("###############################################################################");
        TS.log().info("# TestPlan[" + testPlan.getSource() + " (thread:" + Thread.currentThread().getId() + ") Status: "
                + testPlan.getStatusEnum());
        TS.log().info("# Passed: " + ri.getPass());
        TS.log().info("# Failed: " + ri.getFail());
        TS.log().info("# Ignore: " + ri.getIgnore());
        TS.log().info("# Total: " + ri.getTotal());
        TS.log().info("# Duration: " + TS.util().getDurationPretty(testPlan.getRunTime().getDuration()));

        if (TS.params().isUseXunitFormatter()) {
            TS.log().info("# Report XUnit: "
                    + new JUnitFormatter(testPlan).createReport(filename + ".xml").getReportFile().getAbsolutePath());
        }
        if (TS.params().isUseHtmlFormatter()) {
            final AbstractFormatter html = new HtmlFormatter(testPlan).createReport(filename + ".html");
            TS.log().info("# Report Html: " + html.getReportFile().getAbsolutePath());
            openReport(html.getReportFile().getAbsolutePath());
        }
        if (null == TS.params().getSendJsonTestDataToService()
                || TS.params().getSendJsonTestDataToService().length() > 0) {
            try {
                ObjectMapper map = new ObjectMapper();
                map.configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);

                TS.http().doRequest(
                        new PostRequestDto(TS.params().getSendJsonTestDataToService(), AbstractTestPlan.getTestPlan())
                                .withJsonUTF8());

                // TS.http().doPost(TS.params().getSendJsonTestDataToService(),
                // map.writeValueAsString(AbstractTestPlan.getTestPlan()));
            } catch (Exception e) {
                TS.log().warn("Issue posting data to declared service: " + TS.params().getSendJsonTestDataToService(),
                        e);
            }
        }

        TS.log().info("###############################################################################");
    }

    public static void openReport(final String pathToReport) {
        if (TS.params().isAutoOpenHtmlReport()) {
            try {
                ProcessBuilder pb = null;
                if (Params.isMac()) {
                    pb = new ProcessBuilder("/usr/bin/open", pathToReport);
                } else if (Params.isWindows()) {
                    pb = new ProcessBuilder("cmd", "/c", "start", pathToReport);
                }
                if (null != pb) {
                    final Process p = pb.start();
                    p.waitFor();
                }
            } catch (final Exception e) {
                throw new RuntimeException("Issue Opening Report", e);
            }
        }
    }

}
