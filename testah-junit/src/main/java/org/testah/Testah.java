package org.testah;

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
     */
    public static void main(final String[] args) throws InterruptedException {
        final Cli cli = new Cli();
        cli.getArgumentParser(args);

    }

}
