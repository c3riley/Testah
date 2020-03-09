package org.testah.framework.report.jira;

import org.junit.jupiter.api.Test;
import org.testah.TS;
import org.testah.driver.http.requests.GetRequestDto;
import org.testah.framework.report.jira.dto.IssueStatus;

import static org.junit.jupiter.api.Assertions.*;

class JiraReporterTest
{
    @Test
    void testGetStatus()
    {
        TS.params().setJiraUrl("https://jira.atlassian.com");
        TS.params().setJiraUserName("");
        TS.params().setJiraPassword("");
        JiraReporter jiraReporter = new JiraReporter();
        IssueStatus issueStatus = jiraReporter.getStatus("JRA-9");
        TS.asserts().equalsToIgnoreCase("closed", issueStatus.getFields().getStatus().getName());
    }

    @Test
    void testAddAuthHeader()
    {
        TS.params().setJiraUserName("fakeuser");
        TS.params().setJiraPassword("fakepassword");
        GetRequestDto getRequest = new GetRequestDto("https://fakeurl");
        TS.asserts().equalsTo(0, getRequest.getHeaders().size());
        JiraReporter jiraReporter = new JiraReporter();
        jiraReporter.addAuthHeader(getRequest);
        TS.asserts().equalsTo(1, getRequest.getHeaders().size());
        TS.asserts().equalsToIgnoreCase("Authorization", getRequest.getHeaders().get(0).getName());
        TS.asserts().startsWith("Authorization type.", getRequest.getHeaders().get(0).getValue().toLowerCase(), "basic");
    }

    @Test
    void testIsIssueClosed() {
        TS.params().setJiraUrl("https://jira.atlassian.com");
        TS.params().setJiraUserName("");
        TS.params().setJiraPassword("");
        JiraReporter jiraReporter = new JiraReporter();
        TS.asserts().equalsTo(false, jiraReporter.isIssueUpdatable("JRA-9"));
    }
}