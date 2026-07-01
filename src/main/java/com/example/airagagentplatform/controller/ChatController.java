package com.example.airagagentplatform.controller;

import com.example.airagagentplatform.dto.ChatRequest;
import com.example.airagagentplatform.dto.ChatResponse;
import com.example.airagagentplatform.dto.ChatStreamResponse;
import com.example.airagagentplatform.service.ChatService;
import com.example.airagagentplatform.service.ModelProviderException;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * 接收聊天 HTTP 请求，并把业务处理委托给 {@link ChatService}。
 * Receives chat HTTP requests and delegates business processing to {@link ChatService}.
 *
 * 修改时间 / Last updated: 2026-07-02 01:32 (Asia/Shanghai)
 */
@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    /**
     * 创建聊天控制器，依赖由 Spring 自动注入。
     * Creates the chat controller with a Spring-injected service.
     *
     * @param chatService chat business service used by synchronous and streaming endpoints /
     *                    同步与流式接口共同使用的聊天业务服务
     */
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    /**
     * 处理聊天请求；{@code @Valid} 会在进入服务层前校验消息。
     * Handles a chat request; {@code @Valid} validates the message before the service is called.
     *
     * @param request chat request body / 聊天请求体
     * @return chat response / 聊天响应
     */
    @PostMapping
    public ChatResponse chat(@Valid @RequestBody ChatRequest request) {
        return chatService.chat(request);
    }

    /**
     * 通过 Server-Sent Events 持续返回模型文本块。
     * Streams model text chunks through Server-Sent Events.
     *
     * @param request chat request body / 聊天请求体
     * @return chunk, done, or error events with the conversation id / 带会话 ID 的内容、结束或错误事件
     */
    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<ChatStreamResponse>> stream(@Valid @RequestBody ChatRequest request) {
        String conversationId = request.normalizedConversationId();
        return chatService.stream(request)
                .map(content -> streamEvent("chunk", content, conversationId))
                .concatWithValues(streamEvent("done", "", conversationId))
                .onErrorResume(
                        ModelProviderException.class,
                        exception -> Flux.just(streamEvent(
                                "error",
                                "Model provider request failed.",
                                conversationId)));
    }

    /**
     * 将一个文本块包装为标准 SSE 事件。
     * Wraps one text chunk as a standard SSE event.
     *
     * @param eventName SSE event type: {@code chunk}, {@code done}, or {@code error} /
     *                  SSE 事件类型：{@code chunk}、{@code done} 或 {@code error}
     * @param content text carried by this event; empty for a normal completion event /
     *                本次事件携带的文本；正常结束事件为空字符串
     * @param conversationId conversation associated with the event; {@code null} for stateless chat /
     *                       事件对应的会话 ID；无状态聊天时为 {@code null}
     * @return fully built SSE event / 构建完成的 SSE 事件
     */
    private ServerSentEvent<ChatStreamResponse> streamEvent(
            String eventName,
            String content,
            String conversationId) {
        return ServerSentEvent.builder(new ChatStreamResponse(content, conversationId))
                .event(eventName)
                .build();
    }
}
