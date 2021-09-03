package org.testah.framework.report;

import org.apache.velocity.VelocityContext;
import org.testah.TS;
import org.testah.framework.dto.ResultDto;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.testah.framework.cli.IgnoredTestRecorder.getIgnoredTestCases;
import static org.testah.framework.cli.IgnoredTestRecorder.getIgnoredTestPlans;
import static org.testah.framework.cli.IgnoredTestRecorder.isRecordFilterKnownProblems;

/**
 * The Class AbstractFormatter.
 */
public abstract class AbstractSummaryFormatter extends AbstractFormatter
{

    /**
     * The test plan.
     */
    protected final List<ResultDto> results;
    protected int totalTestCases;
    protected int totalTestCasesFailed;
    protected int totalTestCasesPassed;
    protected int totalTestPlans = -1;
    protected int totalTestCasesIgnored;
    protected long totalDuration;


    /**
     * Instantiates a new abstract formatter.
     * @param results        the test plan result list
     * @param pathToTemplate the path to template
     */
    public AbstractSummaryFormatter(final List<ResultDto> results, final String pathToTemplate)
    {
        super(pathToTemplate);
        this.results = results;
    }

    /**
     * Instantiates a new abstract formatter.
     * @param results               the test plan result list
     * @param totalTestPlans        total test plans
     * @param totalTestCases        total test cases
     * @param totalTestCasesPassed  total test cases passed
     * @param totalTestCasesFailed  total test cases failed
     * @param totalTestCasesIgnored total test cases ignored
     * @param totalDuration         total duration
     * @param pathToTemplate        the path to template
     */
    public AbstractSummaryFormatter(final List<ResultDto> results, int totalTestPlans, int totalTestCases,
                                    int totalTestCasesPassed, int totalTestCasesFailed, int totalTestCasesIgnored,
                                    long totalDuration, final String pathToTemplate)
    {
        super(pathToTemplate);
        this.results = results;
        this.totalTestPlans = totalTestPlans;
        this.totalTestCases = totalTestCases;
        this.totalTestCasesPassed = totalTestCasesPassed;
        this.totalTestCasesFailed = totalTestCasesFailed;
        this.totalTestCasesIgnored = totalTestCasesIgnored;
        this.totalDuration = totalDuration;
    }

    /**
     * Gets the test plan.
     *
     * @return the test plan
     */
    public List<ResultDto> getResults()
    {
        return results;
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
    public String getReport()
    {
        return getReport(getContextBase());
    }

    /**
     * Gets the context base.
     *
     * @return the context base
     */
    @Override
    public VelocityContext getContextBase()
    {
        VelocityContext context = new VelocityContext();

        if (null != results)
        {

            context.put("isRecordFilterKnownProblems", isRecordFilterKnownProblems());
            context.put("ignoredTestPlans", getIgnoredTestPlans());
            context.put("ignoredTestCases", getIgnoredTestCases());
            context.put("results", results);
            context.put("util", TS.util());
            if (totalTestPlans != -1)
            {
                context.put("totalTestPlans", totalTestPlans);
                context.put("totalTestCases", totalTestCases);
                context.put("totalTestCasesPassed", totalTestCasesPassed);
                context.put("totalTestCasesFailed", totalTestCasesFailed);
                context.put("totalTestCasesIgnored", totalTestCasesIgnored);
                context.put("totalDuration", totalDuration);
            }
            context = getContext(context);
        }

        return context;
    }

    public String getBaseReportObject()
    {
        return TS.util().toJson(this.results);
    }


    /**
     * Sets the report file.
     *
     * @param reportFile the new report file
     */
    public void setReportFile(final File reportFile)
    {
        this.reportFile = reportFile;
    }

}
