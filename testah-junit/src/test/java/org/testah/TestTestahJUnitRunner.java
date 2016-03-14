package org.testah;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.testah.framework.dto.ResultDto;
import org.testah.runner.TestahJUnitRunner;

public class TestTestahJUnitRunner {
    
    @Test
    public void testWithOneTest() {
        final TestahJUnitRunner runner = new TestahJUnitRunner();
        final List<Class<?>> lst = new ArrayList<Class<?>>();
        lst.add(TestBrowser.class);
        final List<ResultDto> results = runner.runTests(1, lst);
        Assert.assertNotNull(results);
        Assert.assertEquals(1, results.size());
        
    }
    
    @Test
    public void testWithTwoDifferentTestOnlyOneConcurrent() {
        final TestahJUnitRunner runner = new TestahJUnitRunner();
        final List<Class<?>> lst = new ArrayList<Class<?>>();
        lst.add(TestBrowser.class);
        lst.add(TestHttp.class);
        final List<ResultDto> results = runner.runTests(1, lst);
        Assert.assertNotNull(results);
        Assert.assertEquals(2, results.size());
        
    }

    @Test
    public void testWithTwoDifferentTestOnlyTwoConcurrent() {
        final TestahJUnitRunner runner = new TestahJUnitRunner();
        final List<Class<?>> lst = new ArrayList<Class<?>>();
        lst.add(TestBrowser.class);
        lst.add(TestHttp.class);
        final List<ResultDto> results = runner.runTests(2, lst);
        Assert.assertNotNull(results);
        Assert.assertEquals(2, results.size());
    }
    
    @Test
    public void testWithManyDifferentTestOnlyManyConcurrent() {
        final TestahJUnitRunner runner = new TestahJUnitRunner();
        final List<Class<?>> lst = new ArrayList<Class<?>>();
        lst.add(TestBrowser.class);
        lst.add(TestHttp.class);
        lst.add(TestBrowser.class);
        lst.add(TestHttp.class);
        lst.add(TestBrowser.class);
        lst.add(TestHttp.class);
        lst.add(TestBrowser.class);
        lst.add(TestHttp.class);
        lst.add(TestBrowser.class);
        lst.add(TestHttp.class);
        lst.add(TestBrowser.class);
        lst.add(TestHttp.class);
        lst.add(TestBrowser.class);
        lst.add(TestHttp.class);
        final List<ResultDto> results = runner.runTests(10, lst);
        Assert.assertNotNull(results);
        Assert.assertEquals(14, results.size());
    }
}
