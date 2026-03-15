package com.logger.appenders;

import com.logger.models.LogMessage;

public interface LogAppender {
    /**
     * Appends a log message to the output destination
     * @param logMessage The log message to append
     */
    void append(LogMessage logMessage);

    /**
     * Closes any resources used by the appender
     */
    void close();
}
