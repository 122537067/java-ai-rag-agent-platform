package com.example.airagagentplatform.config;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * 保存项目自有的聊天配置；模型连接参数由 Spring AI 配置管理。
 * Holds application-level chat settings; Spring AI manages model connection properties.
 *
 * 修改时间 / Last updated: 2026-07-02 00:41 (Asia/Shanghai)
 *
 * @param provider configured model provider name / 已配置的模型提供者名称
 * @param systemPrompt instructions applied to every conversation / 每次对话都会使用的系统提示词
 * @param memoryMaxMessages maximum messages kept per conversation window /
 *                          每个会话窗口最多保留的消息数量
 */
@Validated
@ConfigurationProperties(prefix = "app.ai")
public record AiChatProperties(
        @NotBlank String provider,
        @NotBlank String systemPrompt,
        @Min(2) @Max(100) int memoryMaxMessages
) {
}
