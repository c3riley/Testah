package org.testah.util;

import org.apache.maven.wagon.util.IoUtils;
import org.testah.TS;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;

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

	/** The Constant DEFAULT_EXIT_VALUE. */
	public static final int DEFAULT_EXIT_VALUE = -999;

	/** The exit value. */
	private int exitValue = DEFAULT_EXIT_VALUE;

	/** The bang line. */
	private String bangLine = "#!/bin/bash";

	/** The bash source. */
	private String bashSource = "source ~/.bashrc\nsource ~/profile";

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
		setExitValue(DEFAULT_EXIT_VALUE);
		setOutput("");
		final File tempScript = createBashFile(commands);
		try {
			final ProcessBuilder pb = new ProcessBuilder("bash", tempScript.toString());
			// pb.inheritIO();
			final Process process = pb.start();
			process.waitFor();
			setOutput(IoUtils.toString(process.getInputStream()));
			setError(IoUtils.toString(process.getErrorStream()));
			if (verbose) {
				TS.log().debug(getOutput());
				TS.log().debug(getError());
			}
			setExitValue(process.exitValue());
			process.destroy();
			process.destroyForcibly();
		} finally {
			TS.log().trace("tempScript.delete " + tempScript.delete());
		}
		return getOutput();
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
		printWriter.println(getBangLine());
		printWriter.println(getBashSource());
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
	 * @return the bash util
	 */
	public BashUtil setVerbose(final boolean verbose) {
		this.verbose = verbose;
		return this;
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
	 * @return the bash util
	 */
	public BashUtil setOutput(final String output) {
		this.output = output;
		return this;
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
	 * @return the bash util
	 */
	public BashUtil setError(final String error) {
		this.error = error;
		return this;
	}

	/**
	 * Gets the bang line.
	 *
	 * @return the bang line
	 */
	public String getBangLine() {
		if (null == bangLine) {
			bangLine = "";
		}
		return bangLine;
	}

	/**
	 * Sets the bang line.
	 *
	 * @param bangLine
	 *            the new bang line
	 * @return the bash util
	 */
	public BashUtil setBangLine(final String bangLine) {
		this.bangLine = bangLine;
		return this;
	}

	/**
	 * Gets the bash source.
	 *
	 * @return the bash source
	 */
	public String getBashSource() {
		if (null == bashSource) {
			bashSource = "";
		}
		return bashSource;
	}

	/**
	 * Sets the bash source.
	 *
	 * @param bashSource
	 *            the new bash source
	 * @return the bash util
	 */
	public BashUtil setBashSource(final String bashSource) {
		this.bashSource = bashSource;
		return this;
	}

	/**
	 * Gets the exit value.
	 *
	 * @return the exit value
	 */
	public int getExitValue() {
		return exitValue;
	}

	/**
	 * Sets the exit value.
	 *
	 * @param exitValue
	 *            the exit value
	 * @return the bash util
	 */
	public BashUtil setExitValue(final int exitValue) {
		this.exitValue = exitValue;
		return this;
	}
}
