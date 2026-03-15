package com.logger.appenders;

import com.logger.models.LogMessage;

public class ConsoleAppender implements LogAppender {
    
    @Override
    public void append(LogMessage logMessage) {
        System.out.println(logMessage.toString());
    }

    @Override
    public void close() {
        // No resources to close for console output
        System.out.flush();
    }
}
