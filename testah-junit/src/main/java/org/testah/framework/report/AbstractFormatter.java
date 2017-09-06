package org.testah.framework.report;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.testah.TS;
import org.testah.framework.cli.Params;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

/**
 * The Class AbstractFormatter.
 */
public abstract class AbstractFormatter {

    /** The Constant DEFAULT_PACKAGE. */
    protected final static String DEFAULT_PACKAGE = "org/testah/templates/";

    /** The path to template. */
    protected final String pathToTemplate;

    /** The report file. */
    protected File reportFile = null;

    /**
     * Instantiates a new abstract formatter.
     *
     * @param pathToTemplate
     *            the path to template
     */
    public AbstractFormatter(final String pathToTemplate) {
        if (null != pathToTemplate) {
            this.pathToTemplate = pathToTemplate.replace("//", "/");
        } else {
            this.pathToTemplate = null;
        }
    }

    /**
     * Gets the context base.
     *
     * @return the context base
     */
    public abstract VelocityContext getContextBase();

    /**
     * Gets the context.
     *
     * @param context
     *            the context
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

    public abstract String getBaseReportObject();


    /**
     * Gets the report.
     *
     * @param context
     *            the context
     * @return the report
     */
    public String getReport(final VelocityContext context) {
        if (null == context || null == pathToTemplate) {
            TS.log().trace("No report context so returing json for testplan info");
            return TS.util().toJson(getBaseReportObject());
        }
        try {

            final VelocityEngine ve = new VelocityEngine();
            ve.init();

            final InputStream in = this.getClass().getClassLoader().getResourceAsStream(pathToTemplate);

            final InputStreamReader reader = new InputStreamReader(in, "UTF-8");

            final StringWriter writer = new StringWriter();
            ve.evaluate(context, writer, pathToTemplate, reader);

            return maskValuesInReport(writer.toString());

        } catch (final Exception e) {
            TS.log().error(e);
            throw new RuntimeException("Velocity template", e);
        }

    }

    private String maskValuesInReport(final String report) {
        if (TS.getMaskValues().isEmpty()) {
            return report;
        }
        final int size = TS.getMaskValues().keySet().size();
        return StringUtils.replaceEach(report, TS.getMaskValues().keySet().toArray(new String[size]),
                TS.getMaskValues().values().toArray(new String[size]));
    }

    /**
     * Creates the report.
     *
     * @return the abstract formatter
     */
    public abstract AbstractFormatter createReport();


    public AbstractFormatter createReport(final String reportName) {
        return createReport(reportName, Params.getUserDir());
    }



    /**
     * Creates the report.
     *
     * @param reportName
     *            the report name
     * @param directory directory to write the report to
     * @return the abstract formatter
     */
    public AbstractFormatter createReport(final String reportName, final String directory) {
        try {
            reportFile = new File(directory, reportName);
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
     * Sets the report file.
     *
     * @param reportFile
     *            the new report file
     */
    public void setReportFile(final File reportFile) {
        this.reportFile = reportFile;
    }

}
