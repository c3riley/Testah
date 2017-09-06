package org.testah.framework.dto;

import org.junit.runner.Result;
import org.testah.client.dto.TestPlanDto;
import org.testah.framework.testPlan.AbstractTestPlan;


/**
 * The Class ResultDto.
 */
public class ResultDto {

    /** The junit result. */
    private Result      junitResult = null;
    
    /** The test plan. */
    private TestPlanDto testPlan    = null;

    /**
     * Instantiates a new result dto.
     */
    public ResultDto() {

    }

    /**
     * Instantiates a new result dto.
     *
     * @param junitResult the junit result
     * @param testPlan the test plan
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
        this.junitResult = junitResult;
        if(null!=AbstractTestPlan.getTestPlan()) {
            this.testPlan = AbstractTestPlan.getTestPlan().clone();
        } else {
            this.testPlan = AbstractTestPlan.getTestPlan();
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

}
