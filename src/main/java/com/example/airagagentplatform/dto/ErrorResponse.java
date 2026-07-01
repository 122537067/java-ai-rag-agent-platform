package com.example.airagagentplatform.dto;

/**
 * API 发生可预期错误时返回的统一结构。
 * Consistent response returned for expected API errors.
 *
 * @param code machine-readable error code / 便于程序识别的错误码
 * @param message human-readable error detail / 便于阅读的错误说明
 */
public record ErrorResponse(
        String code,
        String message
) {
}
