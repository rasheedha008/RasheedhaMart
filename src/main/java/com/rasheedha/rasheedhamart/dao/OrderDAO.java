package com.rasheedha.rasheedhamart.dao;

import com.rasheedha.rasheedhamart.model.Order;
import com.rasheedha.rasheedhamart.model.OrderItem;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface OrderDAO {
    Order createOrder(Order order, List<OrderItem> items);
    Order createOrderTransactional(Connection conn, Order order, List<OrderItem> items) throws SQLException;
    Optional<Order> findById(Long id);
    List<Order> findByBuyerId(Long buyerId);
    List<Order> findBySellerId(Long sellerId);
    List<Order> findAll();
    boolean updateStatus(Long orderId, String newStatus);
}
