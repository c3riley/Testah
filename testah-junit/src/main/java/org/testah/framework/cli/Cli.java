package org.testah.framework.cli;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.Subparsers;
import org.apache.commons.io.FileUtils;
import org.testah.TS;
import org.testah.client.dto.TestPlanDto;
import org.testah.client.enums.BrowserType;
import org.testah.framework.annotations.KnownProblem;
import org.testah.framework.annotations.TestCase;
import org.testah.framework.annotations.TestPlan;
import org.testah.framework.dto.ResultDto;
import org.testah.framework.dto.TestDtoHelper;
import org.testah.framework.report.SummaryHtmlFormatter;
import org.testah.framework.report.TestPlanReporter;
import org.testah.framework.testPlan.AbstractTestPlan;
import org.testah.runner.TestahJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

import static net.sourceforge.argparse4j.impl.Arguments.enumStringType;

/**
 * The Class Cli.
 */
public class Cli {

    /** The res. */
    private Namespace res;

    /** The param loader. */
    private final ParamLoader paramLoader;

    /** The opt. */
    private Params opt;

    /** The Constant version. */
    public static final String version = "0.8.2";

    /** The Constant BAR_LONG. */
    public static final String BAR_LONG = "=============================================================================================";

    /** The Constant BAR_SHORT. */
    public static final String BAR_SHORT = "=========================================";

    /** The Constant BAR_WALL. */
    public static final String BAR_WALL = "# ";

    private TestFilter testPlanFilter;

    private boolean underTest = false;

    /**
     * Instantiates a new cli.
     */
    public Cli() {

        String propFilePath = System.getenv("TESTAH_PROP");
        if (null == propFilePath) {
            propFilePath = System.getProperty("TESTAH_PROP", ParamLoader.getDefaultPropFilePath());
        }

        this.paramLoader = new ParamLoader(propFilePath);
        this.opt = new ParamLoader(propFilePath).loadParamValues();

    }

    public Subparser addOptions(final Subparser sub) {

        return sub;
    }

    /**
     * Gets the argument parser.
     *
     * @param args
     *            the args
     * @return the argument parser
     */
    public Cli getArgumentParser(final String[] args) {

        final ArgumentParser parser = ArgumentParsers.newArgumentParser("Testah").defaultHelp(true)
                .description("Testah CLI running Automated Tests for Browser and Http").epilog("").version(version);

        parser.addArgument("-v", "--version").action(Arguments.version()).setDefault(version);

        final Subparsers subparsers = parser.addSubparsers().help("sub-command help").dest("subparserName");

        final Subparser run = subparsers.addParser("run").help("run help");
        run.addArgument("-b", "--browser").setDefault(opt.getBrowser()).type(enumStringType(BrowserType.class))
                .help("foo help");
        run.addArgument("-t", "--test").setDefault("").type(String.class)
                .help("Deprecated - Same as --lookAtExternalTests");
        run.addArgument("-i", "--lookAtInternalTests").setDefault(opt.getLookAtInternalTests()).type(String.class)
                .help("lookAtInternalTests, example org.testah, will look at all tests under this package");
        run.addArgument("-e", "--lookAtExternalTests").setDefault(opt.getLookAtExternalTests()).type(String.class).help(
                "lookAtExternalTests is a path to a test file, java or groovy, or a comma seperated like, regex, for directory path");

        final Subparser query = subparsers.addParser("query").help("query help");
        query.addArgument("--file").required(false).action(Arguments.store()).dest("queryResults")
                .setDefault(Params.getUserDir());
        query.addArgument("--includeMeta").required(false).action(Arguments.storeTrue()).dest("includeMeta");
        query.addArgument("--show").required(false).action(Arguments.storeTrue()).dest("showInConsole");
        query.addArgument("-i", "--lookAtInternalTests").setDefault(opt.getLookAtInternalTests()).type(String.class)
                .help("lookAtInternalTests, example org.testah, will look at all tests under this package");
        query.addArgument("-e", "--lookAtExternalTests").setDefault(opt.getLookAtExternalTests()).type(String.class)
                .help("lookAtExternalTests is a path to a test file, java or groovy, or a comma seperated like, regex, for directory path");

        final Subparser create = subparsers.addParser("create").help("create help");
        create.addArgument("--prop", "--properties").required(false).action(Arguments.storeTrue()).dest("prop");
        create.addArgument("--guid").required(false).action(Arguments.storeTrue()).dest("guid");

        writeOutTestah();

        try {
            if (null != args) {
                res = parser.parseArgs(args);
                parser.parseArgs(args, opt);
                TS.setParams(opt);
                String externalTest = res.getString("test");
                if (null == externalTest || externalTest.length() == 0) {
                    externalTest = res.getString("lookAtExternalTests");
                }

                TS.params().setLookAtInternalTests(res.getString("lookAtInternalTests"));
                TS.params().setLookAtExternalTests(externalTest);

                TS.log().debug(Cli.BAR_LONG);
                TS.log().info(Cli.BAR_WALL + "CLI Inputs - " + res);
                TS.log().debug(Cli.BAR_LONG);
                final String subProcess = res.getString("subparserName");
                if (null != res.getString("subparserName")) {
                    try {
                        if (subProcess.equalsIgnoreCase("run")) {

                            processRun();

                        } else if (subProcess.equalsIgnoreCase("query")) {

                            processQuery();

                        } else if (subProcess.equalsIgnoreCase("create")) {

                            processCreate();

                        } else {
                            throw new RuntimeException("Unknown SubParser: " + subProcess);
                        }
                    } catch (final Exception e) {
                        throw new RuntimeException(e);
                    }
                }

            } else {
                TS.log().debug(Cli.BAR_LONG);
                TS.log().debug(Cli.BAR_WALL + "Not using cli params, only loading from properties file [ "
                        + ParamLoader.getDefaultPropFilePath() + " ]");
                TS.log().debug(Cli.BAR_LONG);
            }

        } catch (final ArgumentParserException e) {
            parser.handleError(e);
            throw new RuntimeException(e);
        }
        return this;

    }

