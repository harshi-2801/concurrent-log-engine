package com.harshini.logengine.benchmark;

import com.harshini.logengine.engine.AggregationResult;
import com.harshini.logengine.engine.MultiThreadAnalyzer;
import com.harshini.logengine.engine.SingleThreadAnalyzer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BenchmarkRunner {

    public static void main(String[] args) throws Exception {
        Path input = Paths.get("sample-logs/big.log");
        Files.createDirectories(Paths.get("output"));

        int batchSize = 10_000;

        runSingle(input);
        runMulti(input, 2, batchSize);
        runMulti(input, 4, batchSize);
        runMulti(input, 8, batchSize);
    }

    private static void runSingle(Path input) throws Exception {
        long start = System.nanoTime();
        AggregationResult result = SingleThreadAnalyzer.analyze(input);
        long ms = (System.nanoTime() - start) / 1_000_000;

        printResult("single-thread", 1, result, ms);
    }

    private static void runMulti(Path input, int threads, int batchSize) throws Exception {
        long start = System.nanoTime();
        AggregationResult result = MultiThreadAnalyzer.analyze(input, threads, batchSize);
        long ms = (System.nanoTime() - start) / 1_000_000;

        printResult("multi-thread", threads, result, ms);
    }

    private static void printResult(String mode, int threads, AggregationResult result, long ms) {
        double seconds = ms / 1000.0;
        double throughput = result.getTotalLines() / Math.max(seconds, 0.0001);

        System.out.println("---- " + mode + " (" + threads + " threads) ----");
        System.out.println("Lines: " + result.getTotalLines() + ", malformed: " + result.getMalformedLines());
        System.out.println("Time: " + ms + " ms");
        System.out.println("Throughput: " + String.format("%.2f", throughput) + " lines/sec");
    }
}