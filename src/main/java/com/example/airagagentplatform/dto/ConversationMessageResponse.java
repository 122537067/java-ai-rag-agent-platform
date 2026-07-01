package com.example.airagagentplatform.dto;

/**
 * 会话记忆中的一条消息。
 * A single message in the conversation memory.
 *
 * 修改时间 / Last updated: 2026-07-02 00:41 (Asia/Shanghai)
 *
 * @param role message role, such as user or assistant / 消息角色，例如 user 或 assistant
 * @param content message content / 消息内容
 */
public record ConversationMessageResponse(
        String role,
        String content
) {
}
