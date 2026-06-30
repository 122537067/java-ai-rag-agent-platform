package com.example.airagagentplatform.service;

import com.example.airagagentplatform.dto.ChatRequest;
import com.example.airagagentplatform.dto.ChatResponse;
import org.springframework.stereotype.Service;

@Service
public class BasicChatService implements ChatService {

    @Override
    public ChatResponse chat(ChatRequest request) {
        return new ChatResponse("Chat endpoint received: " + request.message().trim());
    }
}
