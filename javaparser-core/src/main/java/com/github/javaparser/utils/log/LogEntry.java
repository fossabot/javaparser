package com.github.javaparser.utils.log;

import java.time.Instant;

public class LogEntry {

    public enum LogLevel {
        TRACE, DEBUG, INFO, WARN, ERROR
    }

    private final Instant timestamp;
    private final LogLevel logLevel;
    private final String message;
    private final Throwable throwable;

    public LogEntry(LogLevel logLevel, String message) {
        this(logLevel, message, null);
    }

    public LogEntry(LogLevel logLevel, String message, Exception throwable) {
        this.timestamp = Instant.now();
        this.logLevel = logLevel;
        this.message = message;
        this.throwable = throwable;
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }

    public String getMessage() {
        return message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    @Override
    public String toString() {
        return "LogEntry{" +
                "timestamp=" + timestamp +
                ", logLevel=" + logLevel +
                ", message='" + message + '\'' +
                ", throwable=" + throwable +
                '}';
    }
}
