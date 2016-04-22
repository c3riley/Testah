package org.testah.util;

import com.jcraft.jsch.Logger;

@SuppressWarnings("unchecked")
public class SshLogger implements Logger {
	@SuppressWarnings("rawtypes")
	static java.util.Hashtable name = new java.util.Hashtable();

	static {
		name.put(Integer.valueOf(DEBUG), "DEBUG: ");
		name.put(Integer.valueOf(INFO), "INFO: ");
		name.put(Integer.valueOf(WARN), "WARN: ");
		name.put(Integer.valueOf(ERROR), "ERROR: ");
		name.put(Integer.valueOf(FATAL), "FATAL: ");
	}

	public boolean isEnabled(final int level) {
		return true;
	}

	public void log(final int level, final String message) {
		System.err.print(name.get(new Integer(level)));
		System.err.println(message);
	}
}
