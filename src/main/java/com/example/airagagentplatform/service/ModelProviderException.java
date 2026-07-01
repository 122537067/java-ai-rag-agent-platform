package com.example.airagagentplatform.service;

/**
 * 表示模型提供者连接失败或返回了无效结果。
 * Indicates that the model provider failed or returned an invalid result.
 */
public class ModelProviderException extends RuntimeException {

    public ModelProviderException(String message) {
        super(message);
    }

    public ModelProviderException(String message, Throwable cause) {
        super(message, cause);
    }
}
