package org.testah.framework.report;

import java.util.HashMap;

import org.testah.TS;
import org.testah.client.dto.TestPlanDto;
import org.testah.driver.http.requests.PostRequestDto;
import org.testah.driver.http.response.ResponseDto;
import org.testah.framework.cli.Params;
import org.testah.framework.testPlan.AbstractTestPlan;
import org.testah.runner.testPlan.TestPlanActor;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * The Class TestPlanReporter.
 */
public class TestPlanReporter {

    /**
     * Report results.
     *
     * @param testPlan the test plan
     */
    public static void reportResults(final TestPlanDto testPlan) {
        String filename = "results";
        HashMap<String, String> ignored = AbstractTestPlan.getIgnoredTests();
        if (null == testPlan) {
            TS.log().info("###############################################################################");
            TS.log().info("# No Tests Ran, could be due to use of filters, for details turn on trace logging");

            if (null != ignored) {
                ignored.forEach((k, v) -> TS.log().info("# " + v + " - " + k));
            }
            TS.log().info("###############################################################################");
            return;
        }

        testPlan.getRunInfo().setIgnore(testPlan.getRunInfo().getIgnore() + ignored.size());

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
        TS.log().info("# Ignore/NA/FilteredOut: " + ri.getIgnore());
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
                TS.log().info("# Posting Data: ");
                ResponseDto response = TS.http().doRequest(
                        new PostRequestDto(TS.params().getSendJsonTestDataToService(), AbstractTestPlan.getTestPlan())
                                .withJsonUTF8(),
                        false).print(true);
                TS.log().trace("Request Payload:\n" + response.getRequestUsed().getPayloadString());
                TS.log().trace("Response Body:\n" + response.getResponseBody());

            } catch (Exception e) {
                TS.log().warn("Issue posting data to declared service: " + TS.params().getSendJsonTestDataToService(),
                        e);
            }
        }

        TS.log().info("###############################################################################");
    }

    /**
     * Open report.
     *
     * @param pathToReport the path to report
     */
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
