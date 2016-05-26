
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
