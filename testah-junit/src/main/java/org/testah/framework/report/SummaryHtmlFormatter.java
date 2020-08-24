package org.testah.framework.report;

import org.apache.velocity.VelocityContext;
import org.testah.TS;
import org.testah.client.enums.TestStatus;
import org.testah.framework.cli.Params;
import org.testah.framework.dto.ResultDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class HtmlFormatter.
 */
public class SummaryHtmlFormatter extends AbstractSummaryFormatter {

    private static final long serialVersionUID = 4734772475048784881L;

    /**
     * Instantiates a new html formatter.
     *
     * @param testPlan the test plan
     */
    public SummaryHtmlFormatter(final List<ResultDto> testPlan) {
        super(testPlan, AbstractFormatter.DEFAULT_PACKAGE + "summaryHtmlV1.vm");

    }

    /**
     * Instantiates a new html formatter.
     *
     * @param testPlan              the test plan
     * @param totalTestPlans        total test plans
     * @param totalTestCases        total test cases
     * @param totalTestCasesPassed  total test cases passed
     * @param totalTestCasesFailed  total test cases failed
     * @param totalTestCasesIgnored total test cases ignored
     * @param totalDuration         total duration
     */
    public SummaryHtmlFormatter(final List<ResultDto> testPlan, int totalTestPlans, int totalTestCases,
                                int totalTestCasesPassed, int totalTestCasesFailed, int totalTestCasesIgnored,
                                long totalDuration) {
        super(testPlan, totalTestPlans, totalTestCases, totalTestCasesPassed, totalTestCasesFailed,
            totalTestCasesIgnored, totalDuration, AbstractFormatter.DEFAULT_PACKAGE + "summaryHtmlV2.vm");
    }

    /**
     * Override getContext(...) in AbstractSummaryFormatter.
     *
     * @see org.testah.framework.report.AbstractFormatter#getContext(org.apache.velocity.VelocityContext)
     */
    public VelocityContext getContext(final VelocityContext context) {

        final HashMap<TestStatus, Integer> counts = new HashMap<TestStatus, Integer>();
        counts.put(TestStatus.PASSED, 0);
        counts.put(TestStatus.FAILED, 0);
        counts.put(TestStatus.IGNORE, 0);
        getResults().forEach(result -> {
            if (null != result.getTestPlan()) {
                result.getTestPlan().getRunInfo().recalc(result.getTestPlan());
                counts.put(TestStatus.PASSED, counts.get(TestStatus.PASSED) + result.getTestPlan().getRunInfo().getPass());
                counts.put(TestStatus.FAILED, counts.get(TestStatus.FAILED) + result.getTestPlan().getRunInfo().getFail());
                counts.put(TestStatus.IGNORE, counts.get(TestStatus.IGNORE) + result.getTestPlan().getRunInfo().getIgnore());
            } else {
                int pass = result.getJunitResult().getRunCount() -
                    (result.getJunitResult().getFailureCount() + result.getJunitResult().getIgnoreCount());
                counts.put(TestStatus.PASSED, counts.get(TestStatus.PASSED) + pass);
                counts.put(TestStatus.FAILED, counts.get(TestStatus.FAILED) + result.getJunitResult().getFailureCount());
                counts.put(TestStatus.IGNORE, counts.get(TestStatus.IGNORE) + result.getJunitResult().getIgnoreCount());
            }
        });

        context.put("GoogleChart",
            getGoogleChart(counts.get(TestStatus.FAILED), counts.get(TestStatus.PASSED), counts.get(TestStatus.IGNORE)));

        context.put("htmlPath", "");


        return context;
    }

    /**
     * Gets the google chart.
     *
     * @param numFail the num fail
     * @param numPass the num pass
     * @return the google chart
     */
    private String getGoogleChart(final int numFail, final int numPass, final int numIgnore) {
        return "http://chart.apis.google.com/chart?chs=400x100&chco=ff2233,00aa33,C0C0C0&chd=t:" + numFail +
            "," + numPass + "," + numIgnore + "&cht=p3&chl=Failed [" + numFail + "]|Passed [" + numPass + "]" +
            "|Ignore [" + numIgnore + "]&chtt=Run Results";
    }

    /**
     * createReport.
     * @return return self.
     */
    public AbstractFormatter createReport() {
        if (TS.params().isUseSummaryJsonReport()) {
            createJsonReport();
        }
        return createReport("summaryResults.html");
    }

    /**
     * Create Json Report of the summary.
     *
     * @return return self
     */
    public SummaryHtmlFormatter createJsonReport() {
        Map<String, Object> summaryResults = new HashMap<>();
        summaryResults.put("totalTestPlans", totalTestPlans);
        summaryResults.put("totalTestCases", totalTestCases);
        summaryResults.put("totalTestCasesFailed", totalTestCasesFailed);
        summaryResults.put("totalTestCasesPassed", totalTestCasesPassed);
        summaryResults.put("totalTestCasesIgnored", totalTestCasesIgnored);
        summaryResults.put("totalTestPlans", totalTestPlans);
        summaryResults.put("totalDuration", totalDuration);

        createReport("summaryResults.json", Params.getUserDir(), TS.util().toJson(summaryResults));
        return this;
    }

}
