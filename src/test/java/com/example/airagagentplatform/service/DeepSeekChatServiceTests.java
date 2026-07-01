package com.example.airagagentplatform.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.example.airagagentplatform.dto.ChatRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * 使用模拟 ChatClient 验证 DeepSeek 服务，不发送真实网络请求。
 * Verifies the DeepSeek service with a mocked ChatClient and no real network requests.
 *
 * 修改时间 / Last updated: 2026-07-02 00:41 (Asia/Shanghai)
 */
@ExtendWith(MockitoExtension.class)
class DeepSeekChatServiceTests {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ChatClient chatClient;

    private DeepSeekChatService chatService;

    @BeforeEach
    void setUp() {
        var chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .maxMessages(20)
                .build();
        var chatMemoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
        chatService = new DeepSeekChatService(chatClient, chatMemoryAdvisor);
    }

    /**
     * 模型文本应被转换为项目自己的 ChatResponse。
     * Model text should be converted into the application's ChatResponse.
     */
    @Test
    void chatReturnsModelAnswer() {
        when(chatClient.prompt().user("hello").call().content())
                .thenReturn("Hello from DeepSeek");

        var response = chatService.chat(new ChatRequest(" hello "));

        assertThat(response.answer()).isEqualTo("Hello from DeepSeek");
    }

    /**
     * 空模型响应应转换为明确的提供者异常。
     * An empty model response should become an explicit provider exception.
     */
    @Test
    void chatRejectsEmptyModelAnswer() {
        when(chatClient.prompt().user("hello").call().content()).thenReturn("   ");

        assertThatThrownBy(() -> chatService.chat(new ChatRequest("hello")))
                .isInstanceOf(ModelProviderException.class)
                .hasMessage("DeepSeek returned an empty response.");
    }

    /**
     * Spring AI 调用异常应转换为项目自己的异常，避免泄露底层细节。
     * Spring AI failures should be wrapped to avoid leaking implementation details.
     */
    @Test
    void chatWrapsProviderFailure() {
        when(chatClient.prompt().user("hello").call().content())
                .thenThrow(new IllegalStateException("upstream failure"));

        assertThatThrownBy(() -> chatService.chat(new ChatRequest("hello")))
                .isInstanceOf(ModelProviderException.class)
                .hasMessage("DeepSeek model request failed.")
                .hasCauseInstanceOf(IllegalStateException.class);
    }

    /**
     * 流式调用应保留模型返回的文本块顺序，包括空格。
     * Streaming should preserve model chunk order, including whitespace.
     */
    @Test
    void streamReturnsModelChunks() {
        when(chatClient.prompt().user("hello").stream().content())
                .thenReturn(Flux.just("Hello", " ", "world"));

        StepVerifier.create(chatService.stream(new ChatRequest(" hello ")))
                .expectNext("Hello", " ", "world")
                .verifyComplete();
    }

    /**
     * 没有任何文本块时应发出明确的提供者异常。
     * A stream without text chunks should emit an explicit provider exception.
     */
    @Test
    void streamRejectsEmptyModelResponse() {
        when(chatClient.prompt().user("hello").stream().content())
                .thenReturn(Flux.empty());

        StepVerifier.create(chatService.stream(new ChatRequest("hello")))
                .expectErrorMatches(exception -> exception instanceof ModelProviderException
                        && exception.getMessage().equals("DeepSeek returned an empty response."))
                .verify();
    }

    /**
     * 流中的底层异常应转换为项目自己的模型异常。
     * A low-level stream failure should be converted into the application's provider exception.
     */
    @Test
    void streamWrapsProviderFailure() {
        when(chatClient.prompt().user("hello").stream().content())
                .thenReturn(Flux.error(new IllegalStateException("upstream failure")));

        StepVerifier.create(chatService.stream(new ChatRequest("hello")))
                .expectErrorMatches(exception -> exception instanceof ModelProviderException
                        && exception.getMessage().equals("DeepSeek model request failed.")
                        && exception.getCause() instanceof IllegalStateException)
                .verify();
    }
}
