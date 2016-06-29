package org.testah;

import java.io.IOException;

import org.testah.framework.cli.Cli;

/**
 * The Class Testah.
 */
public class Testah {

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 * @throws InterruptedException
	 *             the interrupted exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void main(final String[] args) throws InterruptedException, IOException {
		final Cli cli = new Cli();
		cli.getArgumentParser(args);
	}

}
