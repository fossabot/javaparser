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
    private Map<String, Object> properties;

    public EventContext() {
        this.logEntries = new ArrayList<>();
        this.properties = new HashMap<>();
    }

    public void addLogEntry(LogEntry logEntry) {
        assertNotNull(logEntry);
        this.logEntries.add(logEntry);
    }

    public void end() {
        this.endTimestamp = Instant.now();
    }

    public Object getProperty(String key) {
        return this.properties.get(key);
    }

    public void setProperty(String key, Object value) {
        this.properties.put(key, value);
    }

    public void start() {
        this.startTimestamp = Instant.now();
    }

}
