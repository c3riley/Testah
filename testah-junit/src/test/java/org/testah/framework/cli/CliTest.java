package org.testah.framework.cli;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;

public class CliTest {

    private static final String ORG_TESTAH = "org.testah";
    private static final String PARAM_LOOK_AT_INTERNAL_TESTS = "param_lookAtInternalTests";
    private static final String FILTER_BY_TAG = "filter_DEFAULT_filterByTag";
    public static final String OVERRIDE_JUNIT_5 = "OVERRIDE_JUNIT5";

    @Before
    public void setup() {
        System.clearProperty(PARAM_LOOK_AT_INTERNAL_TESTS);
        System.clearProperty(FILTER_BY_TAG);
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
    public void testCliRunJUnit4() {
        final String[] args = {"run"};
        System.setProperty(FILTER_BY_TAG, "JUNIT_4");
        final Cli cli = new Cli();
        cli.getArgumentParser(args);
    }

    @Test
    public void testCliRunJUnit5() {
        final String[] args = {"run"};
        System.setProperty(FILTER_BY_TAG, "JUNIT_5");
        final Cli cli = new Cli();
        cli.getArgumentParser(args);
    }

    @Test
    public void testCliLongBarWithCenterText() {
        final String version = "[Version 22.1.0]";
        String longBarToPrint = Cli.getLongBarWithTextInCenter(version);
        Assert.assertEquals((longBarToPrint.length() + version.length()) / 2 - version.length(), longBarToPrint.indexOf("["));
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

    @Test
    public void testInitializationError() {
        final String[] args = {"run"};
        boolean found = false;
        System.setProperty(FILTER_BY_TAG, "JUNIT_5_NEG");
        System.setProperty(OVERRIDE_JUNIT_5, "true");
        Cli cli = new Cli();
        try {
            cli.getArgumentParser(args);
        } catch (RuntimeException e) {
            found =
                cli.getResults().stream().anyMatch(
                    resultDto -> resultDto.getTestPlan().getTestCases().get(0).getDescription().contains("does not exist")
                );
        } finally {
            System.clearProperty(FILTER_BY_TAG);
            System.clearProperty(OVERRIDE_JUNIT_5);
        }
        Assert.assertTrue(found);
    }
}
