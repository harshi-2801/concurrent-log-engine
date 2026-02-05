package com.harshini.logengine.engine;

import java.util.HashMap;
import java.util.Map;

public class AggregationResult {
    private final Map<String, Long> levelCounts = new HashMap<>();
    private final Map<String, Long> sourceCounts = new HashMap<>();
    private final Map<String, Long> messageCounts = new HashMap<>();
    private long totalLines = 0;
    private long malformedLines = 0;

    public void incrementLevel(String level) {
        levelCounts.put(level, levelCounts.getOrDefault(level, 0L) + 1);
    }

    public void incrementSource(String source) {
        sourceCounts.put(source, sourceCounts.getOrDefault(source, 0L) + 1);
    }

    public void incrementMessage(String message) {
        messageCounts.put(message, messageCounts.getOrDefault(message, 0L) + 1);
    }

    public void incrementTotalLines() { totalLines++; }
    public void incrementMalformedLines() { malformedLines++; }

    public Map<String, Long> getLevelCounts() { return levelCounts; }
    public Map<String, Long> getSourceCounts() { return sourceCounts; }
    public Map<String, Long> getMessageCounts() { return messageCounts; }
    public long getTotalLines() { return totalLines; }
    public long getMalformedLines() { return malformedLines; }

    public void merge(AggregationResult other) {
        this.totalLines += other.totalLines;
        this.malformedLines += other.malformedLines;

        other.levelCounts.forEach((k, v) -> this.levelCounts.put(k, this.levelCounts.getOrDefault(k, 0L) + v));
        other.sourceCounts.forEach((k, v) -> this.sourceCounts.put(k, this.sourceCounts.getOrDefault(k, 0L) + v));
        other.messageCounts.forEach((k, v) -> this.messageCounts.put(k, this.messageCounts.getOrDefault(k, 0L) + v));
    }
}