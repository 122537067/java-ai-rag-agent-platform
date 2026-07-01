package com.example.airagagentplatform.service;

import com.example.airagagentplatform.dto.ChatRequest;
import com.example.airagagentplatform.dto.ChatResponse;
import reactor.core.publisher.Flux;

/**
 * 定义聊天业务能力，使控制器不依赖具体实现。
 * Defines the chat capability so the controller does not depend on a concrete implementation.
 *
 * 修改时间 / Last updated: 2026-07-02 00:41 (Asia/Shanghai)
 */
public interface ChatService {

    /**
     * 根据用户请求生成聊天响应；请求可选择携带 conversationId 连接上下文。
     * Produces a chat response; the request may carry a conversation id for context.
     *
     * @param request validated chat request / 已校验的聊天请求
     * @return chat response / 聊天响应
     */
    ChatResponse chat(ChatRequest request);

    /**
     * 根据用户请求持续产生聊天文本块；请求可选择携带 conversationId 连接上下文。
     * Produces chat chunks; the request may carry a conversation id for context.
     *
     * @param request validated chat request / 已校验的聊天请求
     * @return model response chunks / 模型响应文本块
     */
    Flux<String> stream(ChatRequest request);
}
