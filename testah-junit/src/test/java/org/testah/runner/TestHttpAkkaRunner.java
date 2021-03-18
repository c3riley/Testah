package org.testah.runner;

import org.junit.Before;
import org.junit.Test;
import org.testah.TS;
import org.testah.driver.http.HttpWrapperV2;
import org.testah.driver.http.requests.GetRequestDto;
import org.testah.driver.http.requests.PostRequestDto;
import org.testah.driver.http.response.ResponseDto;
import org.testah.runner.http.load.HttpAkkaStats;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class TestHttpAkkaRunner {

    @Before
    public void setup() {
        HttpAkkaRunner.reset();
    }

    @Test
    public void testHttpWrapper() {
        HttpWrapperV2 http = new HttpWrapperV2();
        HttpAkkaRunner.getInstance().setHttpWrapper(http);
        assertThat(HttpAkkaRunner.getInstance().getHttpWrapper(), is(http));

        HttpAkkaRunner.getInstance().setHttpWrapper(null);
        assertThat(HttpAkkaRunner.getInstance().getHttpWrapper(), notNullValue());
        assertThat(HttpAkkaRunner.getInstance().getHttpWrapper(), not(http));
        assertThat(HttpAkkaRunner.getInstance().getHttpWrapper(), instanceOf(HttpWrapperV2.class));
    }

    @Test
    public void invalidUrl() {
        final HttpAkkaRunner akkaRunner = HttpAkkaRunner.getInstance();
        akkaRunner.runAndReport(5, new GetRequestDto("htp:/www.goeeeeogle.com"), 5);
        akkaRunner.waitForResponses(new LinkedBlockingQueue<ResponseDto>(), 5, Duration.ofSeconds(5).toMillis());
    }

    @Test
    public void wrongUrl() {
        final HttpAkkaRunner akkaRunner = HttpAkkaRunner.getInstance();
        akkaRunner.runAndReport(5, new GetRequestDto("http://www.goeeeeogle.com"), 5);
        akkaRunner.waitForResponses(new LinkedBlockingQueue<ResponseDto>(), 5, Duration.ofSeconds(5).toMillis());
    }

    @Test
    public void happyPath() {
        final HttpAkkaRunner akkaRunner = HttpAkkaRunner.getInstance();
        akkaRunner.runAndReport(5, new GetRequestDto("http://www.google.com"), 5);
        akkaRunner.waitForResponses(new LinkedBlockingQueue<ResponseDto>(), 5, Duration.ofSeconds(5).toMillis());
    }

    @Test
    public void happyPathPostChangingPayload() {
        final int totalNumberOfPosts = 4;
        final String testUrl = "https://httpbin.org/post";
        final String regexString = ".*(xxx[0-9]+xxx).*";
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
            new ConcurrentLinkedQueue<>(postRequests);
        List<HttpAkkaStats> statsList = new ArrayList<>();

        final HttpAkkaRunner akkaRunner = HttpAkkaRunner.getInstance();
        LinkedBlockingQueue<ResponseDto> responseQueue = new LinkedBlockingQueue<>();
        akkaRunner.runAndReport(responseQueue, 2, concurrentLinkedQueue, true);
        akkaRunner.waitForResponses(responseQueue, totalNumberOfPosts, Duration.ofSeconds(5).toMillis());

        List<ResponseDto> responseList = new ArrayList<>();
        responseQueue.drainTo(responseList);
        statsList.add(new HttpAkkaStats(responseList));

        statsList.stream().forEach(stats -> {
            TS.asserts().isGreaterThan("check average duration", 0, stats.getDuration());
            TS.asserts().equalsTo("check number of posts", totalNumberOfPosts, stats.getTotalResponses());
            TS.asserts().isTrue("check duration percentile", stats.getStatsDuration().getPercentile(90.0) > 0);
        });

        Pattern pattern = Pattern.compile(regexString);
        Set<String> responseValues = new HashSet<>();
        for (ResponseDto response : responseList) {
            Matcher matcher = pattern.matcher(response.getResponseBody().replaceAll("\\s", ""));
            if (matcher.matches()) {
                responseValues.add(matcher.group(1));
            }
        }
        TS.asserts().equalsTo(values, responseValues);
    }

    @Test
    public void happyPathGetChangingPath() {
        final int totalNumberOfGets = 4;
        final String testUrl = "https://httpbin.org/get?key=%s";
        final String regexString = ".*(value::[0-9]+xxx).*";

        List<GetRequestDto> getRequests = new ArrayList<>();
        GetRequestDto getRequest;
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

        final HttpAkkaRunner akkaRunner = HttpAkkaRunner.getInstance();
        LinkedBlockingQueue<ResponseDto> responseQueue = new LinkedBlockingQueue<>();
        akkaRunner.runAndReport(responseQueue, 2, concurrentLinkedQueue, false);
        akkaRunner.waitForResponses(responseQueue, 4, Duration.ofSeconds(10).toMillis());

        List<ResponseDto> responseList = new ArrayList<>();
        responseQueue.drainTo(responseList);
        statsList.add(new HttpAkkaStats(responseList));

        statsList.stream().forEach(stats -> {
            TS.log().info("Elapsed time for calls: " + stats.getDuration());
            TS.asserts().isTrue(stats.getDuration() instanceof Long);
            TS.asserts().isTrue(stats.getDuration() > 0);
            TS.log().info("Average duration of call: " + stats.getAvgDuration());
            TS.asserts().isTrue(stats.getDuration() instanceof Long);
            TS.asserts().isTrue(stats.getDuration() > 0);
            TS.log().info("Number of calls: " + stats.getStatsDuration().getN() + ", 90% = " +
                stats.getStatsDuration().getPercentile(90.0));
            TS.asserts().isTrue(stats.getTotalResponses() == totalNumberOfGets);
            TS.asserts().isTrue(stats.getStatsDuration().getPercentile(90.0) > 0);
        });

        Pattern pattern = Pattern.compile(regexString);
        Set<String> responseValues = new HashSet<>();
        for (ResponseDto response : responseList) {
            Matcher matcher = pattern.matcher(response.getResponseBody().replaceAll("\\s", ""));
            if (matcher.matches()) {
                responseValues.add(matcher.group(1));
            }
        }
        TS.asserts().equalsTo(values, responseValues);
    }

    @Test
    public void runTestsTestWithBadValue() {
        final HttpAkkaRunner akkaRunner = HttpAkkaRunner.getInstance();
        LinkedBlockingQueue<ResponseDto> responseQueue = new LinkedBlockingQueue<>();
        akkaRunner.runTests(responseQueue, 0, null, true);
        TS.asserts().equalsTo(responseQueue.size(), 0);
        responseQueue.clear();

        akkaRunner.runTests(responseQueue, 0, new ConcurrentLinkedQueue(), true);
        TS.asserts().equalsTo(responseQueue.size(), 0);
        responseQueue.clear();

        TS.asserts().assertThat(akkaRunner.runTests(0, null, 0), nullValue());
    }

    @Test(expected = ExceptionInInitializerError.class)
    public void runTestsTestWithError() {
        final HttpAkkaRunner akkaRunner = spy(HttpAkkaRunner.getInstance());
        when(akkaRunner.getActorSystem()).thenThrow(new ExceptionInInitializerError());
        akkaRunner.runTests(-1, new GetRequestDto("exit"), -1);
    }
}
