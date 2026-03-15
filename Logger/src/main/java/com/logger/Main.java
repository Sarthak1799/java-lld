package com.logger;

import com.logger.appenders.ConsoleAppender;
import com.logger.appenders.FileAppender;
import com.logger.appenders.LogAppender;
import com.logger.config.LoggerConfig;
import com.logger.enums.LogLevel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        Logger logger = Logger.getInstance();

        // Demo 1: Console Appender with INFO level
        System.out.println("=== Demo 1: Console Logging with INFO level ===");
        LogAppender consoleAppender = new ConsoleAppender();
        LoggerConfig consoleConfig = new LoggerConfig(LogLevel.INFO, consoleAppender);
        logger.setConfig(consoleConfig);

        logger.debug("This is a DEBUG message - should NOT appear");
        logger.info("This is an INFO message");
        logger.warning("This is a WARNING message");
        logger.error("This is an ERROR message");
        logger.fatal("This is a FATAL message");

        System.out.println("\n=== Demo 2: File Logging with DEBUG level ===");
        // Demo 2: File Appender with DEBUG level
        LogAppender fileAppender = new FileAppender("application.log");
        LoggerConfig fileConfig = new LoggerConfig(LogLevel.DEBUG, fileAppender);
        logger.setConfig(fileConfig);

        logger.debug("Debug message logged to file");
        logger.info("Info message logged to file");
        logger.warning("Warning message logged to file");
        logger.error("Error message logged to file");
        logger.fatal("Fatal message logged to file");

        System.out.println("Logs written to application.log file");

        // Demo 3: Multi-threaded logging
        System.out.println("\n=== Demo 3: Multi-threaded Logging ===");
        LogAppender threadSafeAppender = new ConsoleAppender();
        LoggerConfig threadSafeConfig = new LoggerConfig(LogLevel.INFO, threadSafeAppender);
        logger.setConfig(threadSafeConfig);

        ExecutorService executorService = Executors.newFixedThreadPool(5);

        for (int i = 1; i <= 10; i++) {
            final int threadNum = i;
            executorService.submit(() -> {
                logger.info("Log from Thread-" + threadNum);
                logger.warning("Warning from Thread-" + threadNum);
            });
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Demo 4: Changing log level dynamically
        System.out.println("\n=== Demo 4: Dynamic Log Level Change ===");
        LoggerConfig errorConfig = new LoggerConfig(LogLevel.ERROR, new ConsoleAppender());
        logger.setConfig(errorConfig);

        System.out.println("Log level set to ERROR:");
        logger.debug("This DEBUG message should NOT appear");
        logger.info("This INFO message should NOT appear");
        logger.warning("This WARNING message should NOT appear");
        logger.error("This ERROR message should appear");
        logger.fatal("This FATAL message should appear");

        // Cleanup
        logger.shutdown();
        System.out.println("\n=== Logger shutdown complete ===");
    }
}
