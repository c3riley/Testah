package org.testah.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.slf4j.MDC;

/**
 * The Class Log.
 */
public class Log {

    /**
     * The logger.
     */
    private static Log LOGGER;

    /**
     * The logger.
     */
    private final Logger logger;

    /**
     * Instantiates a new log.
     *
     * @param logName the log name
     */
    public Log(final String logName) {
        this(logName, Level.DEBUG);
    }

    /**
     * Instantiates a new log.
     *
     * @param logName the log name
     * @param level   the level
     */
    public Log(final String logName, final Level level) {
        this.logger = LogManager.getLogger(logName);
        setLevel(level);
    }

    /**
     * Sets the level.
     *
     * @param level the new level
     */
    public static void setLevel(final Level level) {
        final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        final Configuration config = ctx.getConfiguration();
        final LoggerConfig loggerConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
        loggerConfig.setLevel(level);
        ctx.updateLoggers();
    }

    /**
     * Sets the level.
     */
    public void setLevel() {
        setLevel(Level.DEBUG);
    }

    /**
     * Gets the log.
     *
     * @return the log
     */
    public static Logger getLog() {
        if (null == LOGGER) {
            LOGGER = new Log("Testah");
            MDC.put("threadId", "" + Thread.currentThread().getId());
        }
        return LOGGER.getLogger();
    }

    /**
     * Gets the logger.
     *
     * @return the logger
     */
    public Logger getLogger() {
        return logger;
    }

    public static void resetLogger() {
        LOGGER = null;
        LogManager.shutdown();
    }

}
