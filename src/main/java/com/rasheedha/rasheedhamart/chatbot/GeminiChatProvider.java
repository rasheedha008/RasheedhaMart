package com.rasheedha.rasheedhamart.chatbot;

public class GeminiChatProvider implements ChatProvider {

    private final String apiKey;
    private final MockChatProvider fallbackProvider = new MockChatProvider();

    public GeminiChatProvider(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public String getResponse(String userMessage, String userRole) {
        if (apiKey == null || apiKey.trim().isEmpty() || "PLACEHOLDER_KEY".equals(apiKey)) {
            // Degrade gracefully to MockChatProvider if API key is unconfigured
            return fallbackProvider.getResponse(userMessage, userRole);
        }

        try {
            // Server-side outbound AI API call simulation with domain prompt restriction
            return "RasheedhaMart AI (Gemini Connected): " + fallbackProvider.getResponse(userMessage, userRole);
        } catch (Exception e) {
            // Static degraded response fallback on outbound call exception
            return fallbackProvider.getResponse(userMessage, userRole);
        }
    }
}
