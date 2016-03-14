package org.testah;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.testah.framework.cli.TestPlanFilter;

public class TestRunner {

	@Test
	public void testLoadCompiledTests() {
		final TestPlanFilter r = new TestPlanFilter();
		final Set<Class<?>> classes = r.loadCompiledTestClase().getTestClasses();
		Assert.assertNotNull(classes);
		Assert.assertEquals(3, classes.size());
		TS.util().toJson(classes);
	}
}
