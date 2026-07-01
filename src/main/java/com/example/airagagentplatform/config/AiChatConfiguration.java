package com.example.airagagentplatform.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 创建应用共享的 Spring AI 聊天客户端。
 * Creates the Spring AI chat client shared by the application.
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
}
