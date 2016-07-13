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
     * Gets the remote link for test case result.
     *
     * @param testPlan
     *            the test plan
     * @return the remote link for test case result
     */
    public abstract RemoteIssueLinkDto getRemoteLinkForTestCaseResult(final TestCaseDto testPlan);

}
