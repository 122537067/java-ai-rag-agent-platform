# 项目疑问与解答记录

本文档记录项目开发过程中的关键疑问、结论和对应阶段，方便后续复习与面试整理。

## 维护规则

- 记录与项目架构、Spring Boot、Spring AI、模型 API、RAG、Tool Calling 和 MCP 有关的问题。
- 每条记录包含日期、所属迭代、问题和简要结论。
- 结论随代码演进同步更新，过时内容需要明确标注。

## 2026-07-01

### 1. 当前项目具备什么能力？

所属迭代：第一次迭代。

结论：项目最初只有 Spring Boot 骨架、健康检查、参数校验和本地回显聊天接口，不具备真实 AI 能力。

### 2. 项目必须通过 IDEA 启动吗？

所属迭代：第一次迭代。

结论：不是必须。配置好 Java 和 Maven 后可以使用命令行启动；当前电脑未把它们加入 PATH，因此使用 IDEA 更方便。

### 3. 访问根地址为什么显示 Whitelabel Error Page？

所属迭代：第一次迭代。

结论：项目没有为 `/` 配置首页，所以访问根地址返回 404。后端接口位于 `/api/health` 和 `/api/chat`。

### 4. 可以使用 Postman 测试吗？

所属迭代：第一次迭代。

结论：可以。健康检查使用 GET，聊天接口使用 POST，并在 Body 中发送 JSON。

### 5. 可以使用自己的 API 或本地模型吗？

所属迭代：第二次迭代准备阶段。

结论：可以。Spring Boot 对外提供自己的聊天 API，后端可以连接云端模型、自建模型服务或 Ollama、LM Studio 等本地模型服务。

### 6. 可以在本地部署 DeepSeek 吗？

所属迭代：第二次迭代准备阶段。

结论：可以。当前电脑适合通过 Ollama 运行 `deepseek-r1:14b`；它与 DeepSeek 官方云 API 是两种不同部署方式。

### 7. Ollama 和 DeepSeek-R1 14B 占用多少存储？

所属迭代：第二次迭代准备阶段。

结论：Ollama Windows 程序至少需要约 4GB，`deepseek-r1:14b` 默认量化模型约 9GB，建议预留 15GB 至 20GB。

### 8. 模型可以结合企业聊天话术吗？

所属迭代：第二次迭代。

结论：可以。身份、语气和通用规则适合放在 System Prompt；必须逐字一致的内容应由 Java 模板控制；大量企业资料后续使用 RAG。

### 9. 可以接入 DeepSeek 官方 API 吗？

所属迭代：第二次迭代。

结论：可以。项目通过 Spring AI 的 OpenAI-compatible 接口调用 DeepSeek，并从环境变量读取 Base URL、模型名称和 API Key。

### 10. `DEEPSEEK_API_KEY` 在哪个文件？

所属迭代：第二次迭代。

结论：它不是项目文件，而是运行环境变量。当前通过 IDEA Run Configuration 配置，`application.yml` 只保存 `${DEEPSEEK_API_KEY:}` 占位符。

### 11. DeepSeek API 是否自带联网搜索？

所属迭代：第二次迭代。

结论：当前项目没有联网搜索工具。天气、新闻和价格等实时数据需要 Java 调用外部 API，再通过 Tool Calling 把结果交给模型。

### 12. 当前程序是否具备多轮上下文？

所属迭代：第二次迭代。

结论：不具备。每次请求只包含 System Prompt 和当前消息，没有 conversationId、历史消息存储或 Chat Memory。该能力已安排在第 3.5 次迭代。

### 13. Git 提交会泄露 API Key 吗？

所属迭代：第二次迭代。

结论：当前 Git 候选文件中没有真实密钥。IDEA 的 `workspace.xml` 被 `.gitignore` 忽略，源码和示例配置只保留环境变量占位符。

### 14. 流式响应代码属于 Java 还是官方 API？

所属迭代：第三次迭代。

结论：它是多项技术的组合。`stream().content()` 来自 Spring AI，`Flux` 来自 Project Reactor，`ServerSentEvent` 来自 Spring Framework，SSE 是 Web 标准，并非 Java JDK 自带语法。

### 15. 为什么 Postman 看不出流式效果？

所属迭代：第三次迭代。

结论：服务端已经按多个 `chunk` 事件输出。Postman 需要请求 `/api/chat/stream`、设置 `Accept: text/event-stream`，并使用较长问题；也可以用 `curl -N` 关闭客户端缓冲进行验证。

### 16. 流式响应与原同步响应有什么区别？

所属迭代：第三次迭代。

结论：同步接口等待模型生成完整答案后一次返回 JSON；流式接口在生成过程中持续发送 SSE 事件，首段内容更早到达，但客户端需要拼接文本并处理结束、错误和断线状态。流式输出不会自动带来多轮上下文。

### 17. 为什么同步与流式请求的回答内容不一致？

