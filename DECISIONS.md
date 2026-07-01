# Decisions

## 2026-06-26: Project Direction

Decision:

Build a Java AI application engineering portfolio project.

Reason:

The developer already has Java backend experience. The fastest and most practical AI transition path is AI application engineering, not model training.

Focus:

- Spring Boot
- Spring AI
- RAG
- Agent
- Tool Calling
- MCP

## 2026-06-26: Initial Framework Choice

Decision:

Use Spring Boot 3 and Spring AI as the first implementation direction.

Reason:

The developer has a Java background, and Spring AI fits naturally with the Spring ecosystem.

Alternative:

LangChain4j may be considered later.

## 2026-06-26: First Milestone Scope

Decision:

The first milestone only implements a basic AI chat backend.

Reason:

Avoid over-engineering. RAG, Agent, and MCP should be added only after the basic model integration works.

## 2026-06-30: Incremental Delivery Sequence

Decision:

Implement the platform in ten separate iterations. The first iteration contains only the Spring Boot foundation and a local chat endpoint. Model API integration starts in the second iteration.

Reason:

Small milestones make each concept easier to learn, test, and review. They also prevent later RAG, Tool Calling, and MCP concerns from obscuring the basic backend flow.

## 2026-07-01: DeepSeek API Integration

Decision:

Use Spring AI's OpenAI-compatible chat integration to call the DeepSeek API. Use `deepseek-v4-flash` as the default model and keep the base URL, API key, model name, and system prompt externalized.

Reason:

DeepSeek exposes an OpenAI-compatible API, while Spring AI provides a portable Java abstraction that will make future provider changes easier. Environment-based credentials prevent secrets from entering source control.

Compatibility:

Upgrade to Spring Boot 3.5.16 and use Spring AI 1.1.8 because this Spring AI generation supports Spring Boot 3.4.x and 3.5.x.

## 2026-07-01: Conversation Memory Milestone

Decision:

Add iteration 3.5 for multi-turn conversation context after streaming output and before document upload.

Reason:

Conversation memory is independent from RAG and should be established before document-based answering. The first implementation will use an in-memory store behind a replaceable storage interface, keeping a later database or Redis migration straightforward.

## 2026-07-01: Streaming Transport

Decision:

Use Spring AI's `Flux<String>` streaming API and expose it through Spring MVC as Server-Sent Events at `POST /api/chat/stream`.

Reason:

Spring MVC already supports reactive multi-value return types for `text/event-stream`, so the project can stream model output without changing the existing servlet web stack. Named `chunk`, `done`, and `error` events give clients a small and explicit protocol.
