package com.harshini.logengine.engine;
//worker job each thread runs
import com.harshini.logengine.model.LogEvent;
import com.harshini.logengine.parser.LogParser;

import java.util.List;
import java.util.concurrent.Callable;

public class BatchTask implements Callable<AggregationResult> {

    private final List<String> lines;

    public BatchTask(List<String> lines) {
        this.lines = lines;
    }

    @Override
    public AggregationResult call() {
        AggregationResult result = new AggregationResult();

        for (String line : lines) {
            result.incrementTotalLines();

            LogEvent event = LogParser.parseLine(line);
            if (event == null) {
                result.incrementMalformedLines();
                continue;
            }

            result.incrementLevel(event.getLevel());
            result.incrementSource(event.getSource());
            result.incrementMessage(event.getMessage());
        }

        return result;
    }
}