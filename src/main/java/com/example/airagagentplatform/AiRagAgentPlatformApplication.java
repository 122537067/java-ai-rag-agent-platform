package com.example.airagagentplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 应用启动入口，Spring Boot 会从这个包开始扫描组件。
 * Main entry point; Spring Boot scans this package and its subpackages for components.
 */
@SpringBootApplication
public class AiRagAgentPlatformApplication {

    /**
     * 启动内嵌 Web 服务器和 Spring 应用上下文。
     * Starts the embedded web server and the Spring application context.
     *
     * @param args command-line arguments / 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(AiRagAgentPlatformApplication.class, args);
    }
}
