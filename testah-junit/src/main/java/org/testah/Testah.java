package org.testah;

import org.testah.framework.cli.Cli;

public class Testah {

	public static void main(final String[] args) throws InterruptedException {
		final Cli cli = new Cli();
		cli.getArgumentParser(args);

	}

}
