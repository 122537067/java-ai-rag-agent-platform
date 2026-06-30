# AGENTS.md

## Project Role

This is a Java AI application engineering portfolio project.

The developer is a Java backend developer transitioning into AI application engineering.

The target direction is:

- Spring Boot
- Spring AI
- RAG
- Agent workflows
- Tool Calling
- MCP
- Enterprise AI applications

## Project Rules

- Use Java 17+ and Spring Boot 3.
- Prefer Maven.
- Keep the architecture clear and beginner-friendly.
- Use packages such as controller, service, repository, domain, dto, config.
- Do not hardcode API keys or secrets.
- Load model provider, API key, base URL, and model name from environment variables or config files.
- Keep README updated when major features are added.
- Prefer small, reviewable changes.
- Before adding complex architecture, explain why it is needed.

## Current Priority

Implement the project in phases.

Phase 1 only needs:

- Spring Boot project skeleton
- Health check endpoint
- Basic chat endpoint
- Optional streaming chat endpoint
- Model provider configuration
- README setup instructions

Do not implement RAG, Agent, or MCP until the basic chat backend works.