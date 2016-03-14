package org.testah.framework.cli;

import static net.sourceforge.argparse4j.impl.Arguments.enumStringType;

import org.testah.TS;
import org.testah.framework.enums.BrowserType;
import org.testah.runner.TestahJUnitRunner;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.Subparsers;

public class Cli {

	private Namespace res;
	private final ParamLoader paramLoader;
	private final Params opt;
	private final String version = "0.0.1";

	public Cli() {

		String propFilePath = System.getenv("TESTAH_PROP");
		if (null == propFilePath) {
			propFilePath = System.getProperty("TESTAH_PROP", ParamLoader.getDefaultPropFilePath());
		}

		this.paramLoader = new ParamLoader(propFilePath);
		this.opt = new ParamLoader(propFilePath).loadParamValues();

	}

	public Cli getArgumentParser(final String[] args) {

		final ArgumentParser parser = ArgumentParsers.newArgumentParser("Testah").defaultHelp(true)
				.description("Testah CLI running Automated Tests for Browser and Http").epilog("").version(version);

		parser.addArgument("-v", "--version").action(Arguments.version()).setDefault(version);

		final Subparsers subparsers = parser.addSubparsers().help("sub-command help").dest("subparserName");

		final Subparser run = subparsers.addParser("run").help("run help");
		run.addArgument("-b", "--browser").setDefault(opt.getBrowser()).type(enumStringType(BrowserType.class))
				.help("foo help");

		subparsers.addParser("query").help("query help");

		final Subparser create = subparsers.addParser("create").help("query help");
		create.addArgument("--prop", "--properties").required(false).action(Arguments.storeTrue()).dest("prop");
		create.addArgument("--guid").required(false).action(Arguments.storeTrue()).dest("guid");

		try {
			if (null != args) {
				res = parser.parseArgs(args);
				parser.parseArgs(args, opt);
				TS.setParams(opt);

				TS.log().info("CLI Inputs - " + res);

				final String subProcess = res.getString("subparserName");
				if (null != res.getString("subparserName")) {
					if (subProcess.equalsIgnoreCase("run")) {

						processRun();

					} else if (subProcess.equalsIgnoreCase("query")) {

						processQuery();

					} else if (subProcess.equalsIgnoreCase("create")) {

						processCreate();

					} else {

					}
				}

			} else {
				TS.log().debug("Not using cli params, only loading from properties file");
			}
		} catch (final ArgumentParserException e) {
			parser.handleError(e);
			System.exit(1);
		}
		return this;

	}

	public void processCreate() {
		if (res.getBoolean("prop")) {
			paramLoader.overwriteDefaultConfig();
		}
	}

	public void processRun() {
		final TestPlanFilter testPlanFilter = new TestPlanFilter();
		testPlanFilter.filterTestPlansToRun();

		final TestahJUnitRunner junitRunner = new TestahJUnitRunner();
		junitRunner.runTests(TS.params().getNumConcurrentThreads(), testPlanFilter.getTestClassesMetFilters());
	}

	public void processQuery() {

	}

	public Namespace getRes() {
		return res;
	}

	public void setRes(final Namespace res) {
		this.res = res;
	}

	public ParamLoader getParamLoader() {
		return paramLoader;
	}

	public Params getOpt() {
		return opt;
	}

}
