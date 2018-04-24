package org.testah.framework.report.jira;

import org.apache.commons.lang3.StringUtils;
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
 * The Interface JiraRemoteLinkBuilder. This class is a helper to do some of the common task with implementing JiraRemoteLinkBuilder.
 */
public abstract class AbstractJiraRemoteLinkBuilder implements JiraRemoteLinkBuilder {

    /**
     * The Icon 1 url 16 x 16 used with the main remote link object.
     */
    protected final String icon1Url16x16;
    /**
     * The Icon 2 url 16 x 16 used with the status object.
     */
    protected final String icon2Url16x16;
    /**
     * The constant ISSUE_LINK_TYPE_TESTPLAN can be used with the link type and relationship.
     */
    public static final String ISSUE_LINK_TYPE_TESTPLAN = "E2E Testplan";
    /**
     * The constant ISSUE_LINK_TYPE_TESTCASE can be used with the link type and relationship.
     */
    public static final String ISSUE_LINK_TYPE_TESTCASE = "E2E Testcase";
    /**
     * The constant ISSUE_LINK_TYPE_KNOWNPROBLEM_TESTPLAN can be used with the link type and relationship.
     */
    public static final String ISSUE_LINK_TYPE_KNOWNPROBLEM_TESTPLAN = "E2E KP Testplan";
    /**
     * The constant ISSUE_LINK_TYPE_KNOWNPROBLEM_TESTCASE can be used with the link type and relationship.
     */
    public static final String ISSUE_LINK_TYPE_KNOWNPROBLEM_TESTCASE = "E2E KP Testcase";

    private String fileExt = "java";

    private TestPlanDto lastTestPlanDtoUsed = null;

    /**
     * Instantiates a new Abstract jira remote link builder.
     */
    protected AbstractJiraRemoteLinkBuilder() {
        this("https://jira.atlassian.com/favicon.ico");
    }

    /**
     * Instantiates a new Abstract jira remote link builder.
     *
     * @param icon1Url16x16 the icon 1 url 16 x 16 for use with both icons on the remote link object and status obje ct
     */
    protected AbstractJiraRemoteLinkBuilder(final String icon1Url16x16) {
        this(icon1Url16x16, icon1Url16x16);
    }

    /**
     * Instantiates a new Abstract jira remote link builder.
     *
     * @param icon1Url16x16 the icon 1 url 16 x 16 for use with the remote link object
     * @param icon2Url16x16 the icon 2 url 16 x 16 for use with the status object
     */
    protected AbstractJiraRemoteLinkBuilder(final String icon1Url16x16, final String icon2Url16x16) {
        this.icon1Url16x16 = icon1Url16x16;
        this.icon2Url16x16 = icon2Url16x16;
    }

    public abstract RemoteIssueLinkDto getRemoteLinkForTestPlanResult(final TestPlanDto testPlan);

    public abstract RemoteIssueLinkDto getRemoteLinkForTestPlanResultKnownProblem(final TestPlanDto testPlan);

    public abstract RemoteIssueLinkDto getRemoteLinkForTestCaseResult(final TestCaseDto testCase);

    public abstract RemoteIssueLinkDto getRemoteLinkForTestCaseResultKnownProblem(final TestCaseDto testCase);

