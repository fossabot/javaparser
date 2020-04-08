package com.github.javaparser.utils.log;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.javaparser.utils.Utils.assertNotNull;

public class EventContext {

    private Instant startTimestamp;
    private Instant endTimestamp;

    private List<LogEntry> logEntries;
    private Map<String, Object> attributes;

    public EventContext() {
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

}
