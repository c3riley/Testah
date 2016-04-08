package org.testah.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;

import org.apache.maven.wagon.util.IoUtils;
import org.testah.TS;

/**
 * The Class BashUtil.
 */
public class BashUtil {

	/** The verbose. */
	private boolean verbose = true;

	/** The output. */
	private String output = "";

	/** The error. */
	private String error = "";

	/**
	 * Instantiates a new bash util.
	 */
	public BashUtil() {

	}

	/**
	 * Execute commands.
	 *
	 * @param commands
	 *            the commands
	 * @return the string
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws InterruptedException
	 *             the interrupted exception
	 */
	public String executeCommands(final String... commands) throws IOException, InterruptedException {
		final String output = "";
		final File tempScript = createBashFile(commands);
		try {
			final ProcessBuilder pb = new ProcessBuilder("bash", tempScript.toString());
			pb.inheritIO();
			final Process process = pb.start();
			process.waitFor();
			setOutput(IoUtils.toString(process.getInputStream()));
			setError(IoUtils.toString(process.getErrorStream()));
			if (verbose) {
				TS.log().debug(getOutput());
				TS.log().debug(getError());
			}
		} finally {
			tempScript.delete();
		}
		return output;
	}

	/**
	 * Creates the bash file.
	 *
	 * @param commands
	 *            the commands
	 * @return the file
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public File createBashFile(final String... commands) throws IOException {
		final File tmpBashScript = File.createTempFile("tmpBashScript", null);

		final Writer streamWriter = new OutputStreamWriter(new FileOutputStream(tmpBashScript));
		final PrintWriter printWriter = new PrintWriter(streamWriter);
		printWriter.println("#!/bin/bash");
		for (final String command : commands) {
			printWriter.println(command);
		}
		printWriter.close();

		return tmpBashScript;
	}

	/**
	 * Checks if is verbose.
	 *
	 * @return true, if is verbose
	 */
	public boolean isVerbose() {
		return verbose;
	}

	/**
	 * Sets the verbose.
	 *
	 * @param verbose
	 *            the new verbose
	 */
	public void setVerbose(final boolean verbose) {
		this.verbose = verbose;
	}

	/**
	 * Gets the output.
	 *
	 * @return the output
	 */
	public String getOutput() {
		return output;
	}

	/**
	 * Sets the output.
	 *
	 * @param output
	 *            the new output
	 */
	public void setOutput(final String output) {
		this.output = output;
	}

	/**
	 * Gets the error.
	 *
	 * @return the error
	 */
	public String getError() {
		return error;
	}

	/**
	 * Sets the error.
	 *
	 * @param error
	 *            the new error
	 */
	public void setError(final String error) {
		this.error = error;
	}
}
