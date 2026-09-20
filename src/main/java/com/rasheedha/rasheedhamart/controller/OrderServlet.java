package com.rasheedha.rasheedhamart.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rasheedha.rasheedhamart.dto.ApiResponse;
import com.rasheedha.rasheedhamart.exception.ResourceNotFoundException;
import com.rasheedha.rasheedhamart.exception.ValidationException;
import com.rasheedha.rasheedhamart.model.Order;
import com.rasheedha.rasheedhamart.model.User;
import com.rasheedha.rasheedhamart.service.OrderService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class OrderServlet extends HttpServlet {

    private final OrderService orderService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public OrderServlet(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String pathInfo = req.getPathInfo();

        try {
            User user = getAuthenticatedUser(req);

            if ("/seller".equals(pathInfo)) {
                List<Order> sellerOrders = orderService.getSellerOrders(user.getId());
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(objectMapper.writeValueAsString(ApiResponse.success(sellerOrders)));
            } else {
                List<Order> buyerOrders = orderService.getBuyerOrders(user.getId());
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(objectMapper.writeValueAsString(ApiResponse.success(buyerOrders)));
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(objectMapper.writeValueAsString(ApiResponse.error(e.getMessage())));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try {
            User user = getAuthenticatedUser(req);
            Map<String, Object> body = objectMapper.readValue(req.getInputStream(), Map.class);
            Long orderId = Long.parseLong(body.get("orderId").toString());
            String newStatus = body.get("status").toString();

            Order updated = orderService.updateOrderStatus(orderId, newStatus);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(objectMapper.writeValueAsString(ApiResponse.success(updated)));
        } catch (ValidationException | ResourceNotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(objectMapper.writeValueAsString(ApiResponse.error(e.getMessage())));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(objectMapper.writeValueAsString(ApiResponse.error("Internal Server Error: " + e.getMessage())));
        }
    }

    private User getAuthenticatedUser(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            throw new ValidationException("User session expired or not authenticated.");
        }
        return (User) session.getAttribute("user");
    }
}
