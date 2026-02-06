# Concurrent Log Processing Engine & Event Ingestion System

This project demonstrates a backend-focused, systems-oriented application consisting of a high-performance concurrent log processing engine (Java), a Spring Boot backend API for event ingestion, and a native C++ client that publishes events over HTTP. The system is validated end-to-end with benchmarking, performance analysis, and persistent storage.

The project showcases Java backend development, concurrency, REST APIs, client–server communication, and system-level design.

## Architecture Overview

C++ Client  --->  Spring Boot API  --->  In-memory + File Storage  
                      |  
                      └── Health Monitoring (Actuator)

## Java Backend (Spring Boot)

The Java backend is implemented using Spring Boot and exposes REST APIs for event ingestion and retrieval. Events are stored in memory for fast access and appended to disk for persistence.

### Features
- RESTful API for ingesting and querying events
- In-memory storage for recent events
- Persistent storage using JSON Lines format
- Health endpoint for observability

### Endpoints

| Method | Endpoint | Description |
|------|---------|-------------|
| POST | /events | Ingest a new event |
| GET | /events?limit=N | Fetch last N ingested events |
| GET | /actuator/health | Service health check |

### Event Format (JSON)

{
  "timestamp": "2026-02-06T21:00:00Z",
  "level": "ERROR",
  "source": "serviceA",
  "message": "Disk read failure"
}

### Running the Backend

Run the JavaBackendApplication class from IntelliJ.

Verify service health using:
curl http://localhost:8080/actuator/health

Expected response:
{"status":"UP"}

### Testing the Backend with curl

POST an event:
curl -i -X POST http://localhost:8080/events \
  -H "Content-Type: application/json" \
  -d '{"timestamp":"2026-02-06T20:30:00Z","level":"ERROR","source":"serviceA","message":"Disk read failure"}'

GET recent events:
curl "http://localhost:8080/events?limit=5"

## C++ Client (libcurl)

A lightweight native C++ client is implemented using libcurl to publish structured JSON events to the Java backend via HTTP.

### Build
g++ -std=c++17 cpp-client/main.cpp -o cpp-client/client $(curl-config --cflags --libs)

### Run
./cpp-client/client

Verify ingestion:
curl "http://localhost:8080/events?limit=5"

The returned events should include an entry with source set to "cpp-client".

## Concurrent Log Processing Engine (Java)

A high-performance log processing engine processes large structured log files using both single-threaded and multi-threaded execution models.

### Features
- Java concurrency utilities (ExecutorService, thread pools)
- Configurable thread counts
- Throughput and latency benchmarking
- Structured aggregation (level counts, source counts, message counts)
- JSON output generation

### Benchmark Results (500,000 lines)

| Threads | Time (ms) | Throughput (lines/sec) |
|--------|-----------|------------------------|
| 1 | 454 | 1,101,321 |
| 2 | 268 | 1,865,671 |
| 4 | 149 | 3,355,704 |
| 8 | 177 | 2,824,858 |

Observation: Performance improved up to 4 threads, after which scheduling and coordination overhead reduced efficiency.

### Benchmark Results (1,000,000 lines)

| Threads | Time (ms) | Throughput (lines/sec) |
|--------|-----------|------------------------|
| 1 | 692 | 1,445,086 |
| 2 | 452 | 2,212,389 |
| 4 | 294 | 3,401,360 |
| 8 | 187 | 5,347,593 |

Observation: For larger workloads, higher thread counts improved throughput due to better CPU utilization.

## Data Persistence

All ingested backend events are stored in a file named received-events.jsonl. Each line in the file represents a single JSON event, enabling easy inspection, replay, and offline analysis.

Example:
cat received-events.jsonl | tail -n 5

## Key Engineering Concepts Demonstrated

- Java backend development with Spring Boot
- REST API design and validation
- Concurrency and multithreading
- Client–server communication
- Native C++ integration
- Performance benchmarking and analysis
- Observability and health checks
- Clean package structure and separation of concerns

## Future Enhancements

- Kafka-based streaming ingestion
- Database-backed storage (PostgreSQL)
- Containerization with Docker
- Kubernetes deployment
- Authentication and authorization
- Metrics and monitoring dashboards

## Summary

This project demonstrates an end-to-end system combining Java backend services, concurrency-focused log processing, and native C++ client integration. The design emphasizes correctness, performance, and maintainability while remaining simple and extensible.