    /**
     * Process create.
     */
    public void processCreate() {
        if (res.getBoolean("prop")) {
            paramLoader.overwriteDefaultConfig();
        }
    }

    /**
     * Process run.
     */
    public void processRun() {

        this.setTestPlanFilter(new TestFilter());
        getTestPlanFilter().filterTestPlansToRun();

        final TestahJUnitRunner junitRunner = new TestahJUnitRunner();
        if (isUnderTest()) {
            return;
        }
        final List<ResultDto> results = junitRunner.runTests(TS.params().getNumConcurrentThreads(),
                getTestPlanFilter().getTestClassesMetFilters());

        int totalTestCases = 0;
        int totalTestCasesFailed = 0;
        int totalTestCasesPassed = 0;
        int totalTestCasesIgnored = 0;
        int totalTestPlans = 0;

        if (null != results) {
            totalTestPlans = results.size();
            TS.log().info(Cli.BAR_LONG);
            TS.log().info(Cli.BAR_WALL + "TestPlan Result(s):");

            TS.util().pause(1000L, "Waiting for TestsPlans to complete");
            for (final ResultDto result : results) {

                if (null != result.getTestPlan()) {
                    result.getTestPlan().getRunInfo().recalc(result.getTestPlan());
                    totalTestCases += result.getTestPlan().getRunInfo().getTotal();
                    totalTestCasesFailed += result.getTestPlan().getRunInfo().getFail();
                    totalTestCasesPassed += result.getTestPlan().getRunInfo().getPass();
                    totalTestCasesIgnored += result.getTestPlan().getRunInfo().getIgnore();
                } else {
                    TS.log().error("Testplan is null, for " + result.getJunitResult().getFailures());
                }
            }
            for (final ResultDto result : results) {
                if (null != result.getTestPlan()) {
                    TS.getTestPlanReporter().reportResults(result.getTestPlan(), false, this.opt.getOutput());
                }
            }

            for (final ResultDto result : results) {
                if (null != result.getTestPlan()) {
                    System.out.println("" + result.getTestPlan().getRunInfo().getReportFilePath().get("html"));
                }
            }
        }

        TS.log().info(Cli.BAR_LONG);
        TS.log().info(Cli.BAR_WALL + "Overall Results:");
        TS.log().info(Cli.BAR_LONG);
        TS.log().info(Cli.BAR_WALL + "Total TestPlans: " + totalTestPlans);
        TS.log().info(Cli.BAR_WALL + "Total TestCases: " + totalTestCases);
        TS.log().info(Cli.BAR_WALL + "Total TestCases Failed: " + totalTestCasesFailed);
        TS.log().info(Cli.BAR_WALL + "Total TestCases Passed: " + totalTestCasesPassed);
        TS.log().info(Cli.BAR_WALL + "Total TestCases Ignored: " + totalTestCasesIgnored);
        TS.log().info(Cli.BAR_LONG);

        File summaryHtml = new SummaryHtmlFormatter(results).createReport().getReportFile();
        if(TS.params().isAutoOpenHtmlReport()) {
            new TestPlanReporter().openReport(summaryHtml.getAbsolutePath());
        }
        AbstractTestPlan.tearDownTestah();
    }

