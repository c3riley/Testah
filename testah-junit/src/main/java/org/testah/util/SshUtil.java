package org.testah.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import org.testah.TS;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * The Class SshUtil.
 */
// http://www.programcreek.com/java-api-examples/com.jcraft.jsch.JSch
public class SshUtil {

	/** The session timeout. */
	private int sessionTimeout = 100000;

	/** The verbose. */
	private boolean verbose = true;

	/** The max wait time in seconds. */
	private int maxWaitTimeInSeconds = 10;

	/** The jsch. */
	private final JSch jsch = new JSch();

	/**
	 * Instantiates a new ssh util.
	 *
	 * @throws JSchException
	 *             the j sch exception
	 */
	public SshUtil() throws JSchException {

	}

	/**
	 * Gets the jsch.
	 *
	 * @return the jsch
	 */
	public JSch getJsch() {
		return jsch;
	}

	/**
	 * Gets the session.
	 *
	 * @return the session
	 * @throws JSchException
	 *             the j sch exception
	 */
	public Session getSession() throws JSchException {
		return getSession("root", "52.27.61.154", 22);
	}

	/**
	 * Gets the session.
	 *
	 * @param username
	 *            the username
	 * @param host
	 *            the host
	 * @param port
	 *            the port
	 * @return the session
	 * @throws JSchException
	 *             the j sch exception
	 */
	public Session getSession(final String username, final String host, final int port) throws JSchException {
		final Session session = jsch.getSession(username, host, port);
		session.setServerAliveInterval(120 * 1000);
		session.setServerAliveCountMax(1000);
		session.setConfig("TCPKeepAlive", "yes");
		session.setConfig("GSSAPIAuthentication", "no");
		session.setConfig("StrictHostKeyChecking", "no");
		session.connect();
		return session;
	}

	/**
	 * Run shell.
	 *
	 * @param session
	 *            the session
	 * @param commands
	 *            the commands
	 * @return the string
	 * @throws JSchException
	 *             the j sch exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws InterruptedException
	 *             the interrupted exception
	 */
	public String runShell(final Session session, final String... commands)
			throws JSchException, IOException, InterruptedException {
		final Channel channel = session.openChannel("shell");
		String output = "";
		try {
			final OutputStream inputstream_for_the_channel = channel.getOutputStream();
			final PrintStream commander = new PrintStream(inputstream_for_the_channel);

			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			channel.setInputStream(System.in);
			channel.setOutputStream(System.out, true);
			channel.setOutputStream(baos);
			channel.connect();

			for (final String command : commands) {
				commander.println(command);
			}
			commander.println("exit");
			commander.close();

			for (int ctr = 0; ctr < maxWaitTimeInSeconds; ctr++) {
				if (channel.isEOF()) {
					break;
				}
				Thread.sleep(1000);
			}
			output = new String(baos.toByteArray());
			if (verbose) {
				TS.log().debug(output);
			}
		} finally {
			channel.disconnect();
			session.disconnect();
		}
		return output;
	}

	/**
	 * Gets the session timeout.
	 *
	 * @return the session timeout
	 */
	public int getSessionTimeout() {
		return sessionTimeout;
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
	 * Gets the max wait time in seconds.
	 *
	 * @return the max wait time in seconds
	 */
	public int getMaxWaitTimeInSeconds() {
		return maxWaitTimeInSeconds;
	}

	/**
	 * Sets the session timeout.
	 *
	 * @param sessionTimeout
	 *            the new session timeout
	 */
	public void setSessionTimeout(final int sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
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
	 * Sets the max wait time in seconds.
	 *
	 * @param maxWaitTimeInSeconds
	 *            the new max wait time in seconds
	 */
	public void setMaxWaitTimeInSeconds(final int maxWaitTimeInSeconds) {
		this.maxWaitTimeInSeconds = maxWaitTimeInSeconds;
	}

}
