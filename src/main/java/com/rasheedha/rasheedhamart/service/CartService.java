package com.rasheedha.rasheedhamart.service;

import com.rasheedha.rasheedhamart.dao.CartDAO;
import com.rasheedha.rasheedhamart.dao.ProductDAO;
import com.rasheedha.rasheedhamart.exception.ResourceNotFoundException;
import com.rasheedha.rasheedhamart.exception.ValidationException;
import com.rasheedha.rasheedhamart.model.CartItem;
import com.rasheedha.rasheedhamart.model.Product;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartService {

    private final CartDAO cartDAO;
    private final ProductDAO productDAO;

    public CartService(CartDAO cartDAO, ProductDAO productDAO) {
        this.cartDAO = cartDAO;
        this.productDAO = productDAO;
    }

    public CartItem addToCart(Long buyerId, Long productId, int quantity) {
        if (quantity <= 0) {
            throw new ValidationException("Quantity must be greater than zero.");
        }

        Product product = productDAO.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));

        if (product.getStockQuantity() < quantity) {
            throw new ValidationException("Only " + product.getStockQuantity() + " units available in stock.");
        }

        return cartDAO.addItem(buyerId, productId, quantity);
    }

    public CartItem updateCartQuantity(Long buyerId, Long productId, int quantity) {
        if (quantity <= 0) {
            cartDAO.removeItem(buyerId, productId);
            return null;
        }

        Product product = productDAO.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));

        if (product.getStockQuantity() < quantity) {
            throw new ValidationException("Only " + product.getStockQuantity() + " units available in stock.");
        }

        return cartDAO.updateQuantity(buyerId, productId, quantity);
    }

    public boolean removeFromCart(Long buyerId, Long productId) {
        return cartDAO.removeItem(buyerId, productId);
    }

    public Map<String, Object> getCartSummary(Long buyerId) {
        List<CartItem> items = cartDAO.getCartItems(buyerId);
        BigDecimal runningTotal = BigDecimal.ZERO;

        for (CartItem item : items) {
            if (item.getProduct() != null) {
                BigDecimal itemTotal = item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                runningTotal = runningTotal.add(itemTotal);
            }
        }

        Map<String, Object> summary = new HashMap<>();
        summary.put("items", items);
        summary.put("itemCount", items.size());
        summary.put("runningTotal", runningTotal);
        return summary;
    }
}
