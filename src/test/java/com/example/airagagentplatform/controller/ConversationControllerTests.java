package com.example.airagagentplatform.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.airagagentplatform.dto.ConversationListResponse;
import com.example.airagagentplatform.dto.ConversationMemoryResponse;
import com.example.airagagentplatform.dto.ConversationMessageResponse;
import com.example.airagagentplatform.dto.ConversationResponse;
import com.example.airagagentplatform.service.ConversationService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * 验证会话管理接口的 HTTP 行为。
 * Verifies HTTP behavior of the conversation management endpoints.
 *
 * 修改时间 / Last updated: 2026-07-02 00:41 (Asia/Shanghai)
 */
@WebMvcTest(ConversationController.class)
class ConversationControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ConversationService conversationService;

    /**
     * 创建会话应返回一个 conversationId。
     * Creating a conversation should return a conversationId.
     */
    @Test
    void createConversationReturnsId() throws Exception {
        when(conversationService.createConversation()).thenReturn(new ConversationResponse("demo-1"));

        mockMvc.perform(post("/api/conversations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.conversationId").value("demo-1"));
    }

    /**
     * 会话列表应包装成稳定的 JSON 结构。
     * The conversation list should use a stable JSON wrapper.
     */
    @Test
    void listConversationsReturnsIds() throws Exception {
        when(conversationService.listConversations())
                .thenReturn(new ConversationListResponse(List.of(new ConversationResponse("demo-1"))));

        mockMvc.perform(get("/api/conversations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.conversations[0].conversationId").value("demo-1"));
    }

    /**
     * 查看会话应返回当前保留的消息窗口。
     * Inspecting a conversation should return the retained message window.
     */
    @Test
    void getConversationReturnsMemoryWindow() throws Exception {
        when(conversationService.getConversation("demo-1"))
                .thenReturn(new ConversationMemoryResponse(
                        "demo-1",
                        1,
                        List.of(new ConversationMessageResponse("user", "hello"))));

        mockMvc.perform(get("/api/conversations/demo-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.conversationId").value("demo-1"))
                .andExpect(jsonPath("$.messageCount").value(1))
                .andExpect(jsonPath("$.messages[0].content").value("hello"));
    }

    /**
     * 清空会话成功时返回 204。
     * Clearing a conversation should return 204.
     */
    @Test
    void clearConversationReturnsNoContent() throws Exception {
        doNothing().when(conversationService).clearConversation("demo-1");

        mockMvc.perform(delete("/api/conversations/demo-1"))
                .andExpect(status().isNoContent());
    }

    /**
     * 非法 conversationId 应返回统一校验错误。
     * An invalid conversationId should return the standard validation error.
     */
    @Test
    void getConversationRejectsInvalidId() throws Exception {
        mockMvc.perform(get("/api/conversations/bad$id"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message", containsString("conversationId")));
    }
}
