# Roadmap

The project is implemented in ten small, reviewable iterations.

## Iteration 1: Spring Boot and Chat Endpoint (Complete)

- Create the Java 17 and Maven Spring Boot 3 project
- Add a health check endpoint
- Add a validated local chat endpoint
- Do not connect to an external model

## Iteration 2: DeepSeek Model API (Complete)

- Add model provider configuration
- Load credentials from environment variables
- Connect the chat service to DeepSeek through Spring AI
- Add a configurable system prompt
- Map provider failures to a stable API error

## Iteration 3: Streaming Output

- Add a streaming chat endpoint
- Stream model output to the client

## Iteration 4: Document Upload

- Upload supported document formats
- Validate file type and size

## Iteration 5: Text Chunking

- Extract document text
- Split text into configurable chunks

## Iteration 6: Embeddings

- Generate embeddings for chunks
- Keep embedding provider configuration external

## Iteration 7: Vector Retrieval

- Store vectors
- Retrieve semantically relevant chunks

## Iteration 8: RAG Answers

- Build prompts from retrieved context
- Return grounded answers and source references

## Iteration 9: Tool Calling

- Define callable Java tools
- Validate and log tool execution

## Iteration 10: MCP

- Expose selected capabilities through MCP
- Document MCP tools and integration setup
