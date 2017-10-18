package org.testah.runner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.testah.TS;
import org.testah.driver.http.requests.GetRequestDto;
import org.testah.driver.http.requests.PostRequestDto;
import org.testah.driver.http.response.ResponseDto;
import org.testah.runner.httpLoad.HttpAkkaStats;

public class TestHttpAkkaRunner {

    @Test
    public void invalidUrl() {
        final HttpAkkaRunner a = HttpAkkaRunner.getInstance();
        a.runAndReport(5, new GetRequestDto("htp:/www.goeeeeogle.com"), 5);
    }

    @Test
    public void wrongUrl() {
        final HttpAkkaRunner a = HttpAkkaRunner.getInstance();
        a.runAndReport(5, new GetRequestDto("http://www.goeeeeogle.com"), 5);
    }

    @Test
    public void happyPath() {
        final HttpAkkaRunner a = HttpAkkaRunner.getInstance();
        a.runAndReport(5, new GetRequestDto("http://www.google.com"), 5);
    }

    @Test
    public void happyPathPostChangingPayload() {
        final int totalNumberOfPosts = 4;
        final String testUrl = "https://httpbin.org/post";
        final String pattern = ".*(xxx[0-9]+xxx).*";
        List<PostRequestDto> postRequests = new ArrayList<>();
        PostRequestDto postRequest;
        String payload;
        String value;
        Set<String> values = new HashSet<>();
        for (int ipost = 0; ipost < totalNumberOfPosts; ipost++) {
            value = "xxx" + ipost + "xxx";
            values.add(value);
            payload = "{\"key\": \"" + value + "\"}";
            postRequest =
                new PostRequestDto(testUrl, payload);
            postRequest.setContentType("application/json");
            postRequests.add(postRequest);
        }

        ConcurrentLinkedQueue<PostRequestDto> concurrentLinkedQueue =
            new ConcurrentLinkedQueue<PostRequestDto>(postRequests);
        List<HttpAkkaStats> statsList = new ArrayList<>();

        final HttpAkkaRunner a = HttpAkkaRunner.getInstance();
        List<ResponseDto> responses = a.runAndReport(2, concurrentLinkedQueue, false);
        statsList.add(new HttpAkkaStats(responses));

        statsList.stream().forEach(stats -> {
            TS.log().info("Elapsed time for calls: " + stats.getDuration());
            TS.asserts().isTrue(stats.getDuration() instanceof Long);
            TS.asserts().isTrue(stats.getDuration() > 0);
            TS.log().info("Average duration of call: " + stats.getAvgDuration());
            TS.asserts().isTrue(stats.getDuration() instanceof Long);
            TS.asserts().isTrue(stats.getDuration() > 0);
            TS.log().info("Number of calls: " + stats.getStatsDuration().getN() + ", 90% = "
                + stats.getStatsDuration().getPercentile(90.0));
            TS.asserts().isTrue(stats.getTotalResponses() == totalNumberOfPosts);
            TS.asserts().isTrue(stats.getStatsDuration().getPercentile(90.0) > 0);
        });


        Pattern p = Pattern.compile(pattern);
        Set<String> responseValues = new HashSet<>();
        for (ResponseDto response : responses) {
            Matcher m = p.matcher(response.getResponseBody().replaceAll("\\s", ""));
            if (m.matches()) {
                responseValues.add(m.group(1));
            }
        }
        TS.asserts().equalsTo(values, responseValues);
    }

    @Test
    public void happyPathGetChangingPath() {
        final int totalNumberOfGets = 4;
        final String testUrl = "https://httpbin.org/get?key=%s";
        final String pattern = ".*(value::[0-9]+xxx).*";

        List<GetRequestDto> getRequests = new ArrayList<>();
        GetRequestDto getRequest;
        String payload;
        String value;
        Set<String> values = new HashSet<>();
        for (int iget = 0; iget < totalNumberOfGets; iget++) {
            value = "value::" + iget + "xxx";
            getRequest = new GetRequestDto(String.format(testUrl, value)).withJson();
            getRequests.add(getRequest);
            values.add(value);
        }

        ConcurrentLinkedQueue<GetRequestDto> concurrentLinkedQueue =
            new ConcurrentLinkedQueue<>(getRequests);
        List<HttpAkkaStats> statsList = new ArrayList<>();

        final HttpAkkaRunner a = HttpAkkaRunner.getInstance();
        List<ResponseDto> responses = a.runAndReport(2, concurrentLinkedQueue, false);
        statsList.add(new HttpAkkaStats(responses));

        statsList.stream().forEach(stats -> {
            TS.log().info("Elapsed time for calls: " + stats.getDuration());
            TS.asserts().isTrue(stats.getDuration() instanceof Long);
            TS.asserts().isTrue(stats.getDuration() > 0);
            TS.log().info("Average duration of call: " + stats.getAvgDuration());
            TS.asserts().isTrue(stats.getDuration() instanceof Long);
            TS.asserts().isTrue(stats.getDuration() > 0);
            TS.log().info("Number of calls: " + stats.getStatsDuration().getN() + ", 90% = "
                + stats.getStatsDuration().getPercentile(90.0));
            TS.asserts().isTrue(stats.getTotalResponses() == totalNumberOfGets);
            TS.asserts().isTrue(stats.getStatsDuration().getPercentile(90.0) > 0);
        });

        Pattern p = Pattern.compile(pattern);
        Set<String> responseValues = new HashSet<>();
        for (ResponseDto response : responses) {
            Matcher m = p.matcher(response.getResponseBody().replaceAll("\\s", ""));
            if (m.matches()) {
                responseValues.add(m.group(1));
            }
        }
        TS.asserts().equalsTo(values, responseValues);
    }
}
