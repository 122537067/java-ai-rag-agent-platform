package com.example.airagagentplatform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 聊天接口的请求数据。
 * Request data accepted by the chat endpoint.
 *
 * @param message user message; must not be blank and is limited to 4,000 characters /
 *                用户消息，不能为空且最多 4,000 个字符
 */
public record ChatRequest(
        @NotBlank(message = "message must not be blank")
        @Size(max = 4000, message = "message must be at most 4000 characters")
        String message
) {
}
