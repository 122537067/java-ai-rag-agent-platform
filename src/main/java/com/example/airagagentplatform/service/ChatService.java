package com.example.airagagentplatform.service;

import com.example.airagagentplatform.dto.ChatRequest;
import com.example.airagagentplatform.dto.ChatResponse;

/**
 * 定义聊天业务能力，使控制器不依赖具体实现。
 * Defines the chat capability so the controller does not depend on a concrete implementation.
 */
public interface ChatService {

    /**
     * 根据用户请求生成聊天响应。
     * Produces a chat response for the user request.
     *
     * @param request validated chat request / 已校验的聊天请求
     * @return chat response / 聊天响应
     */
    ChatResponse chat(ChatRequest request);
}
