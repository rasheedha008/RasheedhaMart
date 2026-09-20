package com.rasheedha.rasheedhamart.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rasheedha.rasheedhamart.dao.DatabaseManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class HealthServlet extends HttpServlet {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        Map<String, String> healthStatus = new HashMap<>();
        healthStatus.put("status", "UP");

        try (Connection conn = DatabaseManager.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                healthStatus.put("db", "UP");
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                healthStatus.put("db", "DOWN");
                resp.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            }
        } catch (Exception e) {
            healthStatus.put("db", "DOWN");
            resp.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        }

        resp.getWriter().write(objectMapper.writeValueAsString(healthStatus));
    }
}
