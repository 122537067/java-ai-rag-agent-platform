package com.example.airagagentplatform.controller;

import com.example.airagagentplatform.dto.ErrorResponse;
import com.example.airagagentplatform.service.ModelProviderException;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 将控制器抛出的异常转换为统一的 API 错误响应。
 * Converts controller exceptions into a consistent API error response.
 */
@RestControllerAdvice
public class ApiExceptionHandler {

    /**
     * 汇总 DTO 字段校验错误，并返回 HTTP 400。
     * Collects DTO field validation errors and returns HTTP 400.
     *
     * @param exception validation exception / 参数校验异常
     * @return structured validation error / 结构化校验错误
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return ResponseEntity.badRequest().body(new ErrorResponse("VALIDATION_ERROR", message));
    }

    /**
     * 隐藏模型提供者的内部细节，并用 HTTP 502 表示上游服务失败。
     * Hides provider internals and uses HTTP 502 for an upstream model failure.
     *
     * @param exception model provider exception / 模型提供者异常
     * @return safe provider error / 不包含敏感细节的错误响应
     */
    @ExceptionHandler(ModelProviderException.class)
    public ResponseEntity<ErrorResponse> handleModelProvider(ModelProviderException exception) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(new ErrorResponse("MODEL_PROVIDER_ERROR", exception.getMessage()));
    }
}
