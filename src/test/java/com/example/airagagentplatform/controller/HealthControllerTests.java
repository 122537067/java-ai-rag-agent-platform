package com.example.airagagentplatform.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

/**
 * 验证健康检查接口能够报告应用已启动。
 * Verifies that the health endpoint reports a running application.
 */
@WebMvcTest(HealthController.class)
class HealthControllerTests {

    @Autowired
    private MockMvc mockMvc;

    /**
     * 健康检查应返回 HTTP 200、UP 状态和正确的应用名称。
     * Health check should return HTTP 200, UP status, and the configured application name.
     */
    @Test
    void healthReturnsUp() throws Exception {
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.application").value("java-ai-rag-agent-platform"));
    }
}
