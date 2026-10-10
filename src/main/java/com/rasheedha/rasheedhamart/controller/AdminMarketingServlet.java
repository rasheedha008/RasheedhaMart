package com.rasheedha.rasheedhamart.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rasheedha.rasheedhamart.dto.ApiResponse;
import com.rasheedha.rasheedhamart.exception.ValidationException;
import com.rasheedha.rasheedhamart.model.Promotion;
import com.rasheedha.rasheedhamart.service.MarketingService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class AdminMarketingServlet extends HttpServlet {

    private final MarketingService marketingService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AdminMarketingServlet(MarketingService marketingService) {
        this.marketingService = marketingService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try {
            String pathInfo = req.getPathInfo();
            if ("/active".equals(pathInfo)) {
                List<Promotion> activePromos = marketingService.getActivePromotions();
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(objectMapper.writeValueAsString(ApiResponse.success(activePromos)));
            } else {
                List<Promotion> allPromos = marketingService.getAllPromotions();
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(objectMapper.writeValueAsString(ApiResponse.success(allPromos)));
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(objectMapper.writeValueAsString(ApiResponse.error(e.getMessage())));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try {
            Promotion promotion = objectMapper.readValue(req.getInputStream(), Promotion.class);
            Promotion created = marketingService.createPromotion(promotion);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(objectMapper.writeValueAsString(ApiResponse.success(created)));
        } catch (ValidationException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(objectMapper.writeValueAsString(ApiResponse.error(e.getMessage())));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(objectMapper.writeValueAsString(ApiResponse.error(e.getMessage())));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try {
            Long promoId = Long.parseLong(req.getPathInfo().substring(1));
            boolean deleted = marketingService.deletePromotion(promoId);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(objectMapper.writeValueAsString(ApiResponse.success(deleted)));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(objectMapper.writeValueAsString(ApiResponse.error(e.getMessage())));
        }
    }
}
