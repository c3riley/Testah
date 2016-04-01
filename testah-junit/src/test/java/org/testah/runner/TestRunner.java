package org.testah.runner;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.testah.TS;
import org.testah.framework.cli.TestFilter;

public class TestRunner {

	@Test
	public void testLoadCompiledTests() {
		final TestFilter r = new TestFilter();
		final Set<Class<?>> classes = r.loadCompiledTestClase().getTestClasses();
		Assert.assertNotNull(classes);
		Assert.assertEquals(5, classes.size());
		TS.util().toJson(classes);
	}
}
