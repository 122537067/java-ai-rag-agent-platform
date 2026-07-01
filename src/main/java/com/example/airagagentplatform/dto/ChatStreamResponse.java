package com.example.airagagentplatform.dto;

/**
 * SSE 聊天事件携带的数据；事件名称用于区分 chunk、done 和 error。
 * Data carried by an SSE chat event; the event name distinguishes chunk, done, and error.
 *
 * 修改时间 / Last updated: 2026-07-02 00:41 (Asia/Shanghai)
 *
 * @param content streamed text or a safe error message / 流式文本或安全错误信息
 * @param conversationId conversation id used by this stream, or {@code null} for stateless chat /
 *                       本次流式响应对应的会话 ID；无状态聊天时为 {@code null}
 */
public record ChatStreamResponse(
        String content,
        String conversationId
) {
    /**
     * 保持旧代码只构造 content 的用法可用。
     * Keeps older content-only construction available.
     *
     * @param content streamed text or safe error message / 流式文本或安全错误信息
     */
    public ChatStreamResponse(String content) {
        this(content, null);
    }
}
