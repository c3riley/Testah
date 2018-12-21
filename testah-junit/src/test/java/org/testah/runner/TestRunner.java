package org.testah.runner;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.testah.TS;
import org.testah.framework.cli.TestFilter;

import java.util.Set;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertThat;

public class TestRunner {

    @Test
    public void testLoadCompiledTests() {
        final TestFilter filter = new TestFilter();
        filter.loadCompiledTestClass("org.testah.runner.runnertests");
        final Set<Class<?>> classes = filter.getTestClasses();
        Assert.assertNotNull(classes);
        assertThat(classes.size(), equalTo(11));
        TS.util().toJson(classes);
    }
}
