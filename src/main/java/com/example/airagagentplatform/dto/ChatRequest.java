package com.example.airagagentplatform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * 聊天接口的请求数据。
 * Request data accepted by the chat endpoint.
 *
 * 修改时间 / Last updated: 2026-07-02 00:41 (Asia/Shanghai)
 *
 * @param message user message; must not be blank and is limited to 4,000 characters /
 *                用户消息，不能为空且最多 4,000 个字符
 * @param conversationId optional conversation id used to connect short-term context /
 *                       可选会话 ID，用于连接短期上下文
 */
public record ChatRequest(
        @NotBlank(message = "message must not be blank")
        @Size(max = 4000, message = "message must be at most 4000 characters")
        String message,

        @Size(max = 80, message = "conversationId must be at most 80 characters")
        @Pattern(
                regexp = "^[A-Za-z0-9][A-Za-z0-9._:-]{0,79}$",
                message = "conversationId may contain only letters, numbers, dot, underscore, colon, and dash")
        String conversationId
) {
    /**
     * 保留旧测试和旧调用方式，只传消息时表示单轮无状态聊天。
     * Keeps old tests and callers working; message-only requests remain stateless.
     *
     * @param message user message / 用户消息
     */
    public ChatRequest(String message) {
        this(message, null);
    }

    /**
     * 统一处理空会话 ID，避免服务层重复判断。
     * Normalizes an empty conversation id so service code does not repeat the check.
     *
     * @return trimmed conversation id, or {@code null} for stateless chat /
     *         去除首尾空格后的会话 ID；无状态聊天返回 {@code null}
     */
    public String normalizedConversationId() {
        if (conversationId == null || conversationId.isBlank()) {
            return null;
        }
        return conversationId.trim();
    }
}
