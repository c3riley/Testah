package org.testah.framework.cli;

import static net.sourceforge.argparse4j.impl.Arguments.enumStringType;

import java.util.List;

import org.testah.TS;
import org.testah.client.enums.BrowserType;
import org.testah.framework.dto.ResultDto;
import org.testah.framework.report.TestPlanReporter;
import org.testah.runner.TestahJUnitRunner;
import org.testah.util.Log;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.Subparsers;

/**
 * The Class Cli.
 */
public class Cli {

	/** The res. */
	private Namespace res;

	/** The param loader. */
	private final ParamLoader paramLoader;

	/** The opt. */
	private final Params opt;

	/** The Constant version. */
	public static final String version = "0.1.7";

	public static final String BAR_LONG = "=============================================================================================";
	public static final String BAR_SHORT = "=========================================";
	public static final String BAR_WALL = "# ";

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

		subparsers.addParser("query").help("query help");

		final Subparser create = subparsers.addParser("create").help("create help");
		create.addArgument("--prop", "--properties").required(false).action(Arguments.storeTrue()).dest("prop");
		create.addArgument("--guid").required(false).action(Arguments.storeTrue()).dest("guid");

		writeOutTestah();

		try {
			if (null != args) {
				res = parser.parseArgs(args);
				parser.parseArgs(args, opt);
				TS.setParams(opt);
				TS.log().debug(Cli.BAR_LONG);
				TS.log().info(Cli.BAR_WALL + "CLI Inputs - " + res);
				TS.log().debug(Cli.BAR_LONG);
				final String subProcess = res.getString("subparserName");
				if (null != res.getString("subparserName")) {
					if (subProcess.equalsIgnoreCase("run")) {

						processRun();

					} else if (subProcess.equalsIgnoreCase("query")) {

						processQuery();

					} else if (subProcess.equalsIgnoreCase("create")) {

						processCreate();

					} else {
						throw new RuntimeException("Unknown SubParser: " + subProcess);
					}
				}

			} else {
				TS.setParams(opt);
				TS.log().debug(Cli.BAR_LONG);
				TS.log().debug(Cli.BAR_WALL + "Not using cli params, only loading from properties file [ "
						+ ParamLoader.getDefaultPropFilePath() + " ]");
				TS.log().debug(Cli.BAR_LONG);
			}
			Log.setLevel(TS.params().getLevel());
		} catch (final ArgumentParserException e) {
			parser.handleError(e);
			System.exit(1);
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

		final TestFilter testPlanFilter = new TestFilter();
		testPlanFilter.filterTestPlansToRun();

		final TestahJUnitRunner junitRunner = new TestahJUnitRunner();
		final List<ResultDto> results = junitRunner.runTests(TS.params().getNumConcurrentThreads(),
				testPlanFilter.getTestClassesMetFilters());

		int totalTestCases = 0;
		int totalTestCasesFailed = 0;
		int totalTestCasesPassed = 0;
		int totalTestCasesIgnored = 0;
		final int totalTestPlans = results.size();

		if (null != results) {
			TS.log().info(Cli.BAR_LONG);
			TS.log().info(Cli.BAR_WALL + "TestPlan Result(s):");

			TS.util().pause(1000L, "Waiting for TestsPlans to complete");
			for (final ResultDto result : results) {
				if (null != result.getTestPlan()) {
					totalTestCases += result.getTestPlan().getRunInfo().getTotal();
					totalTestCasesFailed += result.getTestPlan().getRunInfo().getFail();
					totalTestCasesPassed += result.getTestPlan().getRunInfo().getPass();
					totalTestCasesIgnored += result.getTestPlan().getRunInfo().getIgnore();
				} else {
					TS.log().error("Testplan is null, for " + result.getJunitResult().getFailures());
				}
			}
		}

		for (final ResultDto result : results) {
			if (null != result.getTestPlan()) {
				TestPlanReporter.reportResults(result.getTestPlan());
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

	}

	/**
	 * Process query.
	 */
	public void processQuery() {

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
	 */
	public void setRes(final Namespace res) {
		this.res = res;
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
}
