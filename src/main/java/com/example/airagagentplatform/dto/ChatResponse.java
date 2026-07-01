package com.example.airagagentplatform.dto;

/**
 * 聊天接口的响应数据。
 * Response data returned by the chat endpoint.
 *
 * @param answer generated or placeholder answer / 生成的回答或占位回答
 */
public record ChatResponse(
        String answer
) {
}
