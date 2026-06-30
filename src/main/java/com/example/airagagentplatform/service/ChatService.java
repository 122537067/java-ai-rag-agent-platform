package com.example.airagagentplatform.service;

import com.example.airagagentplatform.dto.ChatRequest;
import com.example.airagagentplatform.dto.ChatResponse;

public interface ChatService {

    ChatResponse chat(ChatRequest request);
}
