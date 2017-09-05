package org.testah.framework.cli;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.hamcrest.CoreMatchers.equalTo;

public class CliTest {

    @Before
    public void setup() {
        System.getProperties().remove("param_lookAtInternalTests");
    }

    @Test
    public void testCliRun() {
        System.setProperty("param_lookAtInternalTests", "org.testah");
        final String[] args = { "run" };
        final Cli cli = new Cli();
        cli.setUnderTest(true);
        cli.getArgumentParser(args);
        Assert.assertThat(cli.getTestPlanFilter().getTestClasses().size(), equalTo(50));
        Assert.assertThat(cli.getTestPlanFilter().getTestClassesMetFilters().size(), equalTo(46));
    }

    @Test
    public void testCliRunWithExternal() {
        System.setProperty("param_lookAtInternalTests", "org.testah");
        System.setProperty("param_lookAtExternalTests", "test.groovy");
        final String[] args = { "run" };
        final Cli cli = new Cli();
        cli.setUnderTest(true);
        cli.getArgumentParser(args);
        Assert.assertThat(cli.getTestPlanFilter().getTestClasses().size(), equalTo(50));
        Assert.assertThat(cli.getTestPlanFilter().getTestClassesMetFilters().size(), equalTo(46));
    }

    @Test
    public void testCliQuery() {
        System.setProperty("param_lookAtInternalTests", "org.testah");
        final String[] args = { "query" };
        final Cli cli = new Cli();
        cli.getArgumentParser(args);
        Assert.assertThat(cli.getTestPlanFilter().getTestClasses().size(), equalTo(50));
        Assert.assertThat(cli.getTestPlanFilter().getTestClassesMetFilters().size(), equalTo(46));
    }

    @Test
    public void testCliQueryWithExternal() {
        System.setProperty("param_lookAtInternalTests", "org.testah");
        System.setProperty("param_lookAtExternalTests", "test.groovy");
        final String[] args = { "query" };
        final Cli cli = new Cli();
        cli.getArgumentParser(args);
        Assert.assertThat(cli.getTestPlanFilter().getTestClassesMetFilters().size(), equalTo(46));
        Assert.assertThat(cli.getTestPlanFilter().getTestClasses().size(), equalTo(50));
    }

    @Test
    public void testCliCreate() {
        final String[] args = { "create" };
        final Cli cli = new Cli();
        cli.getArgumentParser(args);
        Assert.assertTrue(new File("testah.properties").exists());
    }



}
