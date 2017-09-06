package org.testah.framework.report;

import org.apache.velocity.VelocityContext;
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

    /* (non-Javadoc)
     * @see org.testah.framework.report.AbstractFormatter#getContext(org.apache.velocity.VelocityContext)
     */
    public VelocityContext getContext(final VelocityContext context) {

        final HashMap<String, Integer> counts = new HashMap<String, Integer>();
        counts.put("P", 0);
        counts.put("F", 0);
        counts.put("I", 0);
        getResults().forEach(result -> {
            if(null != result.getTestPlan()) {
                result.getTestPlan().getRunInfo().recalc(result.getTestPlan());
                counts.put("P", counts.get("P") + result.getTestPlan().getRunInfo().getPass());
                counts.put("F", counts.get("F") + result.getTestPlan().getRunInfo().getFail());
                counts.put("I", counts.get("I") + result.getTestPlan().getRunInfo().getIgnore());
            } else {
                int pass =  result.getJunitResult().getRunCount() -
                        (result.getJunitResult().getFailureCount() + result.getJunitResult().getIgnoreCount());
                counts.put("P", counts.get("P") + pass);
                counts.put("F", counts.get("F") + result.getJunitResult().getFailureCount());
                counts.put("I", counts.get("I") + result.getJunitResult().getIgnoreCount());
            }
        });

        context.put("GoogleChart",
                getGoogleChart(counts.get("F"), counts.get("P"), counts.get("I")));

        return context;
    }

    /* (non-Javadoc)
     * @see org.testah.framework.report.AbstractFormatter#createReport()
     */
    public AbstractFormatter createReport() {
        return createReport("summaryResults.html");
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

}
