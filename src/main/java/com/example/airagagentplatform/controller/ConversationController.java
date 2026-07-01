package com.example.airagagentplatform.controller;

import com.example.airagagentplatform.dto.ConversationListResponse;
import com.example.airagagentplatform.dto.ConversationMemoryResponse;
import com.example.airagagentplatform.dto.ConversationResponse;
import com.example.airagagentplatform.service.ConversationService;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理短期会话记忆，便于客户端创建、查看和清空 conversationId。
 * Manages short-term conversation memory so clients can create, inspect, and clear ids.
 *
 * 修改时间 / Last updated: 2026-07-02 01:32 (Asia/Shanghai)
 */
@Validated
@RestController
@RequestMapping("/api/conversations")
public class ConversationController {

    private final ConversationService conversationService;

    /**
     * 创建会话管理控制器，依赖由 Spring 自动注入。
     * Creates the conversation controller with a Spring-injected service.
     *
     * @param conversationService service used to create, inspect, list, and clear conversations /
     *                            用于创建、查看、列出和清空会话的服务
     */
    public ConversationController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    /**
     * 创建一个新的空会话。
     * Creates a new empty conversation.
     *
     * @return new conversation id / 新会话 ID
     */
    @PostMapping
    public ConversationResponse createConversation() {
        return conversationService.createConversation();
    }

    /**
     * 列出当前内存中的会话。
     * Lists conversations currently kept in memory.
     *
     * @return conversation list / 会话列表
     */
    @GetMapping
    public ConversationListResponse listConversations() {
        return conversationService.listConversations();
    }

    /**
     * 查看指定会话当前保留的消息窗口。
     * Reads the retained message window for the specified conversation.
     *
     * @param conversationId conversation id / 会话 ID
     * @return retained memory / 已保留记忆
     */
    @GetMapping("/{conversationId}")
    public ConversationMemoryResponse getConversation(
            @PathVariable
            @Size(max = 80, message = "conversationId must be at most 80 characters")
            @Pattern(
                    regexp = "^[A-Za-z0-9][A-Za-z0-9._:-]{0,79}$",
                    message = "conversationId may contain only letters, numbers, dot, underscore, colon, and dash")
            String conversationId) {
        return conversationService.getConversation(conversationId);
    }

    /**
     * 清空指定会话。
     * Clears the specified conversation.
     *
     * @param conversationId conversation id / 会话 ID
     */
    @DeleteMapping("/{conversationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearConversation(
            @PathVariable
            @Size(max = 80, message = "conversationId must be at most 80 characters")
            @Pattern(
                    regexp = "^[A-Za-z0-9][A-Za-z0-9._:-]{0,79}$",
                    message = "conversationId may contain only letters, numbers, dot, underscore, colon, and dash")
            String conversationId) {
        conversationService.clearConversation(conversationId);
    }
}
