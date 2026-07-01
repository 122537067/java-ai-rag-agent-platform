package com.example.airagagentplatform;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 验证 Spring 应用上下文能够完整启动。
 * Verifies that the complete Spring application context can start.
 */
@SpringBootTest(properties = "spring.ai.openai.api-key=test-api-key")
class AiRagAgentPlatformApplicationTests {

    /**
     * 上下文加载失败时，本测试会直接失败。
     * This test fails automatically if the application context cannot load.
     */
    @Test
    void contextLoads() {
    }
}
