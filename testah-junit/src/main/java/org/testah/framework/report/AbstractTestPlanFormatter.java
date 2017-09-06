package org.testah.framework.report;

import org.apache.velocity.VelocityContext;
import org.testah.TS;
import org.testah.client.dto.TestPlanDto;

import java.io.File;

/**
 * The Class AbstractFormatter.
 */
public abstract class AbstractTestPlanFormatter extends AbstractFormatter {

    /**
     * The test plan.
     */
    protected final TestPlanDto testPlan;

    /**
     * Instantiates a new abstract formatter.
     *
     * @param testPlan       the test plan
     * @param pathToTemplate the path to template
     */
    public AbstractTestPlanFormatter(final TestPlanDto testPlan, final String pathToTemplate) {
        super(pathToTemplate);
        this.testPlan = testPlan;
    }

    /**
     * Gets the context base.
     *
     * @return the context base
     */
    @Override
    public VelocityContext getContextBase() {
        VelocityContext context = new VelocityContext();

        if (null != testPlan) {
            context.put("testPlan", testPlan);
            context.put("util", TS.util());

            context = getContext(context);
        }

        return context;
    }

    /**
     * Gets the context.
     *
     * @param context the context
     * @return the context
     */
    public abstract VelocityContext getContext(final VelocityContext context);

    /**
     * Gets the report.
     *
     * @return the report
     */
    public String getReport() {
        return getReport(getContextBase());
    }

    public String getBaseReportObject() {
        return TS.util().toJson(this.testPlan);
    }

    /**
     * Gets the test plan.
     *
     * @return the test plan
     */
    public TestPlanDto getTestPlan() {
        return testPlan;
    }

    /**
     * Sets the report file.
     *
     * @param reportFile the new report file
     */
    public void setReportFile(final File reportFile) {
        this.reportFile = reportFile;
    }

}
