package com.example.airagagentplatform.dto;

import java.util.List;

/**
 * 当前会话窗口中保留的消息。
 * Messages retained in the current conversation window.
 *
 * 修改时间 / Last updated: 2026-07-02 00:41 (Asia/Shanghai)
 *
 * @param conversationId conversation id / 会话 ID
 * @param messageCount number of retained messages / 当前保留的消息数量
 * @param messages retained messages / 当前保留的消息
 */
public record ConversationMemoryResponse(
        String conversationId,
        int messageCount,
        List<ConversationMessageResponse> messages
) {
}
