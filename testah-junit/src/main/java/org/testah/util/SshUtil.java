package org.testah.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.testah.TS;
import org.testah.framework.dto.StepAction;
import org.testah.util.dto.ShellInfoDto;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

// TODO: Auto-generated Javadoc
/**
 * The Class SshUtil.
 */
// http://www.programcreek.com/java-api-examples/com.jcraft.jsch.JSch
public class SshUtil {

    /** The Constant mergeStreams. */
    private static final String mergeStreams = " 2>&1";

    /** The ignore timeout. */
    private boolean ignoreTimeout = false;

    /** The session timeout. */
    private int sessionTimeout = 100000;

    /** The verbose. */
    private boolean verbose = true;

    /** The max wait time in seconds. */
    private int maxWaitTimeInSeconds = 10;

    /** The pty type value. */
    private String ptyTypeValue = "dumb";

    /** The pem file. */
    private String pemFile = null;

    /** The jsch. */
    private final JSch jsch = new JSch();

    /** The Constant LAST_EXIT_CODE_DEFAULT. */
    public static final int LAST_EXIT_CODE_DEFAULT = -999;

    /** The last exit code. */
    private int lastExitCode = LAST_EXIT_CODE_DEFAULT;

    private boolean autoAddMergeFields = false;

    /**
     * Instantiates a new ssh util.
     *
     * @throws JSchException
     *             the j sch exception
     */
    public SshUtil() throws JSchException {
        setVerbose(true);
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
        if (null != getPemFile()) {
            getJsch().addIdentity(getPemFile());
        }
        StepAction.createInfo("Getting Session", host + ":" + port + " for user: " + username).add();
        return session;
    }

