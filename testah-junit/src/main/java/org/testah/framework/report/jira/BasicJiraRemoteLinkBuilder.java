package org.testah.framework.report.jira;

import org.testah.client.dto.TestCaseDto;
import org.testah.client.dto.TestPlanDto;
import org.testah.framework.report.jira.dto.RemoteIssueLinkDto;

public class BasicJiraRemoteLinkBuilder extends AbstractJiraRemoteLinkBuilder {

    public BasicJiraRemoteLinkBuilder(final String icon1Url16x16) {
        super(icon1Url16x16, icon1Url16x16);
    }

    public BasicJiraRemoteLinkBuilder(final String icon1Url16x16, final String icon2Url16x16) {
        super(icon1Url16x16, icon2Url16x16);
    }

    @Override
    public RemoteIssueLinkDto getRemoteLinkForTestPlanResult(final TestPlanDto testPlan) {
        this.setLastTestPlanDtoUsed(testPlan);
        final String summary = String.format("%s - status: %s - duration: %s", testPlan.getDescription(), testPlan.getStatusEnum(), testPlan.getRunTime().getDuration());
        return getBaseRemoteIssueLinkDto(ISSUE_LINK_TYPE_TESTPLAN, ISSUE_LINK_TYPE_TESTPLAN, testPlan.getName(), summary, testPlan.getName(),
                getRunLinkToUse(), testPlan.getName(), testPlan.getName(),
                getSourceLinkToUse());
    }

    @Override
    public RemoteIssueLinkDto getRemoteLinkForTestPlanResultKnownProblem(final TestPlanDto testPlan) {
        this.setLastTestPlanDtoUsed(testPlan);
        return getBaseRemoteIssueLinkDto("E2E KP Testplan", ISSUE_LINK_TYPE_KNOWNPROBLEM_TESTPLAN, testPlan.getName(), testPlan.getKnownProblem().getDescription(), testPlan.getName(),
                getRunLinkToUse(), "test icon", "test icon 2",
                getSourceLinkToUse());
    }

    @Override
    public RemoteIssueLinkDto getRemoteLinkForTestCaseResult(final TestCaseDto testCase) {
        return getBaseRemoteIssueLinkDto(ISSUE_LINK_TYPE_TESTCASE, ISSUE_LINK_TYPE_TESTCASE, testCase.getName(), testCase.getDescription(), testCase.getName(),
                getRunLinkToUse(), "test icon", "test icon 2",
                getSourceLinkToUse());
    }

    @Override
    public RemoteIssueLinkDto getRemoteLinkForTestCaseResultKnownProblem(final TestCaseDto testCase) {
        return getBaseRemoteIssueLinkDto("E2E KP Testcase", ISSUE_LINK_TYPE_KNOWNPROBLEM_TESTCASE, testCase.getKnownProblem().getTypeOfKnown() + "-" + testCase.getName(), testCase.getKnownProblem().getDescription(), testCase.getName(),
                getRunLinkToUse(), "test icon", "test icon 2",
                getSourceLinkToUse());
    }



}
