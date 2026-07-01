package com.example.airagagentplatform.service;

/**
 * 表示模型提供者连接失败或返回了无效结果。
 * Indicates that the model provider failed or returned an invalid result.
 *
 * 修改时间 / Last updated: 2026-07-02 01:32 (Asia/Shanghai)
 */
public class ModelProviderException extends RuntimeException {

    /**
     * 创建只包含安全错误说明的模型异常。
     * Creates a model exception with a safe error description only.
     *
     * @param message safe message that may be returned to the API caller /
     *                可返回给 API 调用方的安全错误说明
     */
    public ModelProviderException(String message) {
        super(message);
    }

    /**
     * 创建模型异常并保留原始异常链，便于服务端排查问题。
     * Creates a model exception while preserving the original cause for diagnostics.
     *
     * @param message safe high-level description of the provider failure /
     *                模型提供者失败的安全概括
     * @param cause original runtime exception; retained internally and not exposed directly /
     *              原始运行时异常，仅在内部保留，不直接暴露
     */
    public ModelProviderException(String message, Throwable cause) {
        super(message, cause);
    }
}
