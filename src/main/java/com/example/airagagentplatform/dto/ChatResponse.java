package com.example.airagagentplatform.dto;

/**
 * 聊天接口的响应数据。
 * Response data returned by the chat endpoint.
 *
 * 修改时间 / Last updated: 2026-07-02 00:41 (Asia/Shanghai)
 *
 * @param answer generated or placeholder answer / 生成的回答或占位回答
 * @param conversationId conversation id used for this response, or {@code null} for stateless chat /
 *                       本次响应对应的会话 ID；无状态聊天时为 {@code null}
 */
public record ChatResponse(
        String answer,
        String conversationId
) {
    /**
     * 保持旧代码只构造 answer 的用法可用。
     * Keeps older answer-only construction available.
     *
     * @param answer generated answer / 生成的回答
     */
    public ChatResponse(String answer) {
        this(answer, null);
    }
}
