package org.testah.framework.report;

import org.apache.velocity.VelocityContext;
import org.testah.client.dto.TestPlanDto;

/**
 * The Class HtmlFormatter.
 */
public class HtmlFormatter extends AbstractTestPlanFormatter {

    /**
     * Instantiates a new html formatter.
     *
     * @param testPlan the test plan
     */
    public HtmlFormatter(final TestPlanDto testPlan) {
        super(testPlan, AbstractFormatter.DEFAULT_PACKAGE + "htmlReportV1.vm");

    }

    /* (non-Javadoc)
     * @see org.testah.framework.report.AbstractFormatter#getContext(org.apache.velocity.VelocityContext)
     */
    public VelocityContext getContext(final VelocityContext context) {
        context.put("GoogleChart",
                getGoogleChart(this.getTestPlan().getRunInfo().getFail(), this.getTestPlan().getRunInfo().getPass()));
        return context;
    }

    /* (non-Javadoc)
     * @see org.testah.framework.report.AbstractFormatter#createReport()
     */
    public AbstractFormatter createReport() {
        return createReport("results.html");
    }

    /**
     * Gets the google chart.
     *
     * @param numFail the num fail
     * @param numPass the num pass
     * @return the google chart
     */
    private String getGoogleChart(final int numFail, final int numPass) {
        return "http://chart.apis.google.com/chart?chs=400x100&chco=ff2233,00aa33&chd=t:" + numFail + "," + numPass
                + "&cht=p3&chl=Failed [" + numFail + "]|Passed [" + numPass + "]&chtt=Run Results";
    }

}
