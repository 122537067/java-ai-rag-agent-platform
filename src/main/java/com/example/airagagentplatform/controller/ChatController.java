package com.example.airagagentplatform.controller;

import com.example.airagagentplatform.dto.ChatRequest;
import com.example.airagagentplatform.dto.ChatResponse;
import com.example.airagagentplatform.service.ChatService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 接收聊天 HTTP 请求，并把业务处理委托给 {@link ChatService}。
 * Receives chat HTTP requests and delegates business processing to {@link ChatService}.
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
}
