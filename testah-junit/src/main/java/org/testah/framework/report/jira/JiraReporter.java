package org.testah.framework.report.jira;

import java.util.List;

import org.testah.TS;
import org.testah.client.dto.TestPlanDto;
import org.testah.driver.http.requests.AbstractRequestDto;
import org.testah.driver.http.requests.GetRequestDto;
import org.testah.driver.http.requests.PostRequestDto;
import org.testah.driver.http.requests.PutRequestDto;
import org.testah.driver.http.response.ResponseDto;
import org.testah.framework.report.jira.dto.RemoteIssueLinkDto;

import com.fasterxml.jackson.core.type.TypeReference;

public class JiraReporter {

    private final String baseUrl;

    public JiraReporter() {
        this.baseUrl = TS.params().getJiraUrl();
    }

    public void createOrUpdateTestPlanRemoteLink(final TestPlanDto testPlan, final JiraRemoteLinkBuilder remoteLinkBuilder) {
        if (TS.params().isUseJira() && TS.params().getJiraApiUrlBase().length() > 0) {
            RemoteIssueLinkDto remoteLink;
            if (!testPlan.getRelatedIds().isEmpty()) {
                for (final String relatedId : testPlan.getRelatedIds()) {
                    remoteLink = getRemoteLinkForTestPlan(relatedId, testPlan);
                    if (null == remoteLink) {
                        createRemoteLink(relatedId, remoteLinkBuilder.getRemoteLinkForTestPlanResult(testPlan));
                    } else {
                        updateRemoteLink(relatedId, remoteLink.getId(), remoteLinkBuilder.getRemoteLinkForTestPlanResult(testPlan));
                    }
                }
            }
            if (null != testPlan.getKnownProblem()) {
                for (final String relatedId : testPlan.getKnownProblem().getLinkedIds()) {
                    remoteLink = getRemoteLinkForTestPlan(relatedId, testPlan);
                    if (null == remoteLink) {
                        createRemoteLink(relatedId, remoteLinkBuilder.getRemoteLinkForTestPlanResult(testPlan));
                    } else {
                        updateRemoteLink(relatedId, remoteLink.getId(), remoteLinkBuilder.getRemoteLinkForTestPlanResult(testPlan));
                    }
                }
            }
        }
    }

    private AbstractRequestDto addAuthHeader(final AbstractRequestDto request) {
        return request.addBasicAuth(TS.params().getJiraUserName(), TS.params().getJiraPassword());
    }

    public List<RemoteIssueLinkDto> getRemoteLinks(final String issue) {
        GetRequestDto get = new GetRequestDto(baseUrl + "/issue/" + issue +
                "/remotelink");
        return TS.http().doRequest(addAuthHeader(get)).getResponse(new TypeReference<List<RemoteIssueLinkDto>>() {
        });
    }

    public RemoteIssueLinkDto getRemoteLinkForTestPlan(final String issue, final TestPlanDto testPlan) {
        for (RemoteIssueLinkDto link : getRemoteLinks(issue)) {
            if (link.getGlobalId().equals(testPlan.getSource())) {
                return link;
            }
        }
        return null;
    }

    public RemoteIssueLinkDto createRemoteLink(final String issue, final RemoteIssueLinkDto remoteLink) {
        PostRequestDto post = new PostRequestDto(baseUrl + "/issue/" + issue + "/remotelink", remoteLink);
        return TS.http().doRequest(addAuthHeader(post)).getResponse(RemoteIssueLinkDto.class);
    }

    public ResponseDto updateRemoteLink(final String issue, final int id, final RemoteIssueLinkDto remoteLink) {
        PutRequestDto put = new PutRequestDto(baseUrl + "/issue/" + issue + "/remotelink/" + id, remoteLink);
        return TS.http().doRequest(addAuthHeader(put));
    }

}