    /**
     * Gets base remote issue link dto models jira api remote link object is what is passed to jira api.
     *
     * @param relationship the relationship is the type of remote link and its relationship with the jira ticket
     * @param linkType     the link type (see jira doc)
     * @param name         the name is the name of the remote issue link
     * @param summary      the summary is the remote link summary can be desc or more with status information
     * @param title        the title
     * @param url          the url
     * @param iconTitle    the icon title for the remote link icon
     * @param icon2Title   the icon 2 title for the status icon
     * @param icon2Url     the icon 2 url for the status icon
     * @param source       the source is the classpath to the testplan
     * @return the base remote issue link dto
     */
    protected RemoteIssueLinkDto getBaseRemoteIssueLinkDto(
            final String relationship, final String linkType, final String name,
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

    /**
     * Gets application.
     *
     * @param linkType the link type
     * @param name     the name
     * @return the application
     */
    protected Application getApplication(final String linkType, final String name) {
        final Application application = new Application();
        application.setName(name);
        application.setType(linkType);
        return application;
    }

    /**
     * Gets remote issue link.
     *
     * @param summary    the summary
     * @param title      the title
     * @param url        the url
     * @param iconTitle  the icon title
     * @param icon2Title the icon 2 title
     * @param icon2Url   the icon 2 url
     * @return the remote issue link
     */
    protected RemoteIssueLinkObject getRemoteIssueLink(
            final String summary, final String title, final String url,
            final String iconTitle, final String icon2Title, final String icon2Url) {
        RemoteIssueLinkObject remoteIssueLinkObject = new RemoteIssueLinkObject();
        remoteIssueLinkObject.setSummary(summary);
        remoteIssueLinkObject.setTitle(title);
        remoteIssueLinkObject.setUrl(url);
        remoteIssueLinkObject.setStatus(getStatus(icon2Title, icon2Url, false));
        remoteIssueLinkObject.setIcon(getIcon1(iconTitle));
        return remoteIssueLinkObject;
    }

    /**
     * Gets icon 1.
     *
     * @param title the title
     * @return the icon 1
     */
    protected Icon getIcon1(final String title) {
        Icon icon = new Icon();
        icon.setTitle(title);
        icon.setUrl16x16(icon1Url16x16);
        return icon;
    }

    /**
     * Gets status.
     *
     * @param title    the title
     * @param url      the url
     * @param resolved the resolved
     * @return the status
     */
    protected Status getStatus(final String title, final String url, final boolean resolved) {
        final Status status = new Status();
        status.setResolved(resolved);
        status.setIcon(getIcon2(title, url));
        return status;
    }

    /**
     * Gets icon 2.
     *
     * @param title the title
     * @param url   the url
     * @return the icon 2
     */
    protected Icon2 getIcon2(final String title, final String url) {
        Icon2 icon2 = new Icon2();
        icon2.setLink(url);
        icon2.setTitle(title);
        icon2.setUrl16x16(icon2Url16x16);
        return icon2;
    }

    /**
     * Gets last test plan dto used, the testplan the testcase methods will use to get source and run info.
     *
     * @return the last test plan dto used
     */
    protected TestPlanDto getLastTestPlanDtoUsed() {
        if (null == lastTestPlanDtoUsed) {
            throw new RuntimeException("Unable to use getLastTestPlanDtoUsed() unless you "
                    + " call either getRemoteLinkForTestPlanResult or getRemoteLinkForTestPlanResultKnownProblem or "
                    + "set lastTestPlanDtoUsed with setLastTestPlanDtoUsed");
        }
        return lastTestPlanDtoUsed;
    }

    /**
     * Sets last test plan dto used. Can be used to set the testplan the testcase methods will use to get source and run info.
     *
     * @param lastTestPlanDtoUsed the last test plan dto used
     */
    public void setLastTestPlanDtoUsed(final TestPlanDto lastTestPlanDtoUsed) {
        this.lastTestPlanDtoUsed = lastTestPlanDtoUsed;
    }

    /**
     * Gets run link to use. Can be used to set the testplan the testcase methods will use to get source and run info
     *
     * @return the run link to use
     */
    protected String getRunLinkToUse() {
        return validateUrl(getLastTestPlanDtoUsed().getRunInfo().getRunLocation(), "Use-Envir-Param=param_runLocation");
    }

    /**
     * Gets source link to use.
     *
     * @return the source link to use
     */
    protected String getSourceLinkToUse() {
        String sourceUrl = validateUrl(TS.params().getSourceUrl(), "IssueGetting_param_sourceUrl");
        return sourceUrl + (sourceUrl.endsWith("/") ? "" : "/")
                + getSourceWithSlash(getLastTestPlanDtoUsed().getSource()) + "." + getFileExt();
    }

    /**
     * Gets source with slash. For use with source code mgmt, turn the classpath seperator from dot to slash.
     *
     * @param source the source is the absolute classpath to the testplan class
     * @return the source with slash
     */
    protected String getSourceWithSlash(final String source) {
        return StringUtils.replace(source, ".", "/");
    }

    /**
     * Validate url string.  Checks that the string is not empty, if it is will put in a placeholder. Also ensures it starts with http
     * Jira requires a valid link, and will fail if it is not.
     *
     * @param link     the link must be a valid link starting with http
     * @param errorTip the error tip is added as a query parma to the fake link to help know what why fake link is used.
     * @return the string the link or a value that is not empty using a fake link
     */
    protected String validateUrl(String link, final String errorTip) {
        if (StringUtils.isEmpty(link)) {
            return "http://noLinkFoundToUsePlease.com/?errorTip=" + errorTip;
        }
        if (!StringUtils.startsWithIgnoreCase(link, "http")) {
            link = "http://" + link;
        }
        return link;

    }

    public String getFileExt()
    {
        return fileExt;
    }

    public void setFileExt(final String fileExt)
    {
        this.fileExt = fileExt;
    }

}
