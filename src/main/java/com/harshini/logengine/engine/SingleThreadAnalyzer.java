package com.harshini.logengine.engine;

import com.harshini.logengine.model.LogEvent;
import com.harshini.logengine.parser.LogParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SingleThreadAnalyzer {

    public static AggregationResult analyze(Path logFile) throws IOException {
        AggregationResult result = new AggregationResult();

        try (BufferedReader reader = Files.newBufferedReader(logFile)) {
            String line;
            while ((line = reader.readLine()) != null) {
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
        }

        return result;
    }
}