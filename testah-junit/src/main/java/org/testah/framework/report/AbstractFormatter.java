package org.testah.framework.report;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

import org.apache.commons.io.FileUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.testah.TS;
import org.testah.client.dto.TestPlanDto;
import org.testah.framework.cli.Params;


/**
 * The Class AbstractFormatter.
 */
public abstract class AbstractFormatter {

    /** The Constant DEFAULT_PACKAGE. */
    protected final static String DEFAULT_PACKAGE = "org/testah/templates/";
    
    /** The path to template. */
    protected final String        pathToTemplate;
    
    /** The test plan. */
    protected final TestPlanDto   testPlan;
    
    /** The report file. */
    protected File                reportFile      = null;

    /**
     * Instantiates a new abstract formatter.
     *
     * @param testPlan the test plan
     * @param pathToTemplate the path to template
     */
    public AbstractFormatter(final TestPlanDto testPlan, final String pathToTemplate) {
        this.testPlan = testPlan;
        this.pathToTemplate = pathToTemplate.replace("//", "/");
    }

    /**
     * Gets the context base.
     *
     * @return the context base
     */
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

    /**
     * Gets the report.
     *
     * @param context the context
     * @return the report
     */
    public String getReport(final VelocityContext context) {

        try {

            final VelocityEngine ve = new VelocityEngine();
            ve.init();

            final InputStream in = this.getClass().getClassLoader().getResourceAsStream(pathToTemplate);

            final InputStreamReader reader = new InputStreamReader(in);

            final StringWriter writer = new StringWriter();
            ve.evaluate(context, writer, pathToTemplate, reader);

            return writer.toString();

        } catch (final Exception e) {
            TS.log().error(e);
            throw new RuntimeException("Velocity template", e);
        }

    }

    /**
     * Creates the report.
     *
     * @return the abstract formatter
     */
    public abstract AbstractFormatter createReport();

    /**
     * Creates the report.
     *
     * @param reportName the report name
     * @return the abstract formatter
     */
    public AbstractFormatter createReport(final String reportName) {
        try {
            reportFile = new File(Params.addUserDir(reportName));
            FileUtils.writeStringToFile(reportFile, getReport());
        } catch (final IOException e) {
            TS.log().error("issue creating report: " + reportName, e);
        }
        return this;
    }

    /**
     * Gets the default package.
     *
     * @return the default package
     */
    public static String getDefaultPackage() {
        return DEFAULT_PACKAGE;
    }

    /**
     * Gets the path to template.
     *
     * @return the path to template
     */
    public String getPathToTemplate() {
        return pathToTemplate;
    }

    /**
     * Gets the report file.
     *
     * @return the report file
     */
    public File getReportFile() {
        return this.reportFile;
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
