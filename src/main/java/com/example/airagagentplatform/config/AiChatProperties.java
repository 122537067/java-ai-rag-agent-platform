package com.example.airagagentplatform.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * 保存项目自有的聊天配置；模型连接参数由 Spring AI 配置管理。
 * Holds application-level chat settings; Spring AI manages model connection properties.
 *
 * @param provider configured model provider name / 已配置的模型提供者名称
 * @param systemPrompt instructions applied to every conversation / 每次对话都会使用的系统提示词
 */
@Validated
@ConfigurationProperties(prefix = "app.ai")
public record AiChatProperties(
        @NotBlank String provider,
        @NotBlank String systemPrompt
) {
}
