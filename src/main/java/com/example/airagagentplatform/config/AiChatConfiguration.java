package com.example.airagagentplatform.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 创建应用共享的 Spring AI 聊天客户端。
 * Creates the Spring AI chat client shared by the application.
 *
 * 修改时间 / Last updated: 2026-07-02 00:41 (Asia/Shanghai)
 */
@Configuration
@EnableConfigurationProperties(AiChatProperties.class)
public class AiChatConfiguration {

    /**
     * 将可配置的企业话术设置为所有模型请求的默认系统提示词。
     * Applies the configurable company instructions as the default system prompt.
     *
     * @param builder auto-configured Spring AI builder / Spring AI 自动配置的构建器
     * @param properties application chat settings / 应用聊天配置
     * @return configured chat client / 配置完成的聊天客户端
     */
    @Bean
    public ChatClient chatClient(ChatClient.Builder builder, AiChatProperties properties) {
        return builder
                .defaultSystem(properties.systemPrompt())
                .build();
    }

    /**
     * 使用内存作为第一版会话存储，应用重启后会话会丢失。
     * Uses memory for the first conversation store; conversations are lost after restart.
     *
     * @return in-memory chat memory repository / 内存会话仓库
     */
    @Bean
    public ChatMemoryRepository chatMemoryRepository() {
        return new InMemoryChatMemoryRepository();
    }

    /**
     * 创建只保留最近 N 条消息的窗口记忆，避免上下文无限增长。
     * Creates windowed memory that keeps only the latest N messages.
     *
     * @param repository chat memory repository / 会话记忆仓库
     * @param properties application chat settings / 应用聊天配置
     * @return windowed chat memory / 窗口式会话记忆
     */
    @Bean
    public ChatMemory chatMemory(ChatMemoryRepository repository, AiChatProperties properties) {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(repository)
                .maxMessages(properties.memoryMaxMessages())
                .build();
    }

    /**
     * Spring AI Advisor 会在有 conversationId 的请求中读取并写入上下文。
     * The Spring AI advisor reads and writes context for requests with a conversation id.
     *
     * @param chatMemory windowed chat memory / 窗口式会话记忆
     * @return chat memory advisor / 会话记忆 Advisor
     */
    @Bean
    public MessageChatMemoryAdvisor chatMemoryAdvisor(ChatMemory chatMemory) {
        return MessageChatMemoryAdvisor.builder(chatMemory).build();
    }
}
