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
 * 修改时间 / Last updated: 2026-07-01 22:41 (Asia/Shanghai)
 */
@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

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
     * @return chunk, done, or error events / 内容、结束或错误事件
     */
    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<ChatStreamResponse>> stream(@Valid @RequestBody ChatRequest request) {
        return chatService.stream(request)
                .map(content -> streamEvent("chunk", content))
                .concatWithValues(streamEvent("done", ""))
                .onErrorResume(
                        ModelProviderException.class,
                        exception -> Flux.just(streamEvent("error", "Model provider request failed.")));
    }

    private ServerSentEvent<ChatStreamResponse> streamEvent(String eventName, String content) {
        return ServerSentEvent.builder(new ChatStreamResponse(content))
                .event(eventName)
                .build();
    }
}
