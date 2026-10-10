package com.rasheedha.rasheedhamart.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rasheedha.rasheedhamart.dto.ApiResponse;
import com.rasheedha.rasheedhamart.service.AnalyticsService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class AdminAnalyticsServlet extends HttpServlet {

    private final AnalyticsService analyticsService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AdminAnalyticsServlet(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try {
            Map<String, Object> metrics = analyticsService.getAdminAnalyticsSummary();
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(objectMapper.writeValueAsString(ApiResponse.success(metrics)));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(objectMapper.writeValueAsString(ApiResponse.error(e.getMessage())));
        }
    }
}