    /**
     * Process query.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public void processQuery() throws IOException {
        this.setTestPlanFilter(new TestFilter());
        File results = new File(res.getString("queryResults"));
        if (results.isDirectory()) {
            TS.log().trace("results mkdirs: " + results.mkdirs());
            results = new File(results, "queryResults.json");
        } else {
            if (null != results.getParentFile()) {
                TS.log().trace("results.getParentFile() mkdirs: " + results.getParentFile().mkdirs());
            }
        }
        getTestPlanFilter().filterTestPlansToRun();
        Object resultObject = testPlanFilter.getTestClassesMetFilters();
        if (res.getBoolean("includeMeta")) {
            final HashMap<String, TestPlanDto> testPlans = new HashMap<>();
            for (final Class<?> test : getTestPlanFilter().getTestClassesMetFilters()) {
                testPlans
                        .put(test.getCanonicalName(),
                                TestDtoHelper
                                        .createTestPlanDto(test, test.getAnnotation(TestPlan.class),
                                                test.getAnnotation(KnownProblem.class))
                                        .setRunTime(null).setRunInfo(null));

                for (final Method method : test.getDeclaredMethods()) {
                    if (null != method.getAnnotation(TestCase.class)) {
                        testPlans.get(test.getCanonicalName())
                                .addTestCase(TestDtoHelper.createTestCaseDto(test.getCanonicalName(), method.getName(),
                                        method.getAnnotation(TestCase.class), method.getAnnotation(KnownProblem.class),
                                        test.getAnnotation(TestPlan.class)).setRunTime(null));
                    }
                }
            }
            resultObject = testPlans;
        }

        FileUtils.writeStringToFile(results, TS.util().toJson(resultObject));
        TS.log().info("Query Results: Found[" + getTestPlanFilter().getTestClassesMetFilters().size() + "] "
                + results.getAbsolutePath());

        if (res.getBoolean("showInConsole")) {
            TS.log().info(TS.util().toJson(resultObject));
        }

    }

    /**
     * Gets the res.
     *
     * @return the res
     */
    public Namespace getRes() {
        return res;
    }

    /**
     * Sets the res.
     *
     * @param res
     *            the new res
     * @return the cli
     */
    public Cli setRes(final Namespace res) {
        this.res = res;
        return this;
    }

    /**
     * Gets the param loader.
     *
     * @return the param loader
     */
    public ParamLoader getParamLoader() {
        return paramLoader;
    }

    /**
     * Gets the opt.
     *
     * @return the opt
     */
    public Params getOpt() {
        return opt;
    }

    public Cli setOpt(final Params opt) {
        this.opt = opt;
        return this;
    }

    public TestFilter getTestPlanFilter() {
        return testPlanFilter;
    }

    public Cli setTestPlanFilter(final TestFilter testPlanFilter) {
        this.testPlanFilter = testPlanFilter;
        return this;
    }

    /**
     * Write out testah.
     */
    public static void writeOutTestah() {
        System.out.println("\n" + Cli.BAR_LONG);
        System.out.println(
                "      _____________                    _                                         ============");
        System.out.println(
                "     /_____  _____/      ________     | |                                       =======/\\==||");
        System.out.println(
                "          | |           /___ ___/     | |                                      =======/  \\=||");
        System.out.println(
                "          | |   ___    __  | |        | |                                     =======/    \\||");
        System.out.println(
                "          | |  / __|  (    | |  ___   | |___                                  =======\\    /||");
        System.out.println(
                "          | |  |       \\   | | / \\ \\  |  __ \\                                  =======\\  /=||");
        System.out.println(
                "          |_|  \\____  __)  |_| \\__\\_\\ |_|  |_|                                  =======\\/==||");
        System.out.println("=======================================================[Version: " + Cli.version
                + "]======================");

        System.out.println(Cli.BAR_LONG);
    }

    public boolean isUnderTest() {
        return underTest;
    }

    public Cli setUnderTest(final boolean underTest) {
        this.underTest = underTest;
        return this;
    }
}
