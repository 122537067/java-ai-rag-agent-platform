package com.example.airagagentplatform.dto;

/**
 * SSE 聊天事件携带的数据；事件名称用于区分 chunk、done 和 error。
 * Data carried by an SSE chat event; the event name distinguishes chunk, done, and error.
 *
 * 修改时间 / Last updated: 2026-07-01 22:41 (Asia/Shanghai)
 *
 * @param content streamed text or a safe error message / 流式文本或安全错误信息
 */
public record ChatStreamResponse(
        String content
) {
}
