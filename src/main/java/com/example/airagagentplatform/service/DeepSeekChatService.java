package com.example.airagagentplatform.service;

import com.example.airagagentplatform.dto.ChatRequest;
import com.example.airagagentplatform.dto.ChatResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 使用 Spring AI 调用 DeepSeek OpenAI-compatible API。
 * Uses Spring AI to call the DeepSeek OpenAI-compatible API.
 */
@Service
public class DeepSeekChatService implements ChatService {

    private final ChatClient chatClient;

    public DeepSeekChatService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    /**
     * 将已校验的用户消息发送给 DeepSeek，并返回最终文本回答。
     * Sends the validated user message to DeepSeek and returns the final text answer.
     *
     * @param request validated chat request / 已校验的聊天请求
     * @return model response / 模型响应
     */
    @Override
    public ChatResponse chat(ChatRequest request) {
        try {
            String answer = chatClient
                    .prompt()
                    .user(request.message().trim())
                    .call()
                    .content();

            if (!StringUtils.hasText(answer)) {
                throw new ModelProviderException("DeepSeek returned an empty response.");
            }

            return new ChatResponse(answer);
        } catch (ModelProviderException exception) {
            throw exception;
        } catch (RuntimeException exception) {
            throw new ModelProviderException("DeepSeek model request failed.", exception);
        }
    }
}
