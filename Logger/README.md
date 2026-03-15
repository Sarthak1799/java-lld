# Logging Framework - Low Level Design

## Overview
A thread-safe, extensible logging framework that supports multiple log levels and output destinations.

## Features
✅ Multiple log levels: DEBUG, INFO, WARNING, ERROR, FATAL
✅ Timestamp, log level, and message content for each log
✅ Multiple output destinations (Console, File) - extensible for Database
✅ Configuration mechanism for log level and output destination
✅ Thread-safe singleton logger
✅ Extensible design for new log levels and appenders

## Project Structure
```
src/main/java/com/logger/
├── enums/
│   └── LogLevel.java          # Enum defining log levels with priorities
├── models/
│   └── LogMessage.java         # Model representing a log message
├── appenders/
│   ├── LogAppender.java        # Interface for log appenders
│   ├── ConsoleAppender.java    # Console output implementation
│   └── FileAppender.java       # File output implementation
├── config/
│   └── LoggerConfig.java       # Logger configuration class
├── Logger.java                 # Thread-safe singleton logger
└── Main.java                   # Demo application
```

## Components

### 1. LogLevel Enum
- Defines log levels: DEBUG, INFO, WARNING, ERROR, FATAL
- Each level has a priority for filtering
- `isLoggable()` method determines if a message should be logged

### 2. LogMessage Class
- Immutable representation of a log entry
- Contains: timestamp, log level, message
- Automatic timestamp generation
- Formatted string output

### 3. LogAppender Interface
- Contract for output destinations
- Methods: `append()` and `close()`
- Easy to extend for new destinations (Database, Network, etc.)

### 4. Concrete Appenders
- **ConsoleAppender**: Logs to standard output
- **FileAppender**: Logs to file with buffered writing (thread-safe)
- Future: DatabaseAppender, RemoteAppender, etc.

### 5. LoggerConfig Class
- Holds configuration: log level and appender
- Can be updated dynamically

### 6. Logger Class (Singleton)
- Thread-safe using double-checked locking
- Convenience methods: debug(), info(), warning(), error(), fatal()
- Synchronized configuration updates
- Proper resource cleanup with shutdown()

## How to Run
```bash
# Compile
javac -d bin src/main/java/com/logger/**/*.java src/main/java/com/logger/*.java

# Run
java -cp bin com.logger.Main
```

## Usage Example
```java
// Get logger instance
Logger logger = Logger.getInstance();

// Configure with console output at INFO level
LogAppender appender = new ConsoleAppender();
LoggerConfig config = new LoggerConfig(LogLevel.INFO, appender);
logger.setConfig(config);

// Log messages
logger.debug("Debug message");     // Won't appear (below INFO)
logger.info("Info message");       // Will appear
logger.error("Error message");     // Will appear

// Change configuration dynamically
LogAppender fileAppender = new FileAppender("app.log");
LoggerConfig fileConfig = new LoggerConfig(LogLevel.DEBUG, fileAppender);
logger.setConfig(fileConfig);

// Cleanup
logger.shutdown();
```

## Thread Safety
- Singleton pattern with double-checked locking
- Synchronized configuration access
- FileAppender uses synchronized append method
- Tested with multi-threaded demo

## Extensibility

### Adding New Log Levels
1. Add new level to `LogLevel` enum with appropriate priority
2. Optionally add convenience method in `Logger` class

### Adding New Appenders
1. Implement `LogAppender` interface
2. Implement `append()` and `close()` methods
3. Example: DatabaseAppender, ElasticsearchAppender, etc.

```java
public class DatabaseAppender implements LogAppender {
    @Override
    public void append(LogMessage logMessage) {
        // Database insertion logic
    }
    
    @Override
    public void close() {
        // Close DB connection
    }
}
```

## Design Patterns Used
1. **Singleton Pattern**: Logger class
2. **Strategy Pattern**: LogAppender interface with multiple implementations
3. **Builder Pattern**: Can be extended for LoggerConfig
4. **Factory Pattern**: Can be added for Appender creation

## Future Enhancements
- Add DatabaseAppender for database logging
- Implement log rotation for FileAppender
- Add async logging with queue
- Support log formatting templates
- Add log filtering based on message content
- Implement composite appender for multiple destinations
- Add MDC (Mapped Diagnostic Context) for contextual logging
