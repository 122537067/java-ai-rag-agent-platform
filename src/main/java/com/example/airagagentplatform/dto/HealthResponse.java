package com.example.airagagentplatform.dto;

import java.time.Instant;

/**
 * 健康检查接口的响应数据。
 * Response data returned by the health endpoint.
 *
 * @param status application status / 应用状态
 * @param application application name / 应用名称
 * @param timestamp response generation time / 响应生成时间
 */
public record HealthResponse(
        String status,
        String application,
        Instant timestamp
) {
}
