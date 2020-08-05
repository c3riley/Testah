package org.testah.framework.cli;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.testah.TS;
import org.testah.client.dto.TestCaseDto;
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
import java.nio.charset.Charset;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static net.sourceforge.argparse4j.impl.Arguments.enumStringType;

/**
 * The Class Cli.
 */
public class Cli {

    /**
     * The Constant version.
     */

    public static final String version = "2.3.1";

    /**
     * The Constant BAR_LONG.
     */
    public static final String BAR_LONG =
        "=================================================================================================";
    /**
     * The Constant BAR_SHORT.
     */
    public static final String BAR_SHORT = "=========================================";
    /**
     * The Constant BAR_WALL.
     */
    public static final String BAR_WALL = "# ";
    private static boolean running = false;
    /**
     * The param loader.
     */
    private final ParamLoader paramLoader;
    /**
     * The res.
     */
    private Namespace res;
    /**
     * The opt.
     */
    private Params opt;
    private TestFilter testPlanFilter;
    private boolean underTest = false;

    List<ResultDto> results = null;

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

    /**
     * Is running boolean indicates if cli is processing a request.
     *
     * @return the boolean
     */
    public static boolean isRunning() {
        return running;
    }

    /**
     * Sets running.
     *
     * @param running the running
     */
    private static void setRunning(final boolean running) {
        Cli.running = running;
    }

    /**
     * Write out testah banner.
     */
    public static void writeOutTestah() {
        final String versionString = "[Version: " + Cli.version + "]";
        final int startIndex = (Cli.BAR_LONG.length() / 2) - (versionString.length() / 2);
        final String printVersionLine = Cli.BAR_LONG.substring(0, startIndex) + versionString +
            Cli.BAR_LONG.substring(startIndex + versionString.length());
        System.out.println("\n" + Cli.BAR_LONG + "\n");
        System.out.println("TTTTTTTTTTTTTTTTTTTT                          TTTTTTTTTTTTTTTTTTTT              hhhhhh");
        System.out.println("T:::::TT::::TT:::::T                          T:::::TT::::TT:::::T              h::::h");
        System.out.println("TTTTT  T::::T  TTTTTTeeeeeeeeee       ssssssssTTTTT  T::::T  TTTTTaaaaaaaaaaa   h::::h hhhh");
        System.out.println("       T::::T     e:::::eeee::::e    s:::::::::s     T::::T       aaaaaaa:::::a h:::::::::::hh");
        System.out.println("       T::::T    e:::::e    e:::::  s:::::sss:::::s  T::::T              a::::a h::::::hh:::::h");
        System.out.println("       T::::T   e::::::eeee::::::e s::::s    sssss   T::::T        aaaaaa:::::a h:::::h   h:::::h");
        System.out.println("       T::::T  e::::::::::::::::e    s:::::s         T::::T      aa:::::::::::a h::::h     h::::h");
        System.out.println("       T::::T   e::::::eeeeeeeee        s:::::s      T::::T     a::::aaa::::::a h::::h     h::::h");
        System.out.println("       T::::T    e:::::e          sssss     s::::s   T::::T    a::::a    a::::a h::::h     h::::h");
        System.out.println("     T::::::::T   e:::::::eeeeeee   s:::::sss:::::sT::::::::T  a:::::aaa::::::a h::::h     h::::h");
        System.out.println("     TTTTTTTTTT      eeeeeeeeeeee     sssssssss    TTTTTTTTTT    aaaaaaaaa  aaaahhhhhh     hhhhhh");
        System.out.println("\n" + printVersionLine + "\n");
    }

    /**
     * Add options subparser.
     *
     * @param sub the sub
     * @return the subparser
     */
    public Subparser addOptions(final Subparser sub) {

        return sub;
    }