    public String runShell(final Session session, final String... commands)
            throws JSchException, IOException, InterruptedException {
        return runShellRtnInfo(session, commands).getOutput().toString();

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
    public ShellInfoDto runShellRtnInfo(final Session session, final String... commands)
            throws JSchException, IOException, InterruptedException {
        ShellInfoDto info = new ShellInfoDto();
        this.lastExitCode = LAST_EXIT_CODE_DEFAULT;
        if (!session.isConnected()) {
            session.connect();
        }

        final Channel channel = session.openChannel("shell");

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
                if (verbose) {
                    StepAction.createInfo("Running Shell Commnad", command);
                }
                if (isAutoAddMergeFields()) {
                    commander.println(command + mergeStreams);
                } else {
                    commander.println(command);
                }
            }
            commander.println("exit");
            commander.close();

            if (!isIgnoreTimeout()) {
                for (int ctr = 0; ctr < maxWaitTimeInSeconds; ctr++) {
                    if (channel.isEOF()) {
                        break;
                    }
                    Thread.sleep(1000);
                }
                info.getOutput().append(new String(baos.toByteArray()));
            } else {
                final byte[] tmp = new byte[1024];
                while (true) {
                    while (channel.getInputStream().available() > 0) {
                        final int i = channel.getInputStream().read(tmp, 0, 1024);
                        if (i < 0) {
                            break;
                        }
                        info.getOutput().append(new String(tmp, 0, i));
                        if (verbose) {
                            TS.log().debug(new String(tmp, 0, i));
                        }
                    }
                    if (verbose) {
                        TS.log().debug("Should be done reading from input stream");
                    }
                    if (channel.isClosed()) {
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (final Exception ee) {
                        TS.log().trace(ee);
                    }
                }
            }

            if (verbose) {
                StepAction.createInfo("Shell Output", TS.util().toJson(info)).add();
            }
        } finally {
            System.out.println("exit-status: " + channel.getExitStatus());
            this.lastExitCode = channel.getExitStatus();
            info.setExitCode(channel.getExitStatus());
            if (channel.isConnected()) {
                channel.disconnect();
            }
            session.disconnect();
        }
        return info;
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
        final HashMap<Integer, List<String>> outputHash = new HashMap<>();
        final List<String> commandList = new ArrayList<>();
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
        List<String> lst = new ArrayList<>();
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
        return new ArrayList<>();
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
     * @return the ssh util
     */
    public SshUtil setSessionTimeout(final int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
        return this;
    }

    /**
     * Sets the verbose.
     *
     * @param verbose
     *            the new verbose
     * @return the ssh util
     */
    public SshUtil setVerbose(final boolean verbose) {
        JSch.setLogger(new SshLogger());
        this.verbose = verbose;
        return this;
    }

    /**
     * Sets the max wait time in seconds.
     *
     * @param maxWaitTimeInSeconds
     *            the new max wait time in seconds
     * @return the ssh util
     */
    public SshUtil setMaxWaitTimeInSeconds(final int maxWaitTimeInSeconds) {
        this.maxWaitTimeInSeconds = maxWaitTimeInSeconds;
        return this;
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
     * @return the ssh util
     */
    public SshUtil setPtyTypeValue(final String ptyTypeValue) {
        this.ptyTypeValue = ptyTypeValue;
        return this;
    }

    /**
     * Checks if is ignore timeout.
     *
     * @return true, if is ignore timeout
     */
    public boolean isIgnoreTimeout() {
        return ignoreTimeout;
    }

    /**
     * Sets the ignore timeout.
     *
     * @param ignoreTimeout
     *            the new ignore timeout
     * @return the ssh util
     */
    public SshUtil setIgnoreTimeout(final boolean ignoreTimeout) {
        this.ignoreTimeout = ignoreTimeout;
        return this;
    }

    /**
     * Gets the pem file.
     *
     * @return the pem file
     */
    public String getPemFile() {
        return pemFile;
    }

    /**
     * Sets the pem file.
     *
     * @param pemFile
     *            the pem file
     * @return the ssh util
     */
    public SshUtil setPemFile(final String pemFile) {
        this.pemFile = pemFile;
        return this;
    }

    /**
     * Run exec.
     *
     * @param session
     *            the session
     * @param command
     *            the command
     * @return the string
     */
    public String runExec(final Session session, final String command) {
        try {
            if (!session.isConnected()) {
                session.connect();
            }
            this.lastExitCode = LAST_EXIT_CODE_DEFAULT;
            final Channel channel = session.openChannel("exec");
            if (isVerbose()) {
                StepAction.createInfo("runExec", command).add();
            }
            if (isAutoAddMergeFields()) {
                ((ChannelExec) channel).setCommand(command + mergeStreams);
            } else {
                ((ChannelExec) channel).setCommand(command);
            }

            // channel.setInputStream(System.in);
            channel.setInputStream(null);

            // channel.setOutputStream(System.out);

            // FileOutputStream fos=new FileOutputStream("/tmp/stderr");
            // ((ChannelExec)channel).setErrStream(fos);
            ((ChannelExec) channel).setErrStream(System.err);

            final InputStream in = channel.getInputStream();

            channel.connect();
            final StringBuffer sb = new StringBuffer();
            final byte[] tmp = new byte[1024];
            String msg;
            while (true) {
                while (in.available() > 0) {
                    final int i = in.read(tmp, 0, 1024);
                    if (i < 0) {
                        break;
                    }
                    msg = new String(tmp, 0, i, "UTF-8");
                    sb.append(msg);
                    if (isVerbose()) {
                        TS.log().info(msg);
                    }
                }
                if (channel.isClosed()) {
                    if (in.available() > 0) {
                        continue;
                    }
                    TS.log().info("exit-status: " + channel.getExitStatus());
                    this.lastExitCode = channel.getExitStatus();
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (final Exception ee) {
                }
            }
            channel.disconnect();
            session.disconnect();
            return sb.toString();
        } catch (final Exception e) {
            TS.log().error(e);
        }
        return "";
    }

    /**
     * Gets the last exit code.
     *
     * @return the last exit code
     */
    public int getLastExitCode() {
        if (lastExitCode == LAST_EXIT_CODE_DEFAULT) {
            TS.log().warn("Exit Code was not set with last action!, so is " + LAST_EXIT_CODE_DEFAULT);
        }
        return lastExitCode;
    }

    /**
     * Sets the last exit code.
     *
     * @param lastExitCode
     *            the new last exit code
     */
    public void setLastExitCode(final int lastExitCode) {
        this.lastExitCode = lastExitCode;
    }

    public static String getMergestreams() {
        return mergeStreams;
    }

    public boolean isAutoAddMergeFields() {
        return autoAddMergeFields;
    }

    public void setAutoAddMergeFields(final boolean autoAddMergeFields) {
        this.autoAddMergeFields = autoAddMergeFields;
    }

}
