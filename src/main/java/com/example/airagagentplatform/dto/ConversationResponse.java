package com.example.airagagentplatform.dto;

/**
 * 会话标识响应，用于告诉客户端后续请求应携带哪个 conversationId。
 * Conversation id response that tells the client which conversationId to reuse.
 *
 * 修改时间 / Last updated: 2026-07-02 00:41 (Asia/Shanghai)
 *
 * @param conversationId generated or existing conversation id / 生成或已有的会话 ID
 */
public record ConversationResponse(
        String conversationId
) {
}
