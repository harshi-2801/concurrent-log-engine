package com.harshini.logengine.engine;
//splits log file into chunks
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class BatchReader {

    public static List<List<String>> readBatches(Path logFile, int batchSize) throws IOException {
        List<List<String>> batches = new ArrayList<>();
        List<String> current = new ArrayList<>(batchSize);

        try (BufferedReader reader = Files.newBufferedReader(logFile)) {
            String line;
            while ((line = reader.readLine()) != null) {
                current.add(line);
                if (current.size() >= batchSize) {
                    batches.add(current);
                    current = new ArrayList<>(batchSize);
                }
            }
        }

        if (!current.isEmpty()) {
            batches.add(current);
        }

        return batches;
    }
}