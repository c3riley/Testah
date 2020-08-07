package org.testah.framework.cli;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.testah.TS;

import java.io.File;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;

public class CliTest {

    private static final String ORG_TESTAH = "org.testah";
    private static final String PARAM_LOOK_AT_INTERNAL_TESTS = "param_lookAtInternalTests";

    @Before
    public void setup() {
        System.getProperties().remove(PARAM_LOOK_AT_INTERNAL_TESTS);
    }

    @Test
    public void testCliRun() {
        System.setProperty(PARAM_LOOK_AT_INTERNAL_TESTS, ORG_TESTAH);
        final String[] args = {"run"};
        final Cli cli = new Cli();
        cli.setUnderTest(true);
        cli.getArgumentParser(args);
        Assert.assertThat(cli.getTestPlanFilter().getTestClasses().size(), greaterThanOrEqualTo(50));
        Assert.assertThat(cli.getTestPlanFilter().getTestClassesMetFilters().size(), greaterThanOrEqualTo(46));
    }

    @Test(expected = RuntimeException.class)
    public void testCliRunClassInitializationError() {
        System.setProperty(PARAM_LOOK_AT_INTERNAL_TESTS, "org.testah.framework.cli.initialization");
        final String[] args = {"run"};
        final Cli cli = new Cli();
        cli.getArgumentParser(args);
    }

    @Test
    public void testCliRunWithExternal() {
        System.setProperty(PARAM_LOOK_AT_INTERNAL_TESTS, ORG_TESTAH);
        System.setProperty("param_lookAtExternalTests", "test.groovy");
        final String[] args = {"run"};
        final Cli cli = new Cli();
        cli.setUnderTest(true);
        cli.getArgumentParser(args);
        Assert.assertThat(cli.getTestPlanFilter().getTestClasses().size(), greaterThanOrEqualTo(50));
        Assert.assertThat(cli.getTestPlanFilter().getTestClassesMetFilters().size(), greaterThanOrEqualTo(46));
    }

    @Test
    public void testCliQuery() {
        System.setProperty(PARAM_LOOK_AT_INTERNAL_TESTS, ORG_TESTAH);
        final String[] args = {"query"};
        final Cli cli = new Cli();
        cli.getArgumentParser(args);
        Assert.assertThat(cli.getTestPlanFilter().getTestClasses().size(), greaterThanOrEqualTo(50));
        Assert.assertThat(cli.getTestPlanFilter().getTestClassesMetFilters().size(), greaterThanOrEqualTo(46));
    }

    @Test
    public void testCliQueryWithExternal() {
        System.setProperty(PARAM_LOOK_AT_INTERNAL_TESTS, ORG_TESTAH);
        System.setProperty("param_lookAtExternalTests", "test.groovy");
        final String[] args = {"query"};
        final Cli cli = new Cli();
        cli.getArgumentParser(args);
        Assert.assertThat(cli.getTestPlanFilter().getTestClassesMetFilters().size(), greaterThanOrEqualTo(46));
        Assert.assertThat(cli.getTestPlanFilter().getTestClasses().size(), greaterThanOrEqualTo(50));
    }

    @Test()
    public void testCliQueryWithExternalAndRequireRelatedIdsFound() {
        System.setProperty(PARAM_LOOK_AT_INTERNAL_TESTS, "org.testah.framework.cli.requirerelatedids");
        System.setProperty("param_lookAtExternalTests", "test.groovy");
        final String[] args = {"query", "--includeMeta", "--requireRelatedIds"};
        final Cli cli = new Cli();
        cli.getArgumentParser(args);
        Assert.assertThat(cli.getTestPlanFilter().getTestClassesMetFilters().size(), greaterThanOrEqualTo(1));
        Assert.assertThat(cli.getTestPlanFilter().getTestClasses().size(), greaterThanOrEqualTo(1));
    }

    @Test(expected = RuntimeException.class)
    public void testCliQueryWithExternalAndRequireRelatedIdsNotFound() {
        System.setProperty(PARAM_LOOK_AT_INTERNAL_TESTS, ORG_TESTAH);
        System.setProperty("param_lookAtExternalTests", "test.groovy");
        final String[] args = {"query", "--includeMeta", "--requireRelatedIds"};
        final Cli cli = new Cli();
        cli.getArgumentParser(args);
        Assert.assertThat(cli.getTestPlanFilter().getTestClassesMetFilters().size(), greaterThanOrEqualTo(46));
        Assert.assertThat(cli.getTestPlanFilter().getTestClasses().size(), greaterThanOrEqualTo(50));
    }

    @Test
    public void testCliCreate() {
        final String[] args = {"create"};
        final Cli cli = new Cli();
        cli.getArgumentParser(args);
        Assert.assertTrue(new File("testah.properties").exists());
    }

    @Test
    public void testCliLongBarWithCenterText() {
        final String version = "[Version 22.1.0]";
         String longBarToPrint = Cli.getLongBarWithTextInCenter(version);
        Assert.assertEquals((longBarToPrint.length() + version.length()) / 2 - version.length(), longBarToPrint.indexOf("[") );
    }

    @Test
    public void testCliLongBarWithCenterTextLong() {
        final String version = RandomStringUtils.randomAlphanumeric(Cli.BAR_LONG.length() + 1);
        String longBarToPrint = Cli.getLongBarWithTextInCenter(version);
        Assert.assertEquals(version + "\n" + Cli.BAR_LONG, longBarToPrint);
    }

    @Test
    public void testCliLongBarWithCenterTextShort() {
        final String version = RandomStringUtils.randomAlphanumeric(Cli.BAR_LONG.length() - 1);
        String longBarToPrint = Cli.getLongBarWithTextInCenter(version);
        Assert.assertEquals(version + "\n" + Cli.BAR_LONG, longBarToPrint);
    }

    @Test
    public void testCliLongBarWithCenterTextEmpty() {
        final String version = "";
        String longBarToPrint = Cli.getLongBarWithTextInCenter(version);
        Assert.assertEquals(version + "\n" + Cli.BAR_LONG, longBarToPrint);
    }
}
