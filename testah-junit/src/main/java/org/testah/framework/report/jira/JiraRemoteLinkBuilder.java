package org.testah.framework.report.jira;

import org.testah.client.dto.TestCaseDto;
import org.testah.client.dto.TestPlanDto;
import org.testah.framework.report.jira.dto.RemoteIssueLinkDto;

/**
 * The Interface JiraRemoteLinkBuilder.
 */
public interface JiraRemoteLinkBuilder {

    /**
     * Gets the remote link for test plan result.
     *
     * @param testPlan
     *            the test plan
     * @return the remote link for test plan result
     */
    public abstract RemoteIssueLinkDto getRemoteLinkForTestPlanResult(final TestPlanDto testPlan);

    /**
     * Gets the remote link for test plan result known issue.
     *
     * @param testPlan
     *            the test plan
     * @return the remote link for test plan result known issue
     */
    public abstract RemoteIssueLinkDto getRemoteLinkForTestPlanResultKnownProblem(final TestPlanDto testPlan);

    /**
     * Gets the remote link for test case result.
     *
     * @param testCase
     *            the test case
     * @return the remote link for test case result
     */
    public abstract RemoteIssueLinkDto getRemoteLinkForTestCaseResult(final TestCaseDto testCase);

    /**
     * Gets the remote link for test case result known issue.
     *
     * @param testCase
     *            the test case
     * @return the remote link for test case result known issue
     */
    public abstract RemoteIssueLinkDto getRemoteLinkForTestCaseResultKnownProblem(final TestCaseDto testCase);

}
