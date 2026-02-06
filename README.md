# Concurrent Log Processing Engine (Java Multithreading)

## What this project does
This project reads a large structured log file and produces a summary:
- counts by log level (INFO/WARN/ERROR)
- counts by source (serviceA/serviceB/serviceC)
- counts by message

It includes:
- a single-thread baseline
- a multithreaded version using a fixed thread pool (ExecutorService)
- benchmarking to compare performance

---

## Log format
Each line in the log file looks like this:

timestamp | LEVEL | source | message

Example:
2026-02-05T10:12:30Z | ERROR | serviceA | Disk read failure

---

## How to run

### 1) Generate a large log file
Run:
- `LogGenerator.main()`

Output file:
- `sample-logs/big.log`

### 2) Run benchmark (single vs multi-thread)
Run:
- `BenchmarkRunner.main()`

This prints performance for:
- 1 thread (single-thread baseline)
- 2 threads
- 4 threads
- 8 threads

---

## Benchmark results (500,000 lines)

| Mode | Threads | Time (ms) | Throughput (lines/sec) |
|------|---------|-----------|------------------------|
| single-thread | 1 | 454 | 1,101,321 |
| multi-thread  | 2 | 268 | 1,865,671 |
| multi-thread  | 4 | 149 | 3,355,704 |
| multi-thread  | 8 | 177 | 2,824,858 |

Observation:
- 4 threads performed best on my machine.
- 8 threads was slower due to thread overhead and resource contention.

---
### Benchmark Results (1,000,000 lines)

| Mode | Threads | Time (ms) | Throughput (lines/sec) |
|------|---------|-----------|------------------------|
| Single-thread | 1 | 692 | 1,445,086 |
| Multi-thread | 2 | 452 | 2,212,389 |
| Multi-thread | 4 | 294 | 3,401,360 |
| Multi-thread | 8 | 187 | 5,347,593 |

### Observations
- Throughput increased with higher thread counts for larger workloads.
- For the 1,000,000-line input, 8 threads achieved the highest throughput.
- Compared to smaller workloads, thread scheduling overhead was outweighed by improved CPU utilization.
- This demonstrates that optimal concurrency levels depend on workload size and system characteristics.
  
---

## Design choices (simple explanation)
- Used `ExecutorService` to reuse a fixed number of threads safely.
- Split the log file into batches (10,000 lines per batch).
- Each thread processes a batch and returns its own counts.
- Final results are merged at the end to avoid race conditions.

---

## Files / classes
- `LogGenerator` generates sample logs
- `SingleThreadAnalyzer` processes logs using 1 thread
- `MultiThreadAnalyzer` processes logs using a thread pool
- `BenchmarkRunner` runs performance comparisons
