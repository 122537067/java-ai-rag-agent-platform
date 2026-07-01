package com.example.airagagentplatform.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

/**
 * 验证聊天接口的正常响应和参数校验行为。
 * Verifies successful chat responses and request validation behavior.
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
}
