package com.rasheedha.rasheedhamart.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rasheedha.rasheedhamart.chatbot.ChatProvider;
import com.rasheedha.rasheedhamart.chatbot.GeminiChatProvider;
import com.rasheedha.rasheedhamart.chatbot.MockChatProvider;
import com.rasheedha.rasheedhamart.dto.ApiResponse;
import com.rasheedha.rasheedhamart.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServlet extends HttpServlet {

    private final ChatProvider chatProvider;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, RateLimitTracker> rateLimitMap = new ConcurrentHashMap<>();

    public ChatServlet() {
        String providerType = System.getenv("AI_CHATBOT_PROVIDER");
        if ("gemini".equalsIgnoreCase(providerType)) {
            String apiKey = System.getenv("AI_API_KEY");
            this.chatProvider = new GeminiChatProvider(apiKey);
        } else {
            this.chatProvider = new MockChatProvider();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        HttpSession session = req.getSession(true);
        String sessionId = session.getId();

        // 1. Enforce Rate Limiter (10 messages per minute per session)
        RateLimitTracker tracker = rateLimitMap.computeIfAbsent(sessionId, k -> new RateLimitTracker());
        if (!tracker.allowRequest()) {
            resp.setStatus(429); // 429 Too Many Requests
            resp.getWriter().write(objectMapper.writeValueAsString(
                ApiResponse.error("Rate limit exceeded. Maximum 10 messages per minute permitted.")
            ));
            return;
        }

        try {
            Map<String, Object> body = objectMapper.readValue(req.getInputStream(), Map.class);
            String message = body.get("message") != null ? body.get("message").toString() : "";

            // 2. Validate input length cap (500 characters max)
            if (message.length() > 500) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write(objectMapper.writeValueAsString(
                    ApiResponse.error("Message exceeds maximum length of 500 characters.")
                ));
                return;
            }

            User user = (User) session.getAttribute("user");
            String role = (user != null) ? user.getRole() : "GUEST";

            // 3. Get reply from Provider
            String reply = chatProvider.getResponse(message, role);

            Map<String, String> responseData = new HashMap<>();
            responseData.put("reply", reply);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(objectMapper.writeValueAsString(ApiResponse.success(responseData)));

        } catch (Exception e) {
            // Static fallback degraded response on unexpected failure
            Map<String, String> fallbackData = new HashMap<>();
            fallbackData.put("reply", "Welcome to RasheedhaMart! How can I assist you with your orders, products, or cart today?");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(objectMapper.writeValueAsString(ApiResponse.success(fallbackData)));
        }
    }

    private static class RateLimitTracker {
        private int count = 0;
        private long resetTime = System.currentTimeMillis() + 60000;

        public synchronized boolean allowRequest() {
            long now = System.currentTimeMillis();
            if (now > resetTime) {
                count = 0;
                resetTime = now + 60000;
            }
            if (count < 10) {
                count++;
                return true;
            }
            return false;
        }
    }
}
