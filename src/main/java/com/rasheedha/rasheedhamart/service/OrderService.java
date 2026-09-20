package com.rasheedha.rasheedhamart.service;

import com.rasheedha.rasheedhamart.dao.CartDAO;
import com.rasheedha.rasheedhamart.dao.OrderDAO;
import com.rasheedha.rasheedhamart.exception.ResourceNotFoundException;
import com.rasheedha.rasheedhamart.exception.ValidationException;
import com.rasheedha.rasheedhamart.model.CartItem;
import com.rasheedha.rasheedhamart.model.Order;
import com.rasheedha.rasheedhamart.model.OrderItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderService {

    private final OrderDAO orderDAO;
    private final CartDAO cartDAO;

    public OrderService(OrderDAO orderDAO, CartDAO cartDAO) {
        this.orderDAO = orderDAO;
        this.cartDAO = cartDAO;
    }

    public Order checkout(Long buyerId) {
        List<CartItem> cartItems = cartDAO.getCartItems(buyerId);
        if (cartItems.isEmpty()) {
            throw new ValidationException("Cannot place order: Shopping cart is empty.");
        }

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            if (cartItem.getProduct() == null) {
                throw new ValidationException("Cart contains invalid product reference.");
            }
            if (cartItem.getProduct().getStockQuantity() < cartItem.getQuantity()) {
                throw new ValidationException("Product '" + cartItem.getProduct().getName() + "' does not have enough stock available.");
            }

            BigDecimal unitPrice = cartItem.getProduct().getPrice();
            BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            totalAmount = totalAmount.add(subtotal);

            OrderItem orderItem = new OrderItem(
                null,
                null,
                cartItem.getProductId(),
                cartItem.getProduct().getSellerId(),
                cartItem.getQuantity(),
                unitPrice,
                null
            );
            orderItems.add(orderItem);
        }

        Order order = new Order(null, buyerId, totalAmount, "PENDING", null);
        Order createdOrder = orderDAO.createOrder(order, orderItems);

        // Clear shopping cart on successful checkout
        cartDAO.clearCart(buyerId);

        return createdOrder;
    }

    public List<Order> getBuyerOrders(Long buyerId) {
        return orderDAO.findByBuyerId(buyerId);
    }

    public List<Order> getSellerOrders(Long sellerId) {
        return orderDAO.findBySellerId(sellerId);
    }

    public Order updateOrderStatus(Long orderId, String newStatus) {
        Order order = orderDAO.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

        String currentStatus = order.getStatus();
        if (!isValidStatusTransition(currentStatus, newStatus)) {
            throw new ValidationException("Invalid status transition from " + currentStatus + " to " + newStatus);
        }

        boolean updated = orderDAO.updateStatus(orderId, newStatus.toUpperCase());
        if (updated) {
            order.setStatus(newStatus.toUpperCase());
        }
        return order;
    }

    private boolean isValidStatusTransition(String current, String target) {
        if (target == null) return false;
        String t = target.toUpperCase();
        switch (current.toUpperCase()) {
            case "PENDING":
                return "CONFIRMED".equals(t);
            case "CONFIRMED":
                return "SHIPPED".equals(t);
            case "SHIPPED":
                return "DELIVERED".equals(t);
            case "DELIVERED":
                return false; // Terminal state
            default:
                return false;
        }
    }
}
