package com.harshini.logengine.parser;

import com.harshini.logengine.model.LogEvent;

public class LogParser {

    // Expected format:
    // timestamp | LEVEL | source | message
    public static LogEvent parseLine(String line) {
        String[] parts = line.split("\\s\\|\\s", 4);
        if (parts.length < 4) {
            // Skip malformed line
            return null;
        }
        return new LogEvent(parts[0], parts[1], parts[2], parts[3]);
    }
}