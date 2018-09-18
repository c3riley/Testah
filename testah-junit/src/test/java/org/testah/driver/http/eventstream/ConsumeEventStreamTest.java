package org.testah.driver.http.eventstream;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.testah.util.unittest.dtotest.DtoTest;

public class ConsumeEventStreamTest {

    private ConsumeEventStream ces;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void consumeJson() {
        final String url = "http://testah.com";
        ces = new ConsumeEventStream(url);
        Assert.assertEquals(url, ces.getUrl());
        Assert.assertEquals(true, ces.isVerbose());

        ces = new ConsumeEventStream(url, false);
        Assert.assertEquals(ces.getUrl(), url);
        Assert.assertEquals(false, ces.isVerbose());

        ces = new ConsumeEventStream(url, true);
        Assert.assertEquals(url, ces.getUrl());
        Assert.assertEquals(true, ces.isVerbose());

        ces.setVerbose(true);
        Assert.assertEquals(true, ces.isVerbose());

        ces.setVerbose(false);
        Assert.assertEquals(false, ces.isVerbose());
    }

    @Test
    public void testGettersSetters() throws Exception {
        DtoTest test = new DtoTest();
        test.testGettersAndSetters(new ConsumeEventStream(""));
    }

    @Test
    public void consume() {
    }

    @Test
    public void readData() {
    }

    @Test
    public void processData() {
    }

}