package com.rasheedha.rasheedhamart.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rasheedha.rasheedhamart.dto.ApiResponse;
import com.rasheedha.rasheedhamart.exception.ResourceNotFoundException;
import com.rasheedha.rasheedhamart.exception.ValidationException;
import com.rasheedha.rasheedhamart.model.Address;
import com.rasheedha.rasheedhamart.model.User;
import com.rasheedha.rasheedhamart.service.AddressService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class AddressServlet extends HttpServlet {

    private final AddressService addressService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AddressServlet(AddressService addressService) {
        this.addressService = addressService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try {
            User user = getAuthenticatedUser(req);
            List<Address> addresses = addressService.getUserAddresses(user.getId());
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(objectMapper.writeValueAsString(ApiResponse.success(addresses)));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(objectMapper.writeValueAsString(ApiResponse.error(e.getMessage())));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try {
            User user = getAuthenticatedUser(req);
            Address address = objectMapper.readValue(req.getInputStream(), Address.class);
            Address created = addressService.addAddress(user.getId(), address);

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
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try {
            User user = getAuthenticatedUser(req);
            String pathInfo = req.getPathInfo();
            if (pathInfo != null && pathInfo.startsWith("/default/")) {
                Long addressId = Long.parseLong(pathInfo.substring("/default/".length()));
                boolean updated = addressService.setDefaultAddress(user.getId(), addressId);
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(objectMapper.writeValueAsString(ApiResponse.success(updated)));
            } else {
                Address address = objectMapper.readValue(req.getInputStream(), Address.class);
                Address updated = addressService.updateAddress(user.getId(), address);
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(objectMapper.writeValueAsString(ApiResponse.success(updated)));
            }
        } catch (ValidationException | ResourceNotFoundException e) {
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
            User user = getAuthenticatedUser(req);
            Long addressId = Long.parseLong(req.getPathInfo().substring(1));
            boolean deleted = addressService.deleteAddress(user.getId(), addressId);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(objectMapper.writeValueAsString(ApiResponse.success(deleted)));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(objectMapper.writeValueAsString(ApiResponse.error(e.getMessage())));
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
