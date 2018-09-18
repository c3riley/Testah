package org.testah.framework.report;

import org.apache.velocity.VelocityContext;
import org.testah.client.enums.TestStatus;
import org.testah.framework.dto.ResultDto;

import java.util.HashMap;
import java.util.List;

/**
 * The Class HtmlFormatter.
 */
public class SummaryHtmlFormatter extends AbstractSummaryFormatter {

    /**
     * Instantiates a new html formatter.
     *
     * @param testPlan the test plan
     */
    public SummaryHtmlFormatter(final List<ResultDto> testPlan) {
        super(testPlan, AbstractFormatter.DEFAULT_PACKAGE + "summaryHtmlV1.vm");

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
                int pass = result.getJunitResult().getRunCount()
                        - (result.getJunitResult().getFailureCount() + result.getJunitResult().getIgnoreCount());
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
        return "http://chart.apis.google.com/chart?chs=400x100&chco=ff2233,00aa33,C0C0C0&chd=t:" + numFail + "," + numPass + "," + numIgnore
                + "&cht=p3&chl=Failed [" + numFail + "]|Passed [" + numPass + "]|Ignore [" + numIgnore + "]&chtt=Run Results";
    }

    /* (non-Javadoc)
     * @see org.testah.framework.report.AbstractFormatter#createReport()
     */
    public AbstractFormatter createReport() {
        return createReport("summaryResults.html");
    }

}
