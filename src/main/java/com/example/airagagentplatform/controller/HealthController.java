package com.example.airagagentplatform.controller;

import com.example.airagagentplatform.dto.HealthResponse;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提供轻量级健康检查，用于确认应用已经正常启动。
 * Provides a lightweight health check to confirm that the application is running.
 */
@RestController
@RequestMapping("/api")
public class HealthController {

    private final String applicationName;

    public HealthController(@Value("${spring.application.name}") String applicationName) {
        this.applicationName = applicationName;
    }

    /**
     * 返回应用状态、应用名称和当前时间。
     * Returns the application status, name, and current time.
     *
     * @return current health information / 当前健康信息
     */
    @GetMapping("/health")
    public HealthResponse health() {
        return new HealthResponse("UP", applicationName, Instant.now());
    }
}
