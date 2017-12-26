package org.testah.framework.report.jira;

import org.apache.commons.lang.StringUtils;
import org.testah.TS;
import org.testah.client.dto.TestCaseDto;
import org.testah.client.dto.TestPlanDto;
import org.testah.framework.report.jira.dto.Application;
import org.testah.framework.report.jira.dto.Icon;
import org.testah.framework.report.jira.dto.Icon2;
import org.testah.framework.report.jira.dto.RemoteIssueLinkDto;
import org.testah.framework.report.jira.dto.RemoteIssueLinkObject;
import org.testah.framework.report.jira.dto.Status;

/**
 * The Interface JiraRemoteLinkBuilder.
 */
public abstract class AbstractJiraRemoteLinkBuilder implements JiraRemoteLinkBuilder {

    protected final String icon1Url16x16;
    protected final String icon2Url16x16;
    public static final String ISSUE_LINK_TYPE_TESTPLAN = "E2E Testplan";
    public static final String ISSUE_LINK_TYPE_TESTCASE = "E2E Testcase";
    public static final String ISSUE_LINK_TYPE_KNOWNPROBLEM_TESTPLAN = "E2E KP Testplan";
    public static final String ISSUE_LINK_TYPE_KNOWNPROBLEM_TESTCASE = "E2E KP Testcase";

    private TestPlanDto lastTestPlanDtoUsed = null;

    protected AbstractJiraRemoteLinkBuilder() {
        this("https://jira.atlassian.com/favicon.ico");
    }

    protected AbstractJiraRemoteLinkBuilder(final String icon1Url16x16) {
        this(icon1Url16x16, icon1Url16x16);
    }

    protected AbstractJiraRemoteLinkBuilder(final String icon1Url16x16, final String icon2Url16x16) {
        this.icon1Url16x16 = icon1Url16x16;
        this.icon2Url16x16 = icon2Url16x16;
    }

    public abstract RemoteIssueLinkDto getRemoteLinkForTestPlanResult(final TestPlanDto testPlan);

    public abstract RemoteIssueLinkDto getRemoteLinkForTestPlanResultKnownProblem(final TestPlanDto testPlan);

    public abstract RemoteIssueLinkDto getRemoteLinkForTestCaseResult(final TestCaseDto testCase);

    public abstract RemoteIssueLinkDto getRemoteLinkForTestCaseResultKnownProblem(final TestCaseDto testCase);

    protected RemoteIssueLinkDto getBaseRemoteIssueLinkDto(final String relationship, final String linkType, final String name,
                                                           final String summary, final String title,
                                                           final String url, final String iconTitle, final String icon2Title,
                                                           final String icon2Url, final String source) {
        RemoteIssueLinkDto remote = new RemoteIssueLinkDto();
        remote.setGlobalId(relationship + "-" + source);
        remote.setApplication(getApplication(linkType, name));
        remote.setRelationship(relationship);
        remote.setObject(getRemoteIssueLink(summary, title, url, iconTitle, icon2Title, icon2Url));
        return remote;
    }

    protected Application getApplication(final String linkType, final String name) {
        final Application application = new Application();
        application.setName(name);
        application.setType(linkType);
        return application;
    }

    protected RemoteIssueLinkObject getRemoteIssueLink(final String summary, final String title, final String url,
                                                       final String iconTitle, final String icon2Title, final String icon2Url) {
        RemoteIssueLinkObject remoteIssueLinkObject = new RemoteIssueLinkObject();
        remoteIssueLinkObject.setSummary(summary);
        remoteIssueLinkObject.setTitle(title);
        remoteIssueLinkObject.setUrl(url);
        remoteIssueLinkObject.setStatus(getStatus(icon2Title, icon2Url, false));
        remoteIssueLinkObject.setIcon(getIcon1(iconTitle));
        return remoteIssueLinkObject;
    }

    protected Icon getIcon1(final String title) {
        Icon icon = new Icon();
        icon.setTitle(title);
        icon.setUrl16x16(icon1Url16x16);
        return icon;
    }

    protected Status getStatus(final String title, final String url, final boolean resolved) {
        final Status status = new Status();
        status.setResolved(resolved);
        status.setIcon(getIcon2(title, url));
        return status;
    }

    protected Icon2 getIcon2(final String title, final String url) {
        Icon2 icon2 = new Icon2();
        icon2.setLink(url);
        icon2.setTitle(title);
        icon2.setUrl16x16(icon2Url16x16);
        return icon2;
    }

    protected TestPlanDto getLastTestPlanDtoUsed() {
        if (null == lastTestPlanDtoUsed) {
            throw new RuntimeException("Unable to use getLastTestPlanDtoUsed() unless you " +
                    " call either getRemoteLinkForTestPlanResult or getRemoteLinkForTestPlanResultKnownProblem or " +
                    "set lastTestPlanDtoUsed with setLastTestPlanDtoUsed");
        }
        return lastTestPlanDtoUsed;
    }

    public void setLastTestPlanDtoUsed(final TestPlanDto lastTestPlanDtoUsed) {
        this.lastTestPlanDtoUsed = lastTestPlanDtoUsed;
    }

    protected String getRunLinkToUse() {
        return validateUrl(getLastTestPlanDtoUsed().getRunInfo().getRunLocation(), "Use-Envir-Param=param_runLocation");
    }

    protected String getSourceLinkToUse() {
        String sourceUrl = validateUrl(TS.params().getSourceUrl(), "IssueGetting_param_sourceUrl");
        return sourceUrl + (sourceUrl.endsWith("/") ? "" : "/") + getSourceWithSlash(getLastTestPlanDtoUsed().getSource());
    }

    protected String getSourceWithSlash(final String source) {
        if (StringUtils.isEmpty(source)) {
            return source;
        }
        return source.replace(".", "/");
    }

    protected String validateUrl(String link, final String errorTip) {
        if (StringUtils.isEmpty(link)) {
            return "http://noLinkFoundToUsePlease.com/?errorTip=" + errorTip;
        }
        if (!link.toLowerCase().startsWith("http")) {
            link = "http://" + link;
        }
        return link;

    }

}