所属迭代：第三次迭代。

结论：同步和流式接口会分别发起一次独立的模型生成，模型采样具有非确定性，因此相同问题也不保证逐字一致。流式结果还需要按顺序拼接全部 `chunk.content`，不能直接拿 SSE 原始文本与同步 JSON 比较。只有把同一次模型响应从服务端同时复制到两个输出通道，才可能保证内容完全相同。

### 18. “SSE 包装”是什么意思，这个术语什么时候出现？

所属迭代：第三次迭代。

结论：“SSE 包装”是为了便于理解而使用的描述，不是标准中的正式术语。正式说法是 Server-Sent Events 的事件流格式或事件帧：业务数据被写入 `event:`、`data:`、`id:`、`retry:` 等字段，并以空行分隔事件。SSE 原本属于 HTML5，2009 年 4 月 23 日发布首个独立 W3C Working Draft，2015 年 2 月 3 日成为 W3C Recommendation，目前由 WHATWG HTML Living Standard 持续维护。

### 19. WebSocket 和 SSE 有什么区别？

所属迭代：第三次迭代。

结论：SSE 基于普通 HTTP，主要用于服务器向客户端单向推送文本事件，浏览器可自动重连，适合 AI 文本流、通知和进度更新。WebSocket 通过 HTTP Upgrade 建立独立的全双工消息通道，客户端和服务器都能随时发送文本或二进制数据，适合游戏、实时语音和协同编辑。当前聊天场景是一次提问后持续接收回答，因此优先使用实现更简单的 SSE。

通俗理解：SSE 像收音机或直播，连接后主要由服务器持续说、客户端持续听；WebSocket 像打电话，双方随时都可以说话。当前 AI 聊天是用户先问一次、模型持续回答，所以使用 SSE 就足够了。

### 20. 第三次迭代之后要做什么？

所属迭代：第 3.5 次迭代。

结论：下一阶段实现多轮会话与上下文记忆。聊天请求会携带 `conversationId`，后端保存用户与模型消息，并在下一次调用模型时附带有限数量的历史记录。同步和流式接口都会共享这套会话能力；第一版使用内存存储，应用重启后历史会丢失，数据库或 Redis 持久化留到后续阶段。

### 21. 第 3.5 次迭代准备如何实现？

所属迭代：第 3.5 次迭代。

结论：复用 Spring AI 官方 `ChatMemory`、`MessageWindowChatMemory`、`InMemoryChatMemoryRepository` 和 `MessageChatMemoryAdvisor`。新增会话创建、查看当前记忆窗口和清空接口；聊天请求可选携带 `conversationId`，未携带时继续保持单轮无状态行为。同步与流式聊天都使用相同的会话记忆，最多保留可配置数量的最近消息。

### 22. DeepSeek 和 Codex 的会话记录分别存在哪里？

所属迭代：第 3.5 次迭代准备阶段。

结论：当前 Java 项目没有实现会话存储，每次请求只在处理期间存在于内存中，应用本地也没有聊天记录文件或数据库。DeepSeek API 是无状态接口，不会替应用维护下一轮所需的上下文；但 DeepSeek 仍可能按其隐私政策处理和留存 API 的输入、日志等平台数据，因此“无上下文”不等于“平台完全不留数据”。DeepSeek 网页或 App 的聊天历史属于 DeepSeek 账号侧数据，与本项目无关。Codex 的未归档和已归档聊天在登录 ChatGPT 后保存在 OpenAI 账号中，归档只是从侧边栏隐藏，不等于删除；本机还可看到活动会话副本位于 `C:\Users\WihenneWong\.codex\sessions`，归档会话副本位于 `C:\Users\WihenneWong\.codex\archived_sessions`。这些 Codex 数据不会写入当前 Java 项目，不应手动修改或提交到项目 Git。项目中由 Codex 创建或修改的源代码、文档仍保存在当前工作区，并由本地文件系统和 Git 管理。

### 23. 当前 Java 项目没有聊天存档，如何连接上下文？

所属迭代：第 3.5 次迭代准备阶段。

结论：上下文不一定需要永久存档。后端可以先实现短期会话记忆：客户端每次请求携带 `conversationId`，后端用这个 ID 找到最近几轮用户消息和模型回复，再把这些历史消息连同当前问题一起发送给模型。模型接口本身仍然是无状态的，真正“连接上下文”的动作发生在 Java 后端。第一版会使用内存保存会话，应用重启后丢失；后续如需长期保存，再接入数据库、Redis 或向量库。

实现状态：第 3.5 次迭代已经加入 `conversationId`、Spring AI `MessageWindowChatMemory`、内存会话窗口和 `/api/conversations` 管理接口。同步聊天 `/api/chat` 和流式聊天 `/api/chat/stream` 都可以在请求体中携带同一个 `conversationId` 来连接上下文；不携带时仍然是单轮无状态聊天。
