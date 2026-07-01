package com.example.airagagentplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 应用启动入口，Spring Boot 会从这个包开始扫描组件。
 * Main entry point; Spring Boot scans this package and its subpackages for components.
 *
 * 修改时间 / Last updated: 2026-07-02 01:32 (Asia/Shanghai)
 */
@SpringBootApplication
public class AiRagAgentPlatformApplication {

    /**
     * 启动内嵌 Web 服务器和 Spring 应用上下文。
     * Starts the embedded web server and the Spring application context.
     *
     * @param args optional command-line startup arguments passed to Spring Boot /
     *             传给 Spring Boot 的可选命令行启动参数
     */
    public static void main(String[] args) {
        SpringApplication.run(AiRagAgentPlatformApplication.class, args);
    }
}
