package org.testah.runner;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.testah.framework.cli.Cli;
import org.testah.framework.dto.ResultDto;
import org.testah.http.TestHttp;
import org.testah.web.TestBrowser;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;

public class TestTestahJUnitRunner {

    @Ignore
    @Test
    public void testWithOneTest() {
        final TestahJUnitRunner runner = new TestahJUnitRunner();
        final List<Class<?>> lst = new ArrayList<>();
        lst.add(TestBrowser.class);
        final List<ResultDto> results = runner.runTests(1, lst);
        Assert.assertNotNull(results);
        Assert.assertEquals(1, results.size());

    }

    @Ignore
    @Test
    public void testWithTwoDifferentTestOnlyOneConcurrent() {
        final TestahJUnitRunner runner = new TestahJUnitRunner();
        final List<Class<?>> lst = new ArrayList<>();
        lst.add(TestBrowser.class);
        lst.add(TestHttp.class);
        final List<ResultDto> results = runner.runTests(1, lst);
        Assert.assertNotNull(results);
        Assert.assertEquals(2, results.size());

    }

    @Ignore
    @Test
    public void testWithTwoDifferentTestOnlyTwoConcurrent() {
        final TestahJUnitRunner runner = new TestahJUnitRunner();
        final List<Class<?>> lst = new ArrayList<>();
        lst.add(TestBrowser.class);
        lst.add(TestHttp.class);
        final List<ResultDto> results = runner.runTests(2, lst);
        Assert.assertNotNull(results);
        Assert.assertEquals(2, results.size());
    }

    @Ignore
    @Test
    public void testWithManyDifferentTestOnlyManyConcurrent() {
        final TestahJUnitRunner runner = new TestahJUnitRunner();
        final List<Class<?>> lst = new ArrayList<>();
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
    }

    @Test
    public void testCliRunTests() {

        System.setProperty("param_numConcurrentThreads", "10");
        System.setProperty("param_lookAtInternalTests", "org.testah.runner.runnertests");
        try {
            final String[] args = {"run"};
            final Cli cli = new Cli();
            cli.getArgumentParser(args);
            Assert.assertThat(cli.getTestPlanFilter().getTestClasses().size(), greaterThanOrEqualTo(10));
        } finally {
            System.getProperties().remove("param_lookAtInternalTests");
            System.getProperties().remove("param_numConcurrentThreads");
        }
    }

    @Test
    public void testCliRunTestsSerial() {

        System.setProperty("param_numConcurrentThreads", "1");
        System.setProperty("param_lookAtInternalTests", "org.testah.runner.runnertests");
        try {
            final String[] args = {"run"};
            final Cli cli = new Cli();
            cli.getArgumentParser(args);
            Assert.assertThat(cli.getTestPlanFilter().getTestClasses().size(), greaterThanOrEqualTo(10));
        } finally {
            System.getProperties().remove("param_lookAtInternalTests");
            System.getProperties().remove("param_numConcurrentThreads");
        }
    }
}
