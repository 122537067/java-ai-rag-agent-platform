# Java AI RAG Agent Platform

[English](README.md)

这是一个使用 Java 和 Spring Boot 分阶段构建的 AI 应用工程作品集项目。

## 当前迭代

第一次迭代只实现后端基础能力：

- Java 17 和 Maven 项目
- Spring Boot 3 应用
- 健康检查接口
- 基础本地聊天接口
- 请求参数校验和对应的控制器测试

当前聊天接口只返回本地占位回复，尚未调用模型 API。

## 环境要求

- Java 17 或更高版本
- Maven 3.9 或更高版本

## 本地运行

进入项目目录：

```bash
cd java-ai-rag-agent-platform
```

运行测试：

```bash
mvn test
```

启动应用：

```bash
mvn spring-boot:run
```

应用默认使用 `8080` 端口。如需修改端口，请在启动前设置 `SERVER_PORT`。

PowerShell 示例：

```powershell
$env:SERVER_PORT = "8081"
mvn spring-boot:run
```

`.env.example` 文件用于记录项目支持的环境变量，但 Spring Boot 不会自动加载 `.env` 文件。

## API 接口

### 健康检查

```http
GET /api/health
```

响应示例：

```json
{
  "status": "UP",
  "application": "java-ai-rag-agent-platform",
  "timestamp": "2026-06-30T08:00:00Z"
}
```

### 聊天接口

```http
POST /api/chat
Content-Type: application/json

{
  "message": "Hello"
}
```

第一次迭代的响应：

```json
{
  "answer": "Chat endpoint received: Hello"
}
```

空消息会被拒绝并返回 HTTP `400`。消息长度不能超过 4,000 个字符。

## 项目结构

```text
src/main/java/com/example/airagagentplatform
|-- config       # 应用配置
|-- controller   # REST 接口和 API 异常处理
|-- domain       # 核心领域模型
|-- dto          # API 请求和响应对象
|-- repository   # 数据持久化边界
`-- service      # 应用业务逻辑
```

在当前迭代中，`config`、`domain` 和 `repository` 包有意保持最小结构。它们只用于明确代码分层，暂时不引入尚未需要的架构。

## 开发顺序

1. Spring Boot 基础与本地聊天接口
2. 接入模型 API
3. 流式输出
4. 文档上传
5. 文本切块
6. 生成 Embedding
7. 向量检索
8. RAG 回答
9. Tool Calling
10. MCP 集成

模型凭据、模型提供者配置、Spring AI、RAG、Tool Calling 和 MCP 均不属于第一次迭代。
