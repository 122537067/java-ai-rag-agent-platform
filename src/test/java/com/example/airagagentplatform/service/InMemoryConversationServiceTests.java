package com.example.airagagentplatform.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.messages.UserMessage;

/**
 * 验证第一版内存会话服务，不涉及真实模型请求。
 * Verifies the first in-memory conversation service without real model requests.
 *
 * 修改时间 / Last updated: 2026-07-02 00:41 (Asia/Shanghai)
 */
class InMemoryConversationServiceTests {

    private ChatMemory chatMemory;
    private InMemoryConversationService conversationService;

    @BeforeEach
    void setUp() {
        ChatMemoryRepository repository = new InMemoryChatMemoryRepository();
        chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(repository)
                .maxMessages(4)
                .build();
        conversationService = new InMemoryConversationService(chatMemory, repository);
    }

    /**
     * 新建会话应返回非空 ID，并能出现在列表中。
     * A new conversation should return a non-empty id and appear in the list.
     */
    @Test
    void createConversationRegistersId() {
        var response = conversationService.createConversation();

        assertThat(response.conversationId()).isNotBlank();
        assertThat(conversationService.listConversations().conversations())
                .extracting("conversationId")
                .contains(response.conversationId());
    }

    /**
     * 会话消息应转换为 API 友好的角色和内容。
     * Conversation messages should become API-friendly role and content fields.
     */
    @Test
    void getConversationReturnsMessages() {
        var response = conversationService.createConversation();
        chatMemory.add(response.conversationId(), new UserMessage("hello"));

        var memory = conversationService.getConversation(response.conversationId());

        assertThat(memory.messageCount()).isEqualTo(1);
        assertThat(memory.messages().get(0).role()).isEqualTo("user");
        assertThat(memory.messages().get(0).content()).isEqualTo("hello");
    }

    /**
     * 清空后当前会话窗口应没有消息。
     * After clearing, the current conversation window should have no messages.
     */
    @Test
    void clearConversationRemovesMessages() {
        var response = conversationService.createConversation();
        chatMemory.add(response.conversationId(), new UserMessage("hello"));

        conversationService.clearConversation(response.conversationId());

        assertThat(conversationService.getConversation(response.conversationId()).messages()).isEmpty();
    }
}
