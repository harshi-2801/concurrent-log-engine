package com.harshini.logengine.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Random;
import java.nio.file.Paths;

public class LogGenerator {

    private static final String[] LEVELS = {"INFO", "WARN", "ERROR"};
    private static final String[] SOURCES = {"serviceA", "serviceB", "serviceC"};
    private static final String[] MESSAGES = {
            "Disk read failure",
            "Connection timeout",
            "Null pointer exception",
            "Memory usage high",
            "Request processed successfully"
    };

    public static void main(String[] args) throws IOException {
        int lines = 1_000_000;
        Path output = Paths.get("sample-logs/big.log");

        Files.createDirectories(output.getParent());

        Random random = new Random();

        try (BufferedWriter writer = Files.newBufferedWriter(output)) {
            for (int i = 0; i < lines; i++) {
                String logLine = String.format(
                        "%s | %s | %s | %s",
                        Instant.now().toString(),
                        LEVELS[random.nextInt(LEVELS.length)],
                        SOURCES[random.nextInt(SOURCES.length)],
                        MESSAGES[random.nextInt(MESSAGES.length)]
                );
                writer.write(logLine);
                writer.newLine();
            }
        }

        System.out.println("Generated log file with " + lines + " lines at " + output.toAbsolutePath());
    }
}