package com.rasheedha.rasheedhamart.service;

import com.rasheedha.rasheedhamart.dao.AddressDAO;
import com.rasheedha.rasheedhamart.dao.AddressDAOImpl;
import com.rasheedha.rasheedhamart.dao.AddressSnapshotDAO;
import com.rasheedha.rasheedhamart.dao.AddressSnapshotDAOImpl;
import com.rasheedha.rasheedhamart.dao.CartDAO;
import com.rasheedha.rasheedhamart.dao.DatabaseManager;
import com.rasheedha.rasheedhamart.dao.OrderDAO;
import com.rasheedha.rasheedhamart.exception.ResourceNotFoundException;
import com.rasheedha.rasheedhamart.exception.ValidationException;
import com.rasheedha.rasheedhamart.model.Address;
import com.rasheedha.rasheedhamart.model.AddressSnapshot;
import com.rasheedha.rasheedhamart.model.CartItem;
import com.rasheedha.rasheedhamart.model.Order;
import com.rasheedha.rasheedhamart.model.OrderItem;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderService {

    private final OrderDAO orderDAO;
    private final CartDAO cartDAO;
    private final AddressDAO addressDAO;
    private final AddressSnapshotDAO snapshotDAO;

    public OrderService(OrderDAO orderDAO, CartDAO cartDAO) {
        this.orderDAO = orderDAO;
        this.cartDAO = cartDAO;
        this.addressDAO = new AddressDAOImpl();
        this.snapshotDAO = new AddressSnapshotDAOImpl();
    }

    public OrderService(OrderDAO orderDAO, CartDAO cartDAO, AddressDAO addressDAO, AddressSnapshotDAO snapshotDAO) {
        this.orderDAO = orderDAO;
        this.cartDAO = cartDAO;
        this.addressDAO = addressDAO;
        this.snapshotDAO = snapshotDAO;
    }

    public Order checkout(Long buyerId) {
        return checkout(buyerId, null);
    }

    public Order checkout(Long buyerId, Long addressId) {
        List<CartItem> cartItems = cartDAO.getCartItems(buyerId);
        if (cartItems.isEmpty()) {
            throw new ValidationException("Cannot place order: Shopping cart is empty.");
        }

        // Address Validation & Ownership Enforcement
        Address selectedAddress = null;
        if (addressId != null) {
            Optional<Address> optAddr = addressDAO.findById(addressId);
            if (optAddr.isPresent() && optAddr.get().getUserId().equals(buyerId)) {
                selectedAddress = optAddr.get();
            } else {
                throw new ValidationException("Invalid delivery address selected.");
            }
        } else {
            List<Address> userAddrs = addressDAO.findByUserId(buyerId);
            selectedAddress = userAddrs.stream().filter(Address::isDefault).findFirst()
                .orElse(userAddrs.isEmpty() ? null : userAddrs.get(0));
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

        Order order = new Order(
            null,
            buyerId,
            selectedAddress != null ? selectedAddress.getId() : null,
            totalAmount,
            "PENDING",
            null
        );

        // Execute Order Creation + Immutable Snapshot in Single Database Transaction
        try (Connection conn = DatabaseManager.getConnection()) {
            conn.setAutoCommit(false);
            try {
                Order createdOrder = orderDAO.createOrderTransactional(conn, order, orderItems);
                
                if (selectedAddress != null) {
                    AddressSnapshot snapshot = AddressSnapshot.fromAddress(createdOrder.getId(), selectedAddress);
                    snapshotDAO.createSnapshot(conn, snapshot);
                    createdOrder.setShippingAddressSnapshot(snapshot);
                }

                cartDAO.clearCart(buyerId);
                conn.commit();
                return createdOrder;

            } catch (Exception e) {
                conn.rollback();
                throw new RuntimeException("Order creation transaction failed: " + e.getMessage(), e);
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error during order checkout transaction: " + e.getMessage(), e);
        }
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
                return false;
            default:
                return false;
        }
    }
}
