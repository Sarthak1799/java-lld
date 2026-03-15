package com.logger;

import com.logger.config.LoggerConfig;
import com.logger.enums.LogLevel;
import com.logger.models.LogMessage;

public class Logger {
    // Thread-safe singleton using double-checked locking
    private static volatile Logger instance;
    private LoggerConfig config;
    private final Object lock = new Object();

    private Logger() {
        // Private constructor to prevent instantiation
    }

    /**
     * Gets the singleton instance of Logger
     * @return Logger instance
     */
    public static Logger getInstance() {
        if (instance == null) {
            synchronized (Logger.class) {
                if (instance == null) {
                    instance = new Logger();
                }
            }
        }
        return instance;
    }

    /**
     * Sets the logger configuration
     * @param config LoggerConfig instance
     */
    public void setConfig(LoggerConfig config) {
        synchronized (lock) {
            this.config = config;
        }
    }

    /**
     * Logs a message with the specified log level
     * @param level Log level
     * @param message Log message
     */
    public void log(LogLevel level, String message) {
        LoggerConfig currentConfig;
        synchronized (lock) {
            currentConfig = this.config;
        }

        if (currentConfig == null) {
            System.err.println("Logger not configured. Please set configuration before logging.");
            return;
        }

        if (level.isLoggable(currentConfig.getLogLevel())) {
            LogMessage logMessage = new LogMessage(level, message);
            currentConfig.getLogAppender().append(logMessage);
        }
    }

    /**
     * Logs a DEBUG level message
     * @param message Log message
     */
    public void debug(String message) {
        log(LogLevel.DEBUG, message);
    }

    /**
     * Logs an INFO level message
     * @param message Log message
     */
    public void info(String message) {
        log(LogLevel.INFO, message);
    }

    /**
     * Logs a WARNING level message
     * @param message Log message
     */
    public void warning(String message) {
        log(LogLevel.WARNING, message);
    }

    /**
     * Logs an ERROR level message
     * @param message Log message
     */
    public void error(String message) {
        log(LogLevel.ERROR, message);
    }

    /**
     * Logs a FATAL level message
     * @param message Log message
     */
    public void fatal(String message) {
        log(LogLevel.FATAL, message);
    }

    /**
     * Closes the logger and releases resources
     */
    public void shutdown() {
        synchronized (lock) {
            if (config != null && config.getLogAppender() != null) {
                config.getLogAppender().close();
            }
        }
    }
}
