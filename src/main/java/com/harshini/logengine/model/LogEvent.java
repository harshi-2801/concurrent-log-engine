package com.harshini.logengine.model;

public class LogEvent {
    private final String timestamp;
    private final String level;
    private final String source;
    private final String message;

    public LogEvent(String timestamp, String level, String source, String message) {
        this.timestamp = timestamp;
        this.level = level;
        this.source = source;
        this.message = message;
    }

    public String getTimestamp() { return timestamp; }
    public String getLevel() { return level; }
    public String getSource() { return source; }
    public String getMessage() { return message; }
}