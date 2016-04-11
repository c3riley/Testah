package org.testah.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.testah.TS;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelShell;
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

	/** The pty type value. */
	private String ptyTypeValue = "dumb";

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
		return getSession(username, host, port, null);
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
	 * @param password
	 *            the password
	 * @return the session
	 * @throws JSchException
	 *             the j sch exception
	 */
	public Session getSession(final String username, final String host, final int port, final String password)
			throws JSchException {
		final Session session = jsch.getSession(username, host, port);
		session.setServerAliveInterval(120 * 1000);
		session.setServerAliveCountMax(1000);
		session.setConfig("TCPKeepAlive", "yes");
		session.setConfig("GSSAPIAuthentication", "no");
		session.setConfig("StrictHostKeyChecking", "no");
		if (null != password) {
			session.setPassword(password);
		}
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

		if (!session.isConnected()) {
			session.connect();
		}

		final Channel channel = session.openChannel("shell");
		String output = "";
		try {
			final OutputStream inputstream_for_the_channel = channel.getOutputStream();
			final PrintStream commander = new PrintStream(inputstream_for_the_channel);

			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			channel.setInputStream(System.in);
			channel.setOutputStream(System.out, true);
			channel.setOutputStream(baos);
			((ChannelShell) channel).setPtyType(this.getPtyTypeValue());
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
	 * Run shell enhanced.
	 *
	 * @param session
	 *            the session
	 * @param commands
	 *            the commands
	 * @return the hash map
	 * @throws JSchException
	 *             the j sch exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws InterruptedException
	 *             the interrupted exception
	 */
	public HashMap<Integer, List<String>> runShellEnhanced(final Session session, final String... commands)
			throws JSchException, IOException, InterruptedException {
		final HashMap<Integer, List<String>> outputHash = new HashMap<Integer, List<String>>();
		final List<String> commandList = new ArrayList<String>();
		int ctr = 0;
		for (final String command : commands) {
			commandList.add("echo @START" + ctr + "@T= $( date +%T )");
			commandList.add(command);
			commandList.add("echo @END" + ctr + "@T= $( date +%T )");
			ctr++;
		}

		final String outputRaw = runShell(session, commandList.toArray(commands));

		if (null != outputRaw) {
			final List<String> lines = cleanOutput(outputRaw);
			ctr = 0;
			for (final String line : lines) {
				if (line.contains("@START" + ctr + "@")) {
					outputHash.put(ctr, new ArrayList<String>());
					outputHash.get(ctr).add(commands[ctr]);
				} else if (line.contains("@END" + ctr + "@")) {
					ctr++;
				} else {
					outputHash.get(ctr).add(line);
				}
			}
		}
		return outputHash;
	}

	/**
	 * Gets the output lines for command.
	 *
	 * @param command
	 *            the command
	 * @param outputHash
	 *            the output hash
	 * @return the output lines for command
	 */
	public List<String> getOutputLinesForCommand(final String command,
			final HashMap<Integer, List<String>> outputHash) {
		List<String> lst = new ArrayList<String>();
		if (null != command && null != outputHash && !outputHash.isEmpty()) {
			for (final Integer key : outputHash.keySet()) {
				if (outputHash.get(key).get(0).equals(command)) {
					lst = outputHash.get(key);
					lst.remove(0);
					return lst;
				}
			}
		}
		return lst;
	}

	/**
	 * Clean output.
	 *
	 * @param output
	 *            the output
	 * @return the list
	 */
	public List<String> cleanOutput(String output) {
		if (null != output) {
			output = output.replace("\r", "").replace("\t", "");
			return Arrays.asList(output.split("\n"));
		}
		return new ArrayList<String>();
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

	/**
	 * Gets the pty type value.
	 *
	 * @return the pty type value
	 */
	public String getPtyTypeValue() {
		return ptyTypeValue;
	}

	/**
	 * Sets the pty type value.
	 *
	 * @param ptyTypeValue
	 *            the new pty type value
	 */
	public void setPtyTypeValue(final String ptyTypeValue) {
		this.ptyTypeValue = ptyTypeValue;
	}

}
