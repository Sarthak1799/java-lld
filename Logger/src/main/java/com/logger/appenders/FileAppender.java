package com.logger.appenders;

import com.logger.models.LogMessage;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileAppender implements LogAppender {
    private final String filePath;
    private BufferedWriter writer;

    public FileAppender(String filePath) {
        this.filePath = filePath;
        try {
            this.writer = new BufferedWriter(new FileWriter(filePath, true));
        } catch (IOException e) {
            System.err.println("Failed to initialize FileAppender: " + e.getMessage());
        }
    }

    @Override
    public synchronized void append(LogMessage logMessage) {
        try {
            if (writer != null) {
                writer.write(logMessage.toString());
                writer.newLine();
                writer.flush();
            }
        } catch (IOException e) {
            System.err.println("Failed to write log to file: " + e.getMessage());
        }
    }

    @Override
    public void close() {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            System.err.println("Failed to close FileAppender: " + e.getMessage());
        }
    }
}
