package org.testah.client.dto;

import org.testah.TS;
import org.testah.client.enums.TestStatus;
import org.testah.framework.cli.TestFilter;
import org.testah.framework.dto.base.AbstractDtoBase;

import java.util.HashMap;

import static org.testah.framework.cli.IgnoredTestRecorder.recordIgnoredTestCase;

/**
 * The Class RunInfoDto.
 */
public class RunInfoDto extends AbstractDtoBase<RunInfoDto> {

    /**
     * The run id.
     */
    private String runId = null;

    /**
     * The run type.
     */
    private String runType = null;

    /**
     * The version id.
     */
    private String versionId = null;

    /**
     * The run location.
     */
    private String runLocation = null;

    /**
     * The build number.
     */
    private String buildNumber = null;

    /**
     * The pass.
     */
    private int pass = 0;

    /**
     * The fail.
     */
    private int fail = 0;

    /**
     * The ignore.
     */
    private int ignore = 0;

    /**
     * The total.
     */
    private int total = 0;

    private HashMap<String, String> reportFilePaths = new HashMap<String, String>();

    /**
     * The run time properties.
     */
    private HashMap<String, String> runTimeProperties = new HashMap<String, String>();

    /**
     * Instantiates a new run info dto.
     */
    public RunInfoDto() {

    }

    /**
     * Recalc.
     *
     * @param testPlan the test plan
     * @return the run info dto
     */
    public RunInfoDto recalc(final TestPlanDto testPlan) {
        total = testPlan.getTestCases().size();
        pass = 0;
        fail = 0;
        ignore = 0;

        for (final TestCaseDto testCase : testPlan.getTestCases()) {
            // ignore the test case if the Boolean status is not defined ...
            if (null == testCase.getStatus()
                // ... or the filter_DEFAULT_filterIgnoreKnownProblem is set to true
                // and either the test plan or test case are marked with @KnownProblem ...
                || TestFilter.isFilterOn(TS.params().getFilterIgnoreKnownProblem()) && (testPlan.hasKnownProblem() || testCase.hasKnownProblem())
                // ot the status enum is IGNORE
                || TestStatus.IGNORE.equals(testCase.getStatusEnum())) {
                recordIgnoredTestCase(testPlan.getName(), testCase.getName());
                ignore++;
            } else if (testCase.getStatus()) {
                pass++;
            } else {
                fail++;
            }
        }
        return this;
    }

    /**
     * Gets the pass.
     *
     * @return the pass
     */
    public int getPass() {
        return pass;
    }

    /**
     * Sets the pass.
     *
     * @param pass the new pass
     */
    public void setPass(final int pass) {
        this.pass = pass;
    }

    /**
     * Gets the fail.
     *
     * @return the fail
     */
    public int getFail() {
        return fail;
    }

    /**
     * Sets the fail.
     *
     * @param fail the new fail
     */
    public void setFail(final int fail) {
        this.fail = fail;
    }

    /**
     * Gets the ignore.
     *
     * @return the ignore
     */
    public int getIgnore() {
        return ignore;
    }

    /**
     * Sets the ignore.
     *
     * @param ignore the new ignore
     */
    public void setIgnore(final int ignore) {
        this.ignore = ignore;
    }

    /**
     * Gets the run id.
     *
     * @return the run id
     */
    public String getRunId() {
        return runId;
    }

    /**
     * Sets the run id.
     *
     * @param runId the new run id
     */
    public void setRunId(final String runId) {
        this.runId = runId;
    }

    /**
     * Gets the total.
     *
     * @return the total
     */
    public int getTotal() {
        return total;
    }

    /**
     * Sets the total.
     *
     * @param total the new total
     */
    public void setTotal(final int total) {
        this.total = total;
    }

    /**
     * Gets the run location.
     *
     * @return the run location
     */
    public String getRunLocation() {
        return runLocation;
    }

    /**
     * Sets the run location.
     *
     * @param runLocation the new run location
     */
    public void setRunLocation(final String runLocation) {
        this.runLocation = runLocation;
    }

    /**
     * Gets the builds the number.
     *
     * @return the builds the number
     */
    public String getBuildNumber() {
        return buildNumber;
    }

    /**
     * Sets the builds the number.
     *
     * @param buildNumber the new builds the number
     */
    public void setBuildNumber(final String buildNumber) {
        this.buildNumber = buildNumber;
    }

    /**
     * Gets the run type.
     *
     * @return the run type
     */
    public String getRunType() {
        return runType;
    }

    /**
     * Sets the run type.
     *
     * @param runType the new run type
     */
    public void setRunType(final String runType) {
        this.runType = runType;
    }

    /**
     * Gets the version id.
     *
     * @return the version id
     */
    public String getVersionId() {
        return versionId;
    }

    /**
     * Sets the version id.
     *
     * @param versionId the new version id
     */
    public void setVersionId(final String versionId) {
        this.versionId = versionId;
    }

    /**
     * Gets the run time properties.
     *
     * @return the run time properties
     */
    public HashMap<String, String> getRunTimeProperties() {
        return runTimeProperties;
    }

    /**
     * Sets the run time properties.
     *
     * @param runTimeProperties the run time properties
     */
    public void setRunTimeProperties(final HashMap<String, String> runTimeProperties) {
        this.runTimeProperties = runTimeProperties;
    }

    /**
     * Gets the Report Path.
     *
     * @return reportFilePath the Report File Path
     */
    public HashMap<String, String> getReportFilePath() {
        return reportFilePaths;
    }

    /**
     * Sets the Report Path.
     *
     * @param reportFilePaths the Report File Path
     * @return return this to allow chaining
     */
    public RunInfoDto setReportFilePath(final HashMap<String, String> reportFilePaths) {
        this.reportFilePaths = reportFilePaths;
        return this;
    }

    /**
     * Add report path by report type.
     *
     * @param reportType     runtype report string [html,json,xml,txt]
     * @param reportFilePath report file path
     * @return run info object
     */
    public RunInfoDto addReportFilePath(final String reportType, final String reportFilePath) {
        reportFilePaths.put(reportType, reportFilePath);
        return this;
    }
}
