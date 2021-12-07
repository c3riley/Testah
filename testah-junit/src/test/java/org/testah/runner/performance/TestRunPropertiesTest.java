package org.testah.runner.performance;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.time.ZoneId;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class TestRunPropertiesTest {

    private TestRunProperties properties;

    @Before
    public void setUp() throws Exception {
        properties = new TestRunProperties("unitTestService", "testClassName", "testMethodName");
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testConstructorFields() {
        assertThat(properties.getServiceUnderTest(), equalTo("unitTestService"));
        assertThat(properties.getTestClass(), equalTo("testClassName"));
        assertThat(properties.getTestMethod(), equalTo("testMethodName"));
    }

    @Test
    public void testConstructorFields2() {
        properties = new TestRunProperties("unitTestService",
            "testClassName", "testMethodName", 20, 200L);
        assertThat(properties.getNumberOfAkkaThreads(), equalTo(20));
        assertThat(properties.getMillisBetweenChunks(), equalTo(200L));
    }

    @Test
    public void setNumberOfChunks1Test() {
        assertThat(properties.getNumberOfChunks(), equalTo(2500));
        properties.setNumberOfChunks(2);
        assertThat(properties.getNumberOfChunks(), equalTo(2));
        properties.setNumberOfChunks("3");
        assertThat(properties.getNumberOfChunks(), equalTo(3));
    }

    @Test
    public void chunkSizeTest() {
        assertThat(properties.getChunkSize(), equalTo(10));
        properties.setChunkSize(2);
        assertThat(properties.getChunkSize(), equalTo(2));
        properties.setChunkSize("3");
        assertThat(properties.getChunkSize(), equalTo(3));
    }

    @Test
    public void numberOfAkkaThreadsTest() {
        assertThat(properties.getNumberOfAkkaThreads(), equalTo(3));
        properties.setNumberOfAkkaThreads(2);
        assertThat(properties.getNumberOfAkkaThreads(), equalTo(2));
        properties.setNumberOfAkkaThreads("3");
        assertThat(properties.getNumberOfAkkaThreads(), equalTo(3));
    }

    @Test
    public void millisBetweenChunksTest() {
        assertThat(properties.getMillisBetweenChunks(), equalTo(3000L));
        properties.setMillisBetweenChunks(2L);
        assertThat(properties.getMillisBetweenChunks(), equalTo(2L));
        properties.setMillisBetweenChunks("3");
        assertThat(properties.getMillisBetweenChunks(), equalTo(3L));
    }

    @Test
    public void verboseTest() {
        assertThat(properties.isVerbose(), is(false));
        properties.setVerbose(true);
        assertThat(properties.isVerbose(), is(true));
    }

    @Test
    public void getDomainTest() {
        assertThat(properties.getDomain(), nullValue());
        properties.setDomain("this.com");
        assertThat(properties.getDomain(), equalTo("this.com"));
    }

    @Test
    public void getRuntimeTest() {
        assertThat(properties.getRuntime(), equalTo("PT48H"));

        properties.setRunDuration(100L);
        assertThat(properties.getRuntime(), equalTo("PT0.1S"));
        assertThat(properties.getRunDuration(), equalTo(100L));
        properties.setRunDuration("200");
        assertThat(properties.getRuntime(), equalTo("PT0.2S"));
        assertThat(properties.getRunDuration(), equalTo(200L));
    }

    @Test
    public void toStringTest() {
        assertThat(properties.toString(), notNullValue());
    }

    @Test
    public void getStopDateTimeTest() {
        assertThat(properties.getStopTime(), greaterThanOrEqualTo(1L));
        assertThat(properties.getStopDateTime(), equalTo(Instant.ofEpochMilli(properties.getStopTime())
            .atZone(ZoneId.systemDefault()).toLocalDateTime()));
    }

}