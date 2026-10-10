package com.rasheedha.rasheedhamart.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rasheedha.rasheedhamart.dto.ApiResponse;
import com.rasheedha.rasheedhamart.dto.UserResponseDTO;
import com.rasheedha.rasheedhamart.exception.AuthenticationException;
import com.rasheedha.rasheedhamart.exception.ValidationException;
import com.rasheedha.rasheedhamart.model.User;
import com.rasheedha.rasheedhamart.service.AuthService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

public class AuthServlet extends HttpServlet {

    private final AuthService authService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AuthServlet(AuthService authService) {
        this.authService = authService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String pathInfo = req.getPathInfo();

        try {
            if ("/register".equals(pathInfo)) {
                Map<String, String> body = objectMapper.readValue(req.getInputStream(), Map.class);
                UserResponseDTO registered = authService.register(
                    body.get("email"),
                    body.get("password"),
                    body.get("fullName"),
                    body.get("role")
                );
                resp.setStatus(HttpServletResponse.SC_CREATED);
                resp.getWriter().write(objectMapper.writeValueAsString(ApiResponse.success(registered)));

            } else if ("/login".equals(pathInfo)) {
                Map<String, String> body = objectMapper.readValue(req.getInputStream(), Map.class);
                User user = authService.login(body.get("email"), body.get("password"));

                // Invalidate old session and regenerate new session ID for security
                HttpSession oldSession = req.getSession(false);
                if (oldSession != null) {
                    oldSession.invalidate();
                }
                HttpSession newSession = req.getSession(true);
                newSession.setMaxInactiveInterval(1800); // 30 mins session timeout
                newSession.setAttribute("user", user);

                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(objectMapper.writeValueAsString(ApiResponse.success(UserResponseDTO.fromUser(user))));

            } else if ("/admin/login".equals(pathInfo) || "/admin-login".equals(pathInfo)) {
                Map<String, String> body = objectMapper.readValue(req.getInputStream(), Map.class);
                User user = authService.adminLogin(body.get("email"), body.get("password"));

                HttpSession oldSession = req.getSession(false);
                if (oldSession != null) {
                    oldSession.invalidate();
                }
                HttpSession newSession = req.getSession(true);
                newSession.setMaxInactiveInterval(1800);
                newSession.setAttribute("user", user);

                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(objectMapper.writeValueAsString(ApiResponse.success(UserResponseDTO.fromUser(user))));

            } else if ("/logout".equals(pathInfo)) {
                HttpSession session = req.getSession(false);
                if (session != null) {
                    session.invalidate();
                }
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(objectMapper.writeValueAsString(ApiResponse.success("Successfully logged out.")));

            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write(objectMapper.writeValueAsString(ApiResponse.error("Endpoint not found.")));
            }
        } catch (ValidationException | AuthenticationException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(objectMapper.writeValueAsString(ApiResponse.error(e.getMessage())));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(objectMapper.writeValueAsString(ApiResponse.error("Internal Server Error: " + e.getMessage())));
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String pathInfo = req.getPathInfo();

        if ("/me".equals(pathInfo)) {
            HttpSession session = req.getSession(false);
            User user = (session != null) ? (User) session.getAttribute("user") : null;
            if (user != null) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(objectMapper.writeValueAsString(ApiResponse.success(UserResponseDTO.fromUser(user))));
            } else {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resp.getWriter().write(objectMapper.writeValueAsString(ApiResponse.error("Not authenticated.")));
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write(objectMapper.writeValueAsString(ApiResponse.error("Endpoint not found.")));
        }
    }
}
