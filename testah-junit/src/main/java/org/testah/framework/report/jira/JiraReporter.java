package org.testah.framework.report.jira;

import com.fasterxml.jackson.core.type.TypeReference;
import org.testah.TS;
import org.testah.client.dto.TestCaseDto;
import org.testah.client.dto.TestPlanDto;
import org.testah.driver.http.requests.AbstractRequestDto;
import org.testah.driver.http.requests.GetRequestDto;
import org.testah.driver.http.requests.PostRequestDto;
import org.testah.driver.http.requests.PutRequestDto;
import org.testah.driver.http.response.ResponseDto;
import org.testah.framework.report.jira.dto.RemoteIssueLinkDto;

import java.util.List;

public class JiraReporter {

    private final String baseUrl;
    private static final String apiUrl = "rest/api/latest";

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
                    remoteLink = getRemoteLinkForGlobalId(relatedId, testPlan.getSource());
                    if (null == remoteLink) {
                        createRemoteLink(relatedId, remoteLinkBuilder.getRemoteLinkForTestPlanResult(testPlan));
                    } else {
                        updateRemoteLink(relatedId, remoteLink.getId(), remoteLinkBuilder.getRemoteLinkForTestPlanResult(testPlan));
                    }
                }
            }
            if (null != testPlan.getKnownProblem()) {
                for (final String relatedId : testPlan.getKnownProblem().getLinkedIds()) {
                    remoteLink = getRemoteLinkForGlobalId(relatedId, testPlan.getSource());
                    if (null == remoteLink) {
                        createRemoteLink(relatedId, remoteLinkBuilder.getRemoteLinkForTestPlanResultKnownProblem(testPlan));
                    } else {
                        updateRemoteLink(relatedId, remoteLink.getId(),
                                remoteLinkBuilder.getRemoteLinkForTestPlanResultKnownProblem(testPlan));
                    }
                }
            }
            if (null != testPlan.getTestCases() && !testPlan.getTestCases().isEmpty()) {
                for (final TestCaseDto testCase : testPlan.getTestCases()) {
                    if (null != testCase.getKnownProblem() && null != testCase.getKnownProblem().getLinkedIds()) {
                        for (final String relatedId : testCase.getKnownProblem().getLinkedIds()) {
                            remoteLink = getRemoteLinkForGlobalId(relatedId, testCase.getSource());
                            if (null == remoteLink) {
                                createRemoteLink(relatedId, remoteLinkBuilder.getRemoteLinkForTestCaseResultKnownProblem(testCase));
                            } else {
                                updateRemoteLink(relatedId, remoteLink.getId(),
                                        remoteLinkBuilder.getRemoteLinkForTestCaseResultKnownProblem(testCase));
                            }
                        }
                    }
                    if (null != testCase.getRelatedIds() && !testCase.getRelatedIds().isEmpty()) {
                        for (final String relatedId : testCase.getRelatedIds()) {
                            remoteLink = getRemoteLinkForGlobalId(relatedId, testCase.getSource());
                            if (null == remoteLink) {
                                createRemoteLink(relatedId, remoteLinkBuilder.getRemoteLinkForTestCaseResult(testCase));
                            } else {
                                updateRemoteLink(relatedId, remoteLink.getId(), remoteLinkBuilder.getRemoteLinkForTestCaseResult(testCase));
                            }
                        }
                    }
                }
            }
        }
    }

    protected void createOrUpdateTestCases(final List<TestPlanDto> testPlan) {

    }

    protected void createOrUpdateTestPlan(final TestPlanDto testPlan) {

    }




    private <T> T addAuthHeader(final AbstractRequestDto<T> request) {
        return request.addBasicAuth(TS.params().getJiraUserName(), TS.params().getJiraPassword());
    }

    /**
     * Get remote links.
     *
     * @param issue the issue
     * @return list of remote issue link
     */
    public List<RemoteIssueLinkDto> getRemoteLinks(final String issue) {
        GetRequestDto get = new GetRequestDto(baseUrl + "/issue/" + issue
                + "/remotelink");
        return TS.http().doRequest(addAuthHeader(get.withJson())).getResponse(new TypeReference<List<RemoteIssueLinkDto>>() {
        });
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
        if (null != remoteLink) {
            PostRequestDto post = new PostRequestDto(baseUrl + "/issue/" + issue + "/remotelink", remoteLink);
            return TS.http().doRequest(addAuthHeader(post.withJson())).getResponse(RemoteIssueLinkDto.class);
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
    public ResponseDto updateRemoteLink(final String issue, final int id, final RemoteIssueLinkDto remoteLink) {
        if (null != remoteLink) {
            PutRequestDto put = new PutRequestDto(baseUrl + "/issue/" + issue + "/remotelink/" + id, remoteLink);
            return TS.http().doRequest(addAuthHeader(put.withJson()));
        }
        return null;
    }

}
