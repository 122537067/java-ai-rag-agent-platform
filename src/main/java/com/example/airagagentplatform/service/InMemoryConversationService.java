package com.example.airagagentplatform.service;

import com.example.airagagentplatform.dto.ConversationListResponse;
import com.example.airagagentplatform.dto.ConversationMemoryResponse;
import com.example.airagagentplatform.dto.ConversationMessageResponse;
import com.example.airagagentplatform.dto.ConversationResponse;
import java.util.List;
import java.util.UUID;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.messages.Message;
import org.springframework.stereotype.Service;

/**
 * 使用 Spring AI 内存记忆实现第一版会话管理。
 * Implements the first conversation manager with Spring AI in-memory chat memory.
 *
 * 修改时间 / Last updated: 2026-07-02 01:32 (Asia/Shanghai)
 */
@Service
public class InMemoryConversationService implements ConversationService {

    private final ChatMemory chatMemory;
    private final ChatMemoryRepository chatMemoryRepository;

    /**
     * 创建内存会话服务，复用 Spring AI 的窗口记忆和底层仓库。
     * Creates the in-memory conversation service using Spring AI memory components.
     *
     * @param chatMemory window memory used to read and clear retained messages /
     *                   用于读取和清空已保留消息的窗口记忆
     * @param chatMemoryRepository repository used to register and list conversation ids /
     *                             用于登记和列出会话 ID 的底层仓库
     */
    public InMemoryConversationService(ChatMemory chatMemory, ChatMemoryRepository chatMemoryRepository) {
        this.chatMemory = chatMemory;
        this.chatMemoryRepository = chatMemoryRepository;
    }

    /**
     * 生成 UUID 作为客户端后续复用的 conversationId。
     * Generates a UUID for the client to reuse as conversationId.
     *
     * @return new conversation id / 新会话 ID
     */
    @Override
    public ConversationResponse createConversation() {
        String conversationId = UUID.randomUUID().toString();
        chatMemoryRepository.saveAll(conversationId, List.of());
        return new ConversationResponse(conversationId);
    }

    /**
     * 返回当前进程内存中登记过的会话 ID。
     * Returns conversation ids registered in the current process memory.
     *
     * @return conversation list / 会话列表
     */
    @Override
    public ConversationListResponse listConversations() {
        List<ConversationResponse> conversations = chatMemoryRepository.findConversationIds()
                .stream()
                .map(ConversationResponse::new)
                .toList();
        return new ConversationListResponse(conversations);
    }

    /**
     * 读取窗口记忆并转换为项目自己的 DTO，避免 API 暴露 Spring AI 内部类型。
     * Reads window memory and converts it to DTOs so the API does not expose Spring AI types.
     *
     * @param conversationId conversation id / 会话 ID
     * @return retained memory / 已保留记忆
     */
    @Override
    public ConversationMemoryResponse getConversation(String conversationId) {
        List<ConversationMessageResponse> messages = chatMemory.get(conversationId)
                .stream()
                .map(this::toResponse)
                .toList();
        return new ConversationMemoryResponse(conversationId, messages.size(), messages);
    }

    /**
     * 从内存中删除指定会话。
     * Deletes the specified conversation from memory.
     *
     * @param conversationId conversation id / 会话 ID
     */
    @Override
    public void clearConversation(String conversationId) {
        chatMemory.clear(conversationId);
    }

    /**
     * 将 Spring AI 消息转换为不会暴露框架内部类型的接口 DTO。
     * Converts a Spring AI message into an API DTO that hides framework internals.
     *
     * @param message retained Spring AI message containing its role and text /
     *                Spring AI 保留的消息，包含角色与文本
     * @return project-owned conversation message DTO / 项目自有的会话消息 DTO
     */
    private ConversationMessageResponse toResponse(Message message) {
        return new ConversationMessageResponse(
                message.getMessageType().getValue(),
                message.getText());
    }
}
