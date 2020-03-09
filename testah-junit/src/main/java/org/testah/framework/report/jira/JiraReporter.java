package org.testah.framework.report.jira;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang3.StringUtils;
import org.testah.TS;
import org.testah.client.dto.TestCaseDto;
import org.testah.client.dto.TestPlanDto;
import org.testah.driver.http.requests.AbstractRequestDto;
import org.testah.driver.http.requests.GetRequestDto;
import org.testah.driver.http.requests.PostRequestDto;
import org.testah.driver.http.requests.PutRequestDto;
import org.testah.driver.http.response.ResponseDto;
import org.testah.framework.report.jira.dto.IssueStatus;
import org.testah.framework.report.jira.dto.RemoteIssueLinkDto;

import java.util.ArrayList;
import java.util.List;

public class JiraReporter {

    private static final String apiUrl = "rest/api/latest";
    private final String baseUrl;

    /**
     * Constructor.
     */
    public JiraReporter() {
        if (TS.params().getJiraUrl().endsWith("/")) {
            this.baseUrl = TS.params().getJiraUrl() + apiUrl;
        } else {
            this.baseUrl = TS.params().getJiraUrl() + "/" + apiUrl;
        }
    }

    /**
     * Create or update issue.
     *
     * @param testPlan          the test plan
     * @param remoteLinkBuilder JiraRemoteLinkBuilder
     */
    public void createOrUpdateTestPlanRemoteLink(final TestPlanDto testPlan, final JiraRemoteLinkBuilder remoteLinkBuilder) {
        if (TS.params().isUseJiraRemoteLink() && TS.params().getJiraUrl().length() > 0) {
            RemoteIssueLinkDto remoteLink;
            if (!testPlan.getRelatedIds().isEmpty()) {
                for (final String relatedId : testPlan.getRelatedIds()) {
                    if (isIssueUpdatable(relatedId)) {
                        remoteLink = getRemoteLinkForGlobalId(relatedId, testPlan.getSource());
                        if (null == remoteLink)
                        {
                            createRemoteLink(relatedId, remoteLinkBuilder.getRemoteLinkForTestPlanResult(testPlan));
                        } else
                        {
                            updateRemoteLink(relatedId, remoteLink.getId(), remoteLinkBuilder.getRemoteLinkForTestPlanResult(testPlan));
                        }
                    }
                }
            }
            if (null != testPlan.getKnownProblem()) {
                for (final String relatedId : testPlan.getKnownProblem().getLinkedIds()) {
                    if (isIssueUpdatable(relatedId)) {
                        remoteLink = getRemoteLinkForGlobalId(relatedId, testPlan.getSource());
                        if (null == remoteLink) {
                            createRemoteLink(relatedId, remoteLinkBuilder.getRemoteLinkForTestPlanResultKnownProblem(testPlan));
                        } else {
                            updateRemoteLink(relatedId, remoteLink.getId(),
                                    remoteLinkBuilder.getRemoteLinkForTestPlanResultKnownProblem(testPlan));
                        }
                    }
                }
            }
            if (null != testPlan.getTestCases() && !testPlan.getTestCases().isEmpty()) {
                for (final TestCaseDto testCase : testPlan.getTestCases()) {
                    if (null != testCase.getKnownProblem() && null != testCase.getKnownProblem().getLinkedIds()) {
                        for (final String relatedId : testCase.getKnownProblem().getLinkedIds()) {
                            if (isIssueUpdatable(relatedId)) {
                                remoteLink = getRemoteLinkForGlobalId(relatedId, testCase.getSource());
                                if (null == remoteLink) {
                                    createRemoteLink(relatedId, remoteLinkBuilder.getRemoteLinkForTestCaseResultKnownProblem(testCase));
                                } else {
                                    updateRemoteLink(relatedId, remoteLink.getId(),
                                            remoteLinkBuilder.getRemoteLinkForTestCaseResultKnownProblem(testCase));
                                }
                            }
                        }
                    }
                    if (null != testCase.getRelatedIds() && !testCase.getRelatedIds().isEmpty()) {
                        for (final String relatedId : testCase.getRelatedIds()) {
                            if (isIssueUpdatable(relatedId)) {
                                remoteLink = getRemoteLinkForGlobalId(relatedId, testCase.getSource());
                                if (null == remoteLink) {
                                    createRemoteLink(relatedId, remoteLinkBuilder.getRemoteLinkForTestCaseResult(testCase));
                                } else {
                                    updateRemoteLink(relatedId, remoteLink.getId(),
                                            remoteLinkBuilder.getRemoteLinkForTestCaseResult(testCase));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Get remote link for global id.
     *
     * @param issue    the issue
     * @param globalId global id
     * @return remote issue link for global id
     */
    public RemoteIssueLinkDto getRemoteLinkForGlobalId(final String issue, final String globalId) {
        if (null != issue && null != globalId) {
            for (RemoteIssueLinkDto link : getRemoteLinks(issue)) {
                if (link.getGlobalId().equals(globalId)) {
                    return link;
                }
            }
        }
        return null;
    }

    /**
     * Create a remote issue link.
     *
     * @param issue      the issue
     * @param remoteLink remote link to create
     * @return remote issue link
     */
    public RemoteIssueLinkDto createRemoteLink(final String issue, final RemoteIssueLinkDto remoteLink) {
        try {
            if (!StringUtils.isEmpty(issue)) {
                if (null != remoteLink) {
                    PostRequestDto post = new PostRequestDto(baseUrl + "/issue/" + issue + "/remotelink", remoteLink);
                    return TS.http().doRequest(addAuthHeader(post.withJson())).getResponse(RemoteIssueLinkDto.class);
                }
            }
        } catch (Exception e) {
            TS.log().error(e);
        }
        return null;
    }

    /**
     * Update a remote issue link.
     *
     * @param issue      the issue
     * @param id         the issue id
     * @param remoteLink remote issue link
     * @return response of HTTP request
     */
    public ResponseDto updateRemoteLink(final String issue, final int id, final RemoteIssueLinkDto remoteLink)
    {
        try
        {
            if (!StringUtils.isEmpty(issue))
            {
                if (null != remoteLink)
                {
                    PutRequestDto put = new PutRequestDto(baseUrl + "/issue/" + issue + "/remotelink/" + id, remoteLink);
                    return TS.http().doRequest(addAuthHeader(put.withJson()));
                }
            }
        } catch (Exception e)
        {
            TS.log().error(e);
        }
        return null;
    }

    /**
     * Check whether the Jira issue is closed.
     *
     * @param issue the Jira Id of the issue
     * @return true if the Jira issue status is 'Closed'
     */
    public boolean isIssueClosed(final String issue) {
        boolean status = false;
        IssueStatus issueStatus = getStatus(issue);
        if (issueStatus != null)
        {
            status = issueStatus.getFields().getStatus().getName().toLowerCase().equals("closed");
        }
        return status;
    }

    public boolean isIssueUpdatable(final String issue) {
        return !StringUtils.isEmpty(issue) && !isIssueClosed(issue);
    }

    /**
     * Get the status for a jira issue.
     *
     * @param issue      the issue
     * @return the IssueStatus dto
     */
    public IssueStatus getStatus(final String issue) {
        GetRequestDto get = new GetRequestDto(String.format("%s/issue/%s?fields=status", baseUrl, issue));
        return TS.http().doRequest(addAuthHeader(get.withJson())).getResponse(IssueStatus.class);
    }

    /**
     * Get remote links.
     *
     * @param issue the issue
     * @return list of remote issue link
     */
    public List<RemoteIssueLinkDto> getRemoteLinks(final String issue) {
        try {
            if (!StringUtils.isEmpty(issue)) {
                GetRequestDto get = new GetRequestDto(baseUrl + "/issue/" + issue + "/remotelink");
                return TS.http().doRequest(addAuthHeader(get.withJson())).getResponse(new TypeReference<List<RemoteIssueLinkDto>>() {
                });
            }
        } catch (Exception e) {
            TS.log().error(e);
        }
        return new ArrayList<RemoteIssueLinkDto>();
    }

    <T> T addAuthHeader(final AbstractRequestDto<T> request) {
        return request.addBasicAuth(TS.params().getJiraUserName(), TS.params().getJiraPassword());
    }

}
