package com.logger.models;

import com.logger.enums.LogLevel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogMessage {
    private final LocalDateTime timestamp;
    private final LogLevel level;
    private final String message;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    public LogMessage(LogLevel level, String message) {
        this.timestamp = LocalDateTime.now();
        this.level = level;
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public LogLevel getLevel() {
        return level;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return String.format("[%s] [%s] %s",
                timestamp.format(formatter),
                level.name(),
                message);
    }
}
