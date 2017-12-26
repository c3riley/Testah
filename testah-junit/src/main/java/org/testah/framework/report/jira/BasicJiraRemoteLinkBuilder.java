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
        return getBaseRemoteIssueLinkDto(ISSUE_LINK_TYPE_TESTPLAN, ISSUE_LINK_TYPE_TESTPLAN, testPlan.getName(), getTestPlanSummary(testPlan), testPlan.getName(),
                getRunLinkToUse(), testPlan.getName(), testPlan.getName(),
                getSourceLinkToUse(), testPlan.getSource());
    }

    @Override
    public RemoteIssueLinkDto getRemoteLinkForTestPlanResultKnownProblem(final TestPlanDto testPlan) {
        this.setLastTestPlanDtoUsed(testPlan);
        return getBaseRemoteIssueLinkDto(ISSUE_LINK_TYPE_KNOWNPROBLEM_TESTPLAN, ISSUE_LINK_TYPE_KNOWNPROBLEM_TESTPLAN, testPlan.getKnownProblem().getTypeOfKnown() + "-" + testPlan.getName(), getTestPlanSummary(testPlan), testPlan.getName(),
                getRunLinkToUse(), testPlan.getKnownProblem().getDescription(), testPlan.getKnownProblem().getDescription(),
                getSourceLinkToUse(), testPlan.getSource());
    }

    private String getTestPlanSummary(TestPlanDto testPlan) {
        return String.format("%s - status: %s - duration: %s", testPlan.getDescription(), testPlan.getStatusEnum(), testPlan.getRunTime().getDuration());
    }

    private String getTestCaseSummary(TestCaseDto testCase) {
        return String.format("%s - status: %s - duration: %s", testCase.getDescription(), testCase.getStatusEnum(), testCase.getRunTime().getDuration());
    }

    @Override
    public RemoteIssueLinkDto getRemoteLinkForTestCaseResult(final TestCaseDto testCase) {
        return getBaseRemoteIssueLinkDto(ISSUE_LINK_TYPE_TESTCASE, ISSUE_LINK_TYPE_TESTCASE, testCase.getName(), getTestCaseSummary(testCase), testCase.getName(),
                getRunLinkToUse(), testCase.getName(), testCase.getName(),
                getSourceLinkToUse(), getLastTestPlanDtoUsed().getSource());
    }

    @Override
    public RemoteIssueLinkDto getRemoteLinkForTestCaseResultKnownProblem(final TestCaseDto testCase) {
        return getBaseRemoteIssueLinkDto(ISSUE_LINK_TYPE_KNOWNPROBLEM_TESTCASE, ISSUE_LINK_TYPE_KNOWNPROBLEM_TESTCASE, testCase.getKnownProblem().getTypeOfKnown() + "-" + testCase.getName(), getTestCaseSummary(testCase), testCase.getName(),
                getRunLinkToUse(), testCase.getKnownProblem().getDescription(), testCase.getKnownProblem().getDescription(),
                getSourceLinkToUse(), getLastTestPlanDtoUsed().getSource());
    }

}
