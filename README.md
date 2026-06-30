# Java AI RAG Agent Platform

[中文说明](README_CN.md)

A Java AI application engineering portfolio project built incrementally with Java and Spring Boot.

## Current Iteration

Iteration 1 only provides the backend foundation:

- Java 17 and Maven project
- Spring Boot 3 application
- Health check endpoint
- Basic local chat endpoint
- Request validation and focused controller tests

The chat endpoint currently returns a local placeholder response. It does not call a model API yet.

## Requirements

- Java 17 or later
- Maven 3.9 or later

## Local Setup

Enter the project directory:

```bash
cd java-ai-rag-agent-platform
```

Run the tests:

```bash
mvn test
```

Start the application:

```bash
mvn spring-boot:run
```

The application starts on port `8080`. To use another port, set `SERVER_PORT` before starting it.

PowerShell example:

```powershell
$env:SERVER_PORT = "8081"
mvn spring-boot:run
```

The `.env.example` file records supported environment variables, but Spring Boot does not load `.env` files automatically.

## API Endpoints

### Health Check

```http
GET /api/health
```

Example response:

```json
{
  "status": "UP",
  "application": "java-ai-rag-agent-platform",
  "timestamp": "2026-06-30T08:00:00Z"
}
```

### Chat

```http
POST /api/chat
Content-Type: application/json

{
  "message": "Hello"
}
```

Iteration 1 response:

```json
{
  "answer": "Chat endpoint received: Hello"
}
```

Blank messages are rejected with HTTP `400`. Messages are limited to 4,000 characters.

## Project Structure

```text
src/main/java/com/example/airagagentplatform
|-- config       # Application configuration
|-- controller   # REST endpoints and API exception handling
|-- domain       # Core domain models
|-- dto          # API request and response records
|-- repository   # Persistence boundaries
`-- service      # Application business logic
```

The `config`, `domain`, and `repository` packages are intentionally minimal in this iteration. They establish the package boundaries without adding architecture before it is needed.

## Delivery Sequence

1. Spring Boot foundation and local chat endpoint
2. Model API integration
3. Streaming output
4. Document upload
5. Text chunking
6. Embedding generation
7. Vector retrieval
8. RAG answers
9. Tool Calling
10. MCP integration

Model credentials, provider configuration, Spring AI, RAG, Tool Calling, and MCP are not part of iteration 1.
