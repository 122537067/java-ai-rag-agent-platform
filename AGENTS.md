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
- When a Java source file is added or meaningfully changed, add or update a class-level or package-level Javadoc timestamp using `修改时间 / Last updated: YYYY-MM-DD HH:mm (Asia/Shanghai)`.
- Keep timestamps at file-level documentation instead of adding them to every line or trivial statement.
- Record each meaningful project-related user question and its concise conclusion in `QUESTIONS_CN.md`.
- Keep question records aligned with the current implementation and roadmap.

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
