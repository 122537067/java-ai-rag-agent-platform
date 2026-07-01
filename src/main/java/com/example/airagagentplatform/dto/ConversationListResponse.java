package com.example.airagagentplatform.dto;

import java.util.List;

/**
 * 当前内存中已知的会话列表。
 * List of conversations currently known by the in-memory store.
 *
 * 修改时间 / Last updated: 2026-07-02 00:41 (Asia/Shanghai)
 *
 * @param conversations conversation ids / 会话 ID 列表
 */
public record ConversationListResponse(
        List<ConversationResponse> conversations
) {
}
