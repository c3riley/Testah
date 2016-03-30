package org.testah.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

public class Log {

    private static Log   LOGGER;

    private final Logger logger;

    public static Logger getLog() {
        if (null == LOGGER) {
            LOGGER = new Log("Testah");
        }
        return LOGGER.getLogger();
    }

    public Log(final String logName) {
        this(logName, Level.DEBUG);
    }

    public Log(final String logName, final Level level) {
        this.logger = LogManager.getLogger(logName);
        setLevel(level);
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLevel() {
        setLevel(Level.DEBUG);
    }

    public static void setLevel(final Level level) {
        final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        final Configuration config = ctx.getConfiguration();
        final LoggerConfig loggerConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
        loggerConfig.setLevel(level);
        ctx.updateLoggers();

    }

}
