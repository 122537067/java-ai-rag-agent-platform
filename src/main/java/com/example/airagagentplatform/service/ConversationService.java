package com.example.airagagentplatform.service;

import com.example.airagagentplatform.dto.ConversationListResponse;
import com.example.airagagentplatform.dto.ConversationMemoryResponse;
import com.example.airagagentplatform.dto.ConversationResponse;

/**
 * 定义会话记忆管理能力，便于控制器不直接依赖 Spring AI 存储实现。
 * Defines conversation memory management so controllers do not depend on Spring AI storage.
 *
 * 修改时间 / Last updated: 2026-07-02 00:41 (Asia/Shanghai)
 */
public interface ConversationService {

    /**
     * 创建一个新的会话 ID，并在内存仓库中登记。
     * Creates a new conversation id and registers it in the in-memory repository.
     *
     * @return new conversation id / 新会话 ID
     */
    ConversationResponse createConversation();

    /**
     * 列出当前应用进程内已知的会话。
     * Lists conversations known by the current application process.
     *
     * @return conversation list / 会话列表
     */
    ConversationListResponse listConversations();

    /**
     * 查看某个会话当前保留的消息窗口。
     * Reads the retained message window for a conversation.
     *
     * @param conversationId conversation id / 会话 ID
     * @return retained conversation memory / 已保留的会话记忆
     */
    ConversationMemoryResponse getConversation(String conversationId);

    /**
     * 清空某个会话在内存中的消息。
     * Clears a conversation from memory.
     *
     * @param conversationId conversation id / 会话 ID
     */
    void clearConversation(String conversationId);
}
