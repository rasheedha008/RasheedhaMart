package com.rasheedha.rasheedhamart.service;

import com.rasheedha.rasheedhamart.dao.OrderDAO;
import com.rasheedha.rasheedhamart.dao.ProductDAO;
import com.rasheedha.rasheedhamart.dao.UserDAO;
import com.rasheedha.rasheedhamart.dto.UserResponseDTO;
import com.rasheedha.rasheedhamart.exception.ResourceNotFoundException;
import com.rasheedha.rasheedhamart.model.Order;
import com.rasheedha.rasheedhamart.model.Product;

import java.util.List;
import java.util.stream.Collectors;

public class AdminService {

    private final UserDAO userDAO;
    private final ProductDAO productDAO;
    private final OrderDAO orderDAO;

    public AdminService(UserDAO userDAO, ProductDAO productDAO, OrderDAO orderDAO) {
        this.userDAO = userDAO;
        this.productDAO = productDAO;
        this.orderDAO = orderDAO;
    }

    public List<UserResponseDTO> getAllUsers() {
        return userDAO.findAll().stream()
            .map(UserResponseDTO::fromUser)
            .collect(Collectors.toList());
    }

    public List<Order> getAllOrders() {
        return orderDAO.findAll();
    }

    public boolean moderateRemoveListing(Long productId) {
        Product product = productDAO.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));
        return productDAO.delete(product.getId());
    }

    public boolean deleteUser(Long userId) {
        userDAO.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        return userDAO.delete(userId);
    }
}
