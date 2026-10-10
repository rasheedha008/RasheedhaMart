package com.rasheedha.rasheedhamart.chatbot;

public interface ChatProvider {
    String getResponse(String userMessage, String userRole);
}