    /**
     * Gets the argument parser.
     *
     * @param args the args
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
                "lookAtExternalTests is a path to a test file, java or groovy, or a comma separated like, regex, for directory path");

        final Subparser query = subparsers.addParser("query").help("query help");
        query.addArgument("--file").required(false).action(Arguments.store()).dest("queryResults")
                .setDefault(Params.getUserDir());
        query.addArgument("--includeMeta").required(false).action(Arguments.storeTrue()).dest("includeMeta");
        query.addArgument("--requireRelatedIds").required(false).action(Arguments.storeTrue()).dest("requireRelatedIds");
        query.addArgument("--show").required(false).action(Arguments.storeTrue()).dest("showInConsole");
        query.addArgument("-i", "--lookAtInternalTests").setDefault(opt.getLookAtInternalTests()).type(String.class)
                .help("lookAtInternalTests, example org.testah, will look at all tests under this package");
        query.addArgument("-e", "--lookAtExternalTests").setDefault(opt.getLookAtExternalTests()).type(String.class)
                .help("lookAtExternalTests is a path to a test file, java or groovy, or a comma separated like, regex, for directory path");

        final Subparser create = subparsers.addParser("create").help("create help");
        create.addArgument("--prop", "--properties").required(false).action(Arguments.storeTrue()).dest("prop");
        create.addArgument("--guid").required(false).action(Arguments.storeTrue()).dest("guid");

        writeOutTestah();

        try {
            setRunning(true);
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
                TS.log().debug(Cli.BAR_WALL + "Not using cli params, only loading from properties file [ " +
                        ParamLoader.getDefaultPropFilePath() + " ]");
                TS.log().debug(Cli.BAR_LONG);
            }

        } catch (final ArgumentParserException e) {
            parser.handleError(e);
            throw new RuntimeException(e);
        } finally {
            setRunning(false);
        }
        return this;
    }

    /**
     * Process run.
     */
    public void processRun() {

        //Reset Results List
        results = null;
        List<String> initializationErrorFailures = new ArrayList<>();
        this.setTestPlanFilter(new TestFilter());
        getTestPlanFilter().filterTestPlansToRun();

        final TestahJUnitRunner junitRunner = new TestahJUnitRunner();
        if (isUnderTest()) {
            return ;
        }

        Instant startTest = Instant.now();
        results = junitRunner.runTests(TS.params().getNumConcurrentThreads(),
                getTestPlanFilter().getTestClassesMetFilters());
        Instant endTest = Instant.now();
        long duration = Duration.between(startTest, endTest).toMillis();

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
                    TS.log().error("Testplan is null, looking at junit result for " + result.getJunitResult().getFailures());

                    if (null != result.getJunitResult()) {
                        if (null != result.getJunitResult().getFailures()) {
                            result.getJunitResult().getFailures().forEach(failure -> {
                                if (StringUtils.startsWithIgnoreCase(failure.toString(), "initializationError")) {
                                    initializationErrorFailures.add(result.getJunitResult().getFailures().toString());
                                }
                            });
                        } else {
                            totalTestCases += result.getJunitResult().getRunCount();
                            totalTestCasesFailed += result.getJunitResult().getFailureCount();
                            totalTestCasesPassed += result.getJunitResult().getRunCount() -
                                    (result.getJunitResult().getFailureCount() + result.getJunitResult().getIgnoreCount());
                            totalTestCasesIgnored += result.getJunitResult().getIgnoreCount();
                        }
                    }
                }
            }
            for (final ResultDto result : results) {
                if (null != result.getTestPlan()) {
                    TS.getTestPlanReporter().reportResults(result.getTestPlan(), false, this.opt.getOutput(), true);
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
        TS.log().info(Cli.BAR_WALL + "Total Duration: " + TS.util().getDurationPretty(duration));
        TS.log().info(Cli.BAR_LONG);

        File summaryHtml = new SummaryHtmlFormatter(results, totalTestPlans, totalTestCases, totalTestCasesPassed, totalTestCasesFailed, totalTestCasesIgnored, duration).createReport().getReportFile();
        if (TS.params().isAutoOpenHtmlReport()) {
            new TestPlanReporter().openReport(summaryHtml.getAbsolutePath());
        }
        AbstractTestPlan.tearDownTestah();

        if (totalTestCasesFailed > 0) {
            throw new RuntimeException("There are test failures " + totalTestCasesFailed);
        }

        if (!initializationErrorFailures.isEmpty()) {
            throw new RuntimeException("There are test failures due to test classes not being able to load: " +
                    initializationErrorFailures);
        }

    }

    /**
     * Process query.
     *
     * @throws IOException Signals that an I/O exception has occurred.
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

            if (res.getBoolean("requireRelatedIds")) {
                HashMap<TestPlanDto, List<TestCaseDto>> missingRelatedIds = new HashMap<>();
                testPlans.values().parallelStream().forEach(testPlan -> {
                    List<TestCaseDto> testCaseDtos = new ArrayList<>();
                    testPlan.getTestCases().parallelStream().forEach(testCaseDto -> {
                        if (testCaseDto.getRelatedIds() == null || testCaseDto.getRelatedIds().isEmpty()) {
                            testCaseDtos.add(testCaseDto);
                        }
                    });
                    if (!testCaseDtos.isEmpty()) {
                        missingRelatedIds.put(testPlan, testCaseDtos);
                    }
                });
                if (!missingRelatedIds.isEmpty()) {
                    throw new RuntimeException("Metadata audit failure: At least 1 testcase is missing required " +
                            "related field value! The value can be applied at the testplan level for all " +
                            "testcases to get - " + TS.util().toJson(missingRelatedIds));
                }
            }


            resultObject = testPlans;
        }

        FileUtils.writeStringToFile(results, TS.util().toJson(resultObject), Charset.forName("UTF-8"));
        TS.log().info("Query Results: Found[" + getTestPlanFilter().getTestClassesMetFilters().size() + "] " +
                results.getAbsolutePath());

        if (res.getBoolean("showInConsole")) {
            TS.log().info(TS.util().toJson(resultObject));
        }

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
     * Gets test plan filter.
     *
     * @return the test plan filter
     */
    public TestFilter getTestPlanFilter() {
        return testPlanFilter;
    }

    /**
     * Sets test plan filter.
     *
     * @param testPlanFilter the test plan filter
     * @return the test plan filter
     */
    public Cli setTestPlanFilter(final TestFilter testPlanFilter) {
        this.testPlanFilter = testPlanFilter;
        return this;
    }

    /**
     * Is under test boolean for use with unit tests testing cli class.
     *
     * @return the boolean
     */
    public boolean isUnderTest() {
        return underTest;
    }

    /**
     * Sets under test for use with unit tests testing cli class.
     *
     * @param underTest the under test
     * @return the under test
     */
    public Cli setUnderTest(final boolean underTest) {
        this.underTest = underTest;
        return this;
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
     * @param res the new res
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

    /**
     * Sets opt.
     *
     * @param opt the opt
     * @return the opt
     */
    public Cli setOpt(final Params opt) {
        this.opt = opt;
        return this;
    }

    public List<ResultDto> getResults()
    {
        return results;
    }
}
