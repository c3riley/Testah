package org.testah.framework.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import org.junit.runner.Result;
import org.testah.TS;
import org.testah.client.dto.TestPlanDto;
import org.testah.framework.dto.base.AbstractDtoBase;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * The Class ResultDto.
 */
public class ResultDto extends AbstractDtoBase<ResultDto> {

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
    private long junitPass = 0;

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
        if (testPlan != null) {
            if (testPlan.hasKnownProblem() && testPlan.getKnownProblem().getTypeOfKnown() != null) {
                junitIgnore += testPlan.getTestCases().size();
            }
            else {
                long failedKnownProblemTestCaseCount = testPlan.getTestCases().stream().filter(testCase ->
                    testCase.hasKnownProblem() && !testCase.getStatus()
                ).count();
                junitIgnore += failedKnownProblemTestCaseCount;
                junitFailure -= failedKnownProblemTestCaseCount;
                long passedKnownProblemTestCaseCount = testPlan.getTestCases().stream().filter(testCase ->
                    testCase.hasKnownProblem() && !testCase.getStatus()
                ).count();
                junitIgnore += passedKnownProblemTestCaseCount;
                junitPass -= passedKnownProblemTestCaseCount;
            }
        }
    }

    /**
     * Instantiates a new result dto.
     *
     * @param junitResult the junit result
     */
    public ResultDto(final Result junitResult) {
        this(junitResult, getTestPlanDto());
        this.junitCount = junitResult.getRunCount();
        this.junitFailure += junitResult.getFailureCount();
        this.junitIgnore += junitResult.getIgnoreCount();
        this.junitFailureMessage = junitResult.getFailures().toString();
        this.junitPass = this.junitCount - (this.junitFailure + this.junitIgnore);
    }

    /**
     * Results Dto for JUnit 5 with a TestExecutionSummary.
     *
     * @param testExecutionSummary JUnit 5 test execution summary.
     */
    public ResultDto(final TestExecutionSummary testExecutionSummary) {
        this.testExecutionSummary = testExecutionSummary;
        this.testPlan = getTestPlanDto();
        this.junitCount = testExecutionSummary.getTestsStartedCount();
        this.junitFailure = testExecutionSummary.getTestsFailedCount();
        this.junitIgnore = testExecutionSummary.getTestsAbortedCount() + testExecutionSummary.getTestsSkippedCount();
        this.junitFailureMessage = getFailuresFromTestExecutionSummary(testExecutionSummary);
        this.junitPass = testExecutionSummary.getTestsSucceededCount();
    }

    protected static String getFailuresFromTestExecutionSummary(final TestExecutionSummary testExecutionSummary) {
        StringWriter out = new StringWriter();
        PrintWriter writer = new PrintWriter(out);
        testExecutionSummary.printFailuresTo(writer);
        return out.toString();
    }

    protected static TestPlanDto getTestPlanDto() {
        try {
            TestPlanDto testPlanDto = TS.testSystem().getTestPlan();
            if (null != testPlanDto) {
                testPlanDto = testPlanDto.clone();
                TS.testSystem().cleanUpTestplanThreadLocal();
            }
            return testPlanDto;
        } catch (Throwable throwable) {
            TS.log().warn("Issue getting testPlanDto - " + throwable.getMessage());
            return null;
        }
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

    @JsonIgnore
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
        return junitPass;
    }

    public String getJunitFailureMessage() {
        return junitFailureMessage;
    }
}
