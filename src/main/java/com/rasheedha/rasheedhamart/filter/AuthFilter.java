package com.rasheedha.rasheedhamart.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rasheedha.rasheedhamart.dto.ApiResponse;
import com.rasheedha.rasheedhamart.model.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthFilter implements Filter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getRequestURI();

        // Public routes bypass auth filter check
        if (path.startsWith("/api/v1/auth") || path.startsWith("/api/v1/health") || path.equals("/") || path.endsWith(".html") || path.endsWith(".js") || path.endsWith(".css")) {
            chain.doFilter(request, response);
            return;
        }

        // GET products search/browse is public
        if (path.startsWith("/api/v1/products") && "GET".equalsIgnoreCase(httpRequest.getMethod()) && !path.contains("/seller")) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = httpRequest.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            sendError(httpResponse, HttpServletResponse.SC_UNAUTHORIZED, "Authentication required. Please log in.");
            return;
        }

        // Role-based protection check for admin routes
        if (path.startsWith("/api/v1/admin") && !"ADMIN".equalsIgnoreCase(user.getRole())) {
            sendError(httpResponse, HttpServletResponse.SC_FORBIDDEN, "Access denied. Admin role required.");
            return;
        }

        // Role-based protection check for seller routes
        if (path.contains("/seller") && !"SELLER".equalsIgnoreCase(user.getRole()) && !"ADMIN".equalsIgnoreCase(user.getRole())) {
            sendError(httpResponse, HttpServletResponse.SC_FORBIDDEN, "Access denied. Seller role required.");
            return;
        }

        chain.doFilter(request, response);
    }

    private void sendError(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(ApiResponse.error(message)));
    }

    @Override
    public void destroy() {}
}
