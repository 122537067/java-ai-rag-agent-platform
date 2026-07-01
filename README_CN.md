# Java AI RAG Agent Platform

[English](README.md)

这是一个使用 Java 和 Spring Boot 分阶段构建的 AI 应用工程作品集项目。

## 当前迭代

第二次迭代已经把现有聊天接口连接到 DeepSeek API：

- Java 17 和 Maven
- Spring Boot 3.5
- Spring AI 1.1
- DeepSeek OpenAI-compatible 聊天 API
- 可配置的企业 System Prompt
- 健康检查、参数校验和统一 API 错误
- 不调用真实模型 API 的自动化测试

流式输出、文档处理、RAG、Tool Calling 和 MCP 尚未实现。

## 环境要求

- Java 17 或更高版本
- Maven 3.9 或更高版本
- 有效的 DeepSeek API Key

## DeepSeek 配置

应用通过环境变量读取模型配置。不要把真实 API Key 提交到 Git。

| 环境变量 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- |
| `AI_PROVIDER` | 否 | `deepseek` | 逻辑模型提供者名称 |
| `DEEPSEEK_API_KEY` | 是 | 无 | DeepSeek API 凭据 |
| `DEEPSEEK_BASE_URL` | 否 | `https://api.deepseek.com` | DeepSeek API 基础地址 |
| `DEEPSEEK_MODEL` | 否 | `deepseek-v4-flash` | 聊天模型名称 |
| `AI_SYSTEM_PROMPT` | 否 | 通用助手提示词 | 每次聊天使用的企业话术 |
| `SERVER_PORT` | 否 | `8080` | 本地 HTTP 端口 |

PowerShell 示例：

```powershell
$env:DEEPSEEK_API_KEY = "你的新密钥"
$env:AI_SYSTEM_PROMPT = "你是公司的专业助手，请使用简洁的中文回答。"
mvn spring-boot:run
```

使用 IntelliJ IDEA 时，打开 **Run/Debug Configurations**，选择 `AiRagAgentPlatformApplication`，在 **Environment variables** 中添加 `DEEPSEEK_API_KEY`。

`.env.example` 只用于记录配置示例，Spring Boot 不会自动加载 `.env` 文件。

## 本地运行

执行测试，测试过程不会请求真实模型：

```bash
mvn clean test
```

配置 API Key 后启动应用：

```bash
mvn spring-boot:run
```

## API 接口

### 健康检查

```http
GET /api/health
```

### 聊天接口

```http
POST /api/chat
Content-Type: application/json

{
  "message": "解释一下 Java 的依赖注入。"
}
```

响应：

```json
{
  "answer": "DeepSeek 生成的回答"
}
```

空消息返回 HTTP `400`。模型服务失败时返回 HTTP `502`，错误码为 `MODEL_PROVIDER_ERROR`。

## 项目结构

```text
src/main/java/com/example/airagagentplatform
|-- config
|   |-- AiChatConfiguration.java  # 创建 Spring AI ChatClient
|   `-- AiChatProperties.java     # 项目自己的 AI 配置
|-- controller                    # REST 接口和 API 错误处理
|-- domain                        # 后续添加核心领域模型
|-- dto                           # API 请求和响应对象
|-- repository                    # 后续添加持久化组件
`-- service
    |-- ChatService.java          # 与提供者无关的聊天接口
    `-- DeepSeekChatService.java  # DeepSeek 实现
```

## 开发顺序

1. Spring Boot 基础与本地聊天接口：已完成
2. DeepSeek 模型 API：已完成
3. 流式输出
4. 文档上传
5. 文本切块
6. 生成 Embedding
7. 向量检索
8. RAG 回答
9. Tool Calling
10. MCP 集成
