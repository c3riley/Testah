package org.testah.framework.dto;

import org.junit.platform.launcher.listeners.TestExecutionSummary;
import org.junit.runner.Result;
import org.testah.TS;
import org.testah.client.dto.TestPlanDto;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * The Class ResultDto.
 */
public class ResultDto {

    /**
     * The junit 4 result.
     */
    private Result junitResult = null;
    /**
     * the junit 5 result.
     */
    private TestExecutionSummary testExecutionSummary = null;

    private long junitCount = 0;
    private long junitFailure = 0;
    private long junitIgnore = 0;

    private String junitFailureMessage = "";

    /**
     * The test plan.
     */
    private TestPlanDto testPlan = null;

    /**
     * The class Name.
     */
    private String className = null;

    /**
     * Instantiates a new result dto.
     */
    public ResultDto() {

    }

    /**
     * Instantiates a new result dto.
     *
     * @param junitResult the junit result
     * @param testPlan    the test plan
     */
    public ResultDto(final Result junitResult, final TestPlanDto testPlan) {
        this.junitResult = junitResult;
        this.testPlan = testPlan;
    }

    /**
     * Instantiates a new result dto.
     *
     * @param junitResult the junit result
     */
    public ResultDto(final Result junitResult) {
        this(junitResult, getTestPlanDto());
        this.junitCount = junitResult.getRunCount();
        this.junitFailure = junitResult.getFailureCount();
        this.junitIgnore = junitResult.getIgnoreCount();
        this.junitFailureMessage = junitResult.getFailures().toString();
    }

    public ResultDto(final TestExecutionSummary testExecutionSummary) {
        this.testExecutionSummary = testExecutionSummary;
        this.testPlan = getTestPlanDto();
        this.junitCount = testExecutionSummary.getTestsStartedCount();
        this.junitFailure = testExecutionSummary.getTestsFailedCount();
        this.junitIgnore = testExecutionSummary.getTestsAbortedCount() + testExecutionSummary.getTestsSkippedCount();
        this.junitFailureMessage = getFailuresFromTestExecutionSummary(testExecutionSummary);
    }

    protected static String getFailuresFromTestExecutionSummary(final TestExecutionSummary testExecutionSummary) {
        StringWriter out = new StringWriter();
        PrintWriter writer = new PrintWriter(out);
        testExecutionSummary.printFailuresTo(writer);
        return out.toString();
    }

    protected static TestPlanDto getTestPlanDto() {
        TestPlanDto testPlanDto = null;
        if (null != TS.testSystem().getTestPlan()) {
            testPlanDto = TS.testSystem().getTestPlan().clone();
            TS.testSystem().cleanUpTestplanThreadLocal();
        } else {
            testPlanDto = TS.testSystem().getTestPlan();
        }
        return testPlanDto;
    }

    /**
     * Gets the junit result.
     *
     * @return the junit result
     */
    public Result getJunitResult() {
        return junitResult;
    }

    /**
     * Sets the junit result.
     *
     * @param junitResult the new junit result
     */
    public void setJunitResult(final Result junitResult) {
        this.junitResult = junitResult;
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
     * Sets the test plan.
     *
     * @param testPlan the new test plan
     */
    public void setTestPlan(final TestPlanDto testPlan) {
        this.testPlan = testPlan;
    }

    public String getClassName() {
        return className;
    }

    public ResultDto setClassName(final String className) {
        this.className = className;
        return this;
    }

    public TestExecutionSummary getTestExecutionSummary() {
        return testExecutionSummary;
    }

    public long getJunitCount() {
        return junitCount;
    }

    public long getJunitFailure() {
        return junitFailure;
    }

    public long getJunitIgnore() {
        return junitIgnore;
    }

    public long getJunitPass() {
        return junitCount - (junitFailure + junitIgnore);
    }

    public String getJunitFailureMessage() {
        return junitFailureMessage;
    }
}
