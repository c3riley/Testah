package org.testah.util;

import com.jcraft.jsch.Logger;

public class SshLogger implements Logger {

    private static final java.util.Hashtable<Integer, String> logLevels = new java.util.Hashtable<>();

    static {
        logLevels.put(DEBUG, "DEBUG: ");
        logLevels.put(INFO, "INFO: ");
        logLevels.put(WARN, "WARN: ");
        logLevels.put(ERROR, "ERROR: ");
        logLevels.put(FATAL, "FATAL: ");
    }

    public boolean isEnabled(final int level) {
        return logLevels.get(level) != null;
    }

    public void log(final int level, final String message) {
        System.err.print(logLevels.get(level));
        System.err.println(message);
    }
}
