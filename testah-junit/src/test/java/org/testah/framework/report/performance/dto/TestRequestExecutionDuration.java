package org.testah.framework.report.performance.dto;

import org.junit.Test;
import org.testah.TS;

public class TestRequestExecutionDuration {
    private static final String aggregation = "FAKE_aggregation";
    private static final String domain = "api.dev1.company.com";
    private static final String testClass = "FAKE_test_class";
    private static final String testMethod = "FAKE_test_method";
    private static final String collectionTime = "2017-11-01T13:54:06";
    private static final String timestamp = "2017-11-01T13:45:06";
    private static final String service = "FAKE_SERVICE";
    private static final long duration = 123456789L;
    private static final Integer statusCode = 101;

    @Test
    public void testSingleMode() {
        RequestExecutionDuration requestExecutionDuration = new RequestExecutionDuration(aggregation);
        requestExecutionDuration
            .setCollectionTime(collectionTime)
            .setDomain(domain)
            .setDuration(duration)
            .setService(service)
            .setStatusCode(statusCode)
            .setTestClass(testClass)
            .setTestMethod(testMethod)
            .setTimestamp(timestamp);

        TS.asserts().equalsTo(aggregation, requestExecutionDuration.getAggregation());
        TS.asserts().equalsTo(collectionTime, requestExecutionDuration.getCollectionTime());
        TS.asserts().equalsTo(domain, requestExecutionDuration.getDomain());
        TS.asserts().equalsTo(duration, requestExecutionDuration.getDuration());
        TS.asserts().equalsTo(service, requestExecutionDuration.getService());
        TS.asserts().equalsTo(statusCode, requestExecutionDuration.getStatusCode());
        TS.asserts().equalsTo(testClass, requestExecutionDuration.getTestClass());
        TS.asserts().equalsTo(timestamp, requestExecutionDuration.getTimestamp());
    }

    @Test
    public void testChunkMode() {
        RequestExecutionDuration requestExecutionDuration = new RequestExecutionDuration(aggregation);
        requestExecutionDuration
            .setCollectionTime(collectionTime)
            .setDomain(domain)
            .setDuration(duration)
            .setService(service)
            .setTestClass(testClass)
            .setTestMethod(testMethod);

        TS.asserts().equalsTo(aggregation, requestExecutionDuration.getAggregation());
        TS.asserts().equalsTo(duration, requestExecutionDuration.getDuration());
        TS.asserts().equalsTo(domain, requestExecutionDuration.getDomain());
        TS.asserts().equalsTo(duration, requestExecutionDuration.getDuration());
        TS.asserts().equalsTo(service, requestExecutionDuration.getService());
        TS.asserts().equalsTo(null, requestExecutionDuration.getStatusCode());
        TS.asserts().equalsTo(testClass, requestExecutionDuration.getTestClass());
        TS.asserts().equalsTo(null, requestExecutionDuration.getTimestamp());
    }
}
