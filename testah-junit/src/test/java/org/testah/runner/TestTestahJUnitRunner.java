/*
#
# Copyright (c) 2014-2016 Cazena, Inc., as an unpublished work.
# This notice does not imply unrestricted or public access to these
# materials which are a trade secret of Cazena, Inc. or its
# subsidiaries or affiliates (together referred to as "Cazena"), and
# which may not be copied, reproduced, used, sold or transferred to any
# third party without Cazena's prior written consent.
#
# All rights reserved.
*/
package org.testah.runner;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.testah.framework.dto.ResultDto;
import org.testah.http.TestHttp;
import org.testah.web.TestBrowser;

public class TestTestahJUnitRunner {

    @Ignore
    @Test
    public void testWithOneTest() {
        final TestahJUnitRunner runner = new TestahJUnitRunner();
        final List<Class<?>> lst = new ArrayList<Class<?>>();
        lst.add(TestBrowser.class);
        final List<ResultDto> results = runner.runTests(1, lst);
        Assert.assertNotNull(results);
        Assert.assertEquals(1, results.size());

    }

    @Ignore
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

    @Ignore
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

    @Ignore
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
    }
}
