package org.testah.framework.report.jira;

import org.testah.client.dto.TestCaseDto;
import org.testah.client.dto.TestPlanDto;
import org.testah.framework.report.jira.dto.RemoteIssueLinkDto;

/**
 * The type Basic jira remote link builder. This is a basic implimentation of JiraRemoteLinkBuilder to make it easier to have Jira
 * integration out of the box. This class needs to be instantiated and set into the global TS.getTestPlanReporter() at runtime. Best to do
 * this in a setup common to all testplans.
 *
 * <p>Example: TS.getTestPlanReporter().setJiraRemoteLinkBuilder(new BasicJiraRemoteLinkBuilder(defaultIcon, defaultIcon)); .
 */
public class BasicJiraRemoteLinkBuilder extends AbstractJiraRemoteLinkBuilder {

    /**
     * Instantiates a new Basic jira remote link builder.
     *
     * @param icon1Url16x16 Icon path will be used for both object and status
     */
    public BasicJiraRemoteLinkBuilder(final String icon1Url16x16) {
        super(icon1Url16x16, icon1Url16x16);
    }

    /**
     * Instantiates a new Basic jira remote link builder.
     *
     * @param icon1Url16x16 the icon used with the object
     * @param icon2Url16x16 the icon used with the status
     */
    public BasicJiraRemoteLinkBuilder(final String icon1Url16x16, final String icon2Url16x16) {
        super(icon1Url16x16, icon2Url16x16);
    }

    @Override
    public RemoteIssueLinkDto getRemoteLinkForTestPlanResult(final TestPlanDto testPlan) {
        this.setLastTestPlanDtoUsed(testPlan);
        return getBaseRemoteIssueLinkDto(ISSUE_LINK_TYPE_TESTPLAN, ISSUE_LINK_TYPE_TESTPLAN, testPlan.getName(),
                getTestPlanSummary(testPlan), testPlan.getName(),
                getRunLinkToUse(), testPlan.getName(), testPlan.getName(),
                getSourceLinkToUse(), testPlan.getSource());
    }

    @Override
    public RemoteIssueLinkDto getRemoteLinkForTestPlanResultKnownProblem(final TestPlanDto testPlan) {
        this.setLastTestPlanDtoUsed(testPlan);
        return getBaseRemoteIssueLinkDto(ISSUE_LINK_TYPE_KNOWNPROBLEM_TESTPLAN, ISSUE_LINK_TYPE_KNOWNPROBLEM_TESTPLAN,
                testPlan.getKnownProblem().getTypeOfKnown() + "-" + testPlan.getName(), getTestPlanSummary(testPlan), testPlan.getName(),
                getRunLinkToUse(), testPlan.getKnownProblem().getDescription(), testPlan.getKnownProblem().getDescription(),
                getSourceLinkToUse(), testPlan.getSource());
    }

    @Override
    public RemoteIssueLinkDto getRemoteLinkForTestCaseResult(final TestCaseDto testCase) {
        return getBaseRemoteIssueLinkDto(ISSUE_LINK_TYPE_TESTCASE, ISSUE_LINK_TYPE_TESTCASE, testCase.getName(),
                getTestCaseSummary(testCase), testCase.getName(),
                getRunLinkToUse(), testCase.getName(), testCase.getName(),
                getSourceLinkToUse(), getLastTestPlanDtoUsed().getSource());
    }

    @Override
    public RemoteIssueLinkDto getRemoteLinkForTestCaseResultKnownProblem(final TestCaseDto testCase) {
        return getBaseRemoteIssueLinkDto(ISSUE_LINK_TYPE_KNOWNPROBLEM_TESTCASE, ISSUE_LINK_TYPE_KNOWNPROBLEM_TESTCASE,
                testCase.getKnownProblem().getTypeOfKnown() + "-" + testCase.getName(), getTestCaseSummary(testCase),
                testCase.getName(), getRunLinkToUse(), testCase.getKnownProblem().getDescription(),
                testCase.getKnownProblem().getDescription(), getSourceLinkToUse(), getLastTestPlanDtoUsed().getSource());
    }

    private String getTestPlanSummary(TestPlanDto testPlan) {
        return String.format("%s - status: %s - duration: %s", testPlan.getDescription(), testPlan.getStatusEnum(),
                testPlan.getRunTime().getDuration());
    }

    private String getTestCaseSummary(TestCaseDto testCase) {
        return String.format("%s - status: %s - duration: %s", testCase.getDescription(), testCase.getStatusEnum(),
                testCase.getRunTime().getDuration());
    }
}
