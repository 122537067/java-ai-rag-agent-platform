package com.example.airagagentplatform.service;

import com.example.airagagentplatform.dto.ChatRequest;
import com.example.airagagentplatform.dto.ChatResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;

/**
 * 使用 Spring AI 调用 DeepSeek OpenAI-compatible API。
 * Uses Spring AI to call the DeepSeek OpenAI-compatible API.
 *
 * 修改时间 / Last updated: 2026-07-02 01:32 (Asia/Shanghai)
 */
@Service
public class DeepSeekChatService implements ChatService {

    private final ChatClient chatClient;
    private final MessageChatMemoryAdvisor chatMemoryAdvisor;

    /**
     * 创建 DeepSeek 聊天服务，两个依赖都由 Spring 配置类提供。
     * Creates the DeepSeek chat service with dependencies supplied by Spring configuration.
     *
     * @param chatClient client that sends synchronous or streaming requests to the model API /
     *                   向模型 API 发送同步或流式请求的客户端
     * @param chatMemoryAdvisor advisor that loads and stores messages by conversation id /
     *                          按会话 ID 读取和保存消息的记忆 Advisor
     */
    public DeepSeekChatService(ChatClient chatClient, MessageChatMemoryAdvisor chatMemoryAdvisor) {
        this.chatClient = chatClient;
        this.chatMemoryAdvisor = chatMemoryAdvisor;
    }

    /**
     * 将已校验的用户消息发送给 DeepSeek；带 conversationId 时会自动带上短期上下文。
     * Sends the validated user message to DeepSeek; a conversation id enables short-term context.
     *
     * @param request validated chat request / 已校验的聊天请求
     * @return model response / 模型响应
     */
    @Override
    public ChatResponse chat(ChatRequest request) {
        try {
            String answer = prompt(request)
                    .call()
                    .content();

            if (!StringUtils.hasText(answer)) {
                throw new ModelProviderException("DeepSeek returned an empty response.");
            }

            return new ChatResponse(answer, request.normalizedConversationId());
        } catch (ModelProviderException exception) {
            throw exception;
        } catch (RuntimeException exception) {
            throw new ModelProviderException("DeepSeek model request failed.", exception);
        }
    }

    /**
     * 将 DeepSeek 生成的文本块作为响应式流返回；带 conversationId 时共享同一套上下文。
     * Returns DeepSeek-generated text chunks as a stream; a conversation id shares the same context.
     *
     * @param request validated chat request / 已校验的聊天请求
     * @return model response chunks / 模型响应文本块
     */
    @Override
    public Flux<String> stream(ChatRequest request) {
        try {
            return prompt(request)
                    .stream()
                    .content()
                    .filter(StringUtils::hasLength)
                    .switchIfEmpty(Flux.error(
                            new ModelProviderException("DeepSeek returned an empty response.")))
                    .onErrorMap(
                            exception -> !(exception instanceof ModelProviderException),
                            exception -> new ModelProviderException(
                                    "DeepSeek model request failed.", exception));
        } catch (RuntimeException exception) {
            return Flux.error(new ModelProviderException("DeepSeek model request failed.", exception));
        }
    }

    /**
     * 构建模型请求；只有请求携带 conversationId 时才启用上下文记忆。
     * Builds a model request and enables memory only when a conversation id is present.
     *
     * @param request validated request containing the user message and optional conversation id /
     *                已校验的请求，包含用户消息和可选会话 ID
     * @return request specification ready for synchronous or streaming execution /
     *         可用于同步或流式执行的请求定义
     */
    private ChatClient.ChatClientRequestSpec prompt(ChatRequest request) {
        ChatClient.ChatClientRequestSpec prompt = chatClient
                .prompt()
                .user(request.message().trim());

        String conversationId = request.normalizedConversationId();
        if (!StringUtils.hasText(conversationId)) {
            return prompt;
        }

        return prompt.advisors(advisor -> {
            advisor.advisors(chatMemoryAdvisor);
            advisor.param(ChatMemory.CONVERSATION_ID, conversationId);
        });
    }
}
