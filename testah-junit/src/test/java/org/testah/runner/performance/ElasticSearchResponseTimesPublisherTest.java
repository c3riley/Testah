package org.testah.runner.performance;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testah.util.unittest.dtotest.DtoTest;

import static org.junit.Assert.*;

public class ElasticSearchResponseTimesPublisherTest {

    private DtoTest test;

    @Before
    public void setUp() throws Exception {
        test = new DtoTest();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getDateTimeStringTest() throws Exception {
        String baseUrl = "baseUrl";
        String index = "index";
        String type = "type";
        String username = "username";
        String password = "password";
        TestRunProperties runProps = new TestRunProperties(
            "service", "testClass", "testMethod");

        test.addToIgnoredGetFields("getUploadUrl");
        test.testGettersAndSetters(new ElasticSearchResponseTimesPublisher(
            baseUrl, index, type, username, password, runProps));
    }

    @Test
    public void getDateTimeString1Test() {
    }

    @Test
    public void pushTest() {
    }

    @Test
    public void cleanupTest() {
    }

    @Test
    public void getUploadUrlTest() {
    }

    @Test
    public void getUrlPathUploadTest() {
    }

    @Test
    public void getBaseUrlTest() {
    }

    @Test
    public void setBaseUrlTest() {
    }

    @Test
    public void getVerboseTest() {
    }

    @Test
    public void setVerboseTest() {
    }

    @Test
    public void getIndexTest() {
    }

    @Test
    public void setIndexTest() {
    }
}