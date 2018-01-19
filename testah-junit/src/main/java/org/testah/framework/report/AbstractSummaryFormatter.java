package org.testah.framework.report;

import org.apache.velocity.VelocityContext;
import org.testah.TS;
import org.testah.framework.dto.ResultDto;

import java.io.File;
import java.util.List;

/**
 * The Class AbstractFormatter.
 */
public abstract class AbstractSummaryFormatter extends AbstractFormatter {

    /**
     * The test plan.
     */
    protected final List<ResultDto> results;

    /**
     * Instantiates a new abstract formatter.
     *
     * @param results        the test plan result list
     * @param pathToTemplate the path to template
     */
    public AbstractSummaryFormatter(final List<ResultDto> results, final String pathToTemplate) {
        super(pathToTemplate);
        this.results = results;
    }

    /**
     * Gets the context base.
     *
     * @return the context base
     */
    @Override
    public VelocityContext getContextBase() {
        VelocityContext context = new VelocityContext();

        if (null != results) {
            context.put("results", results);
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
        return TS.util().toJson(this.results);
    }

    /**
     * Gets the test plan.
     *
     * @return the test plan
     */
    public List<ResultDto> getResults() {
        return results;
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
