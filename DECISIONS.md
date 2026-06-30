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
