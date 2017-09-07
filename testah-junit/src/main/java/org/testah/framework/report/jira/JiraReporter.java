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
    private final static String apiUrl = "rest/api/latest";

    public JiraReporter() {
        if (TS.params().getJiraUrl().endsWith("/")) {
            this.baseUrl = TS.params().getJiraUrl() + apiUrl;
        } else {
            this.baseUrl = TS.params().getJiraUrl() + "/" + apiUrl;
        }
    }

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
                        updateRemoteLink(relatedId, remoteLink.getId(), remoteLinkBuilder.getRemoteLinkForTestPlanResultKnownProblem(testPlan));
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
                    if (null != testCase.getRelatedLinks() && !testCase.getRelatedLinks().isEmpty()) {
                        for (final String relatedId : testCase.getRelatedLinks()) {
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

    private <T> T addAuthHeader(final AbstractRequestDto<T> request) {
        return request.addBasicAuth(TS.params().getJiraUserName(), TS.params().getJiraPassword());
    }

    public List<RemoteIssueLinkDto> getRemoteLinks(final String issue) {
        GetRequestDto get = new GetRequestDto(baseUrl + "/issue/" + issue +
                "/remotelink");
        return TS.http().doRequest(addAuthHeader(get.withJson())).getResponse(new TypeReference<List<RemoteIssueLinkDto>>() {
        });
    }

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

    public RemoteIssueLinkDto createRemoteLink(final String issue, final RemoteIssueLinkDto remoteLink) {
        if (null != remoteLink) {
            PostRequestDto post = new PostRequestDto(baseUrl + "/issue/" + issue + "/remotelink", remoteLink);
            return TS.http().doRequest(addAuthHeader(post.withJson())).getResponse(RemoteIssueLinkDto.class);
        }
        return null;
    }

    public ResponseDto updateRemoteLink(final String issue, final int id, final RemoteIssueLinkDto remoteLink) {
        if (null != remoteLink) {
            PutRequestDto put = new PutRequestDto(baseUrl + "/issue/" + issue + "/remotelink/" + id, remoteLink);
            return TS.http().doRequest(addAuthHeader(put.withJson()));
        }
        return null;
    }

}
