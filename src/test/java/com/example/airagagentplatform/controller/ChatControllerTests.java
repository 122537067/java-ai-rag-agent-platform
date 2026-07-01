package com.example.airagagentplatform.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.airagagentplatform.dto.ChatResponse;
import com.example.airagagentplatform.service.ChatService;
import com.example.airagagentplatform.service.ModelProviderException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import reactor.core.publisher.Flux;

/**
 * 验证聊天接口的正常响应和参数校验行为。
 * Verifies successful chat responses and request validation behavior.
 *
 * 修改时间 / Last updated: 2026-07-01 22:41 (Asia/Shanghai)
 */
@WebMvcTest(ChatController.class)
class ChatControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ChatService chatService;

    /**
     * 有效消息应返回 HTTP 200 和模型回答。
     * A valid message should return HTTP 200 and the model answer.
     */
    @Test
    void chatReturnsModelResponse() throws Exception {
        when(chatService.chat(any())).thenReturn(new ChatResponse("DeepSeek answer for hello"));

        mockMvc.perform(post("/api/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"message\":\"hello\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.answer", containsString("hello")));
    }

    /**
     * 空白消息应被 DTO 校验拒绝。
     * A blank message should be rejected by DTO validation.
     */
    @Test
    void chatRejectsBlankMessage() throws Exception {
        mockMvc.perform(post("/api/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"message\":\"   \"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    /**
     * 模型提供者失败时应返回 HTTP 502 和稳定的错误码。
     * A model provider failure should return HTTP 502 and a stable error code.
     */
    @Test
    void chatReturnsBadGatewayWhenModelProviderFails() throws Exception {
        when(chatService.chat(any()))
                .thenThrow(new ModelProviderException("DeepSeek model request failed."));

        mockMvc.perform(post("/api/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"message\":\"hello\"}"))
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$.code").value("MODEL_PROVIDER_ERROR"));
    }

    /**
     * 流式接口应依次返回内容事件和结束事件。
     * The streaming endpoint should return chunk events followed by a done event.
     */
    @Test
    void streamReturnsChunkAndDoneEvents() throws Exception {
        when(chatService.stream(any())).thenReturn(Flux.just("Hello", " ", "world"));

        var result = mockMvc.perform(post("/api/chat/stream")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.TEXT_EVENT_STREAM)
                        .content("{\"message\":\"hello\"}"))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(result))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_EVENT_STREAM))
                .andExpect(content().string(containsString("event:chunk")))
                .andExpect(content().string(containsString("data:{\"content\":\"Hello\"}")))
                .andExpect(content().string(containsString("event:done")));
    }

    /**
     * 流开始后的模型异常应作为 SSE error 事件返回。
     * A provider failure after streaming starts should be returned as an SSE error event.
     */
    @Test
    void streamReturnsErrorEventWhenModelProviderFails() throws Exception {
        when(chatService.stream(any()))
                .thenReturn(Flux.error(new ModelProviderException("DeepSeek model request failed.")));

        var result = mockMvc.perform(post("/api/chat/stream")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.TEXT_EVENT_STREAM)
                        .content("{\"message\":\"hello\"}"))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(result))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("event:error")))
                .andExpect(content().string(containsString("Model provider request failed.")));
    }
}
