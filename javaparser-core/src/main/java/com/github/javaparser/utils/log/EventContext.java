package com.github.javaparser.utils.log;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.javaparser.utils.Utils.assertNotNull;

public class EventContext {

    private final String message;
    private Instant startTimestamp;
    private Instant endTimestamp;

    private List<LogEntry> logEntries;
    private Map<String, Object> attributes;

    public EventContext(String message) {
        this.message = message;
        this.logEntries = new ArrayList<>();
        this.attributes = new HashMap<>();
    }

    public void addLogEntry(LogEntry logEntry) {
        assertNotNull(logEntry);
        this.logEntries.add(logEntry);
    }

    public void end() {
        this.endTimestamp = Instant.now();
    }

    public Object getAttribute(String key) {
        return this.attributes.get(key);
    }

    public void setAttribute(String key, Object value) {
        this.attributes.put(key, value);
    }

    public void start() {
        this.startTimestamp = Instant.now();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(500);
        sb.append("-----");
        sb.append("\n").append("Event: ").append(message);

        sb.append("\n").append("- Duration: ").append(endTimestamp.toEpochMilli() - startTimestamp.toEpochMilli()).append("ms");
        sb.append("\n").append("-- Start: ").append(startTimestamp);
        sb.append("\n").append("-- End: ").append(endTimestamp);

//        sb.append("\n");
        sb.append("\n").append("- Context Attributes (" + attributes.size() + "): ");
        attributes.forEach((s, o) -> {
            sb.append("\n").append("-- ").append(s).append(": ").append(o);
        });

//        sb.append("\n");
        sb.append("\n").append("- Log Entries (" + logEntries.size() + "): ");
        logEntries.forEach(logEntry -> {
            sb.append("\n").append("-- ").append(logEntry);
        });

        return sb.toString();
    }
}
