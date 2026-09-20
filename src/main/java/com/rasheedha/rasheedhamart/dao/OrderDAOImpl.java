package com.rasheedha.rasheedhamart.dao;

import com.rasheedha.rasheedhamart.model.Order;
import com.rasheedha.rasheedhamart.model.OrderItem;
import com.rasheedha.rasheedhamart.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDAOImpl implements OrderDAO {

    @Override
    public Order createOrder(Order order, List<OrderItem> items) {
        Connection conn = null;
        try {
            conn = DatabaseManager.getConnection();
            conn.setAutoCommit(false); // Begin transaction

            String orderSql = "INSERT INTO orders (buyer_id, total_amount, status) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setLong(1, order.getBuyerId());
                pstmt.setBigDecimal(2, order.getTotalAmount());
                pstmt.setString(3, order.getStatus() != null ? order.getStatus() : "PENDING");

                pstmt.executeUpdate();
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        order.setId(rs.getLong(1));
                    }
                }
            }

            String itemSql = "INSERT INTO order_items (order_id, product_id, seller_id, quantity, unit_price) VALUES (?, ?, ?, ?, ?)";
            String updateStockSql = "UPDATE products SET stock_quantity = stock_quantity - ? WHERE id = ? AND stock_quantity >= ?";

            try (PreparedStatement itemStmt = conn.prepareStatement(itemSql);
                 PreparedStatement stockStmt = conn.prepareStatement(updateStockSql)) {

                for (OrderItem item : items) {
                    item.setOrderId(order.getId());
                    itemStmt.setLong(1, order.getId());
                    itemStmt.setLong(2, item.getProductId());
                    itemStmt.setLong(3, item.getSellerId());
                    itemStmt.setInt(4, item.getQuantity());
                    itemStmt.setBigDecimal(5, item.getUnitPrice());
                    itemStmt.addBatch();

                    // Deduct stock quantity atomically
                    stockStmt.setInt(1, item.getQuantity());
                    stockStmt.setLong(2, item.getProductId());
                    stockStmt.setInt(3, item.getQuantity());
                    int updated = stockStmt.executeUpdate();
                    if (updated == 0) {
                        throw new SQLException("Insufficient stock for product ID: " + item.getProductId());
                    }
                }
                itemStmt.executeBatch();
            }

            conn.commit(); // Commit transaction
            order.setItems(items);
            return order;
        } catch (Exception e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { /* ignore */ }
            }
            throw new RuntimeException("Transaction failed while creating order: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ex) { /* ignore */ }
            }
        }
    }

    @Override
    public Optional<Order> findById(Long id) {
        String sql = "SELECT id, buyer_id, total_amount, status, created_at FROM orders WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Order order = mapResultSetToOrder(rs);
                    order.setItems(fetchOrderItems(order.getId()));
                    return Optional.of(order);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding order by ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Order> findByBuyerId(Long buyerId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT id, buyer_id, total_amount, status, created_at FROM orders WHERE buyer_id = ? ORDER BY id DESC";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, buyerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Order order = mapResultSetToOrder(rs);
                    order.setItems(fetchOrderItems(order.getId()));
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching buyer orders: " + e.getMessage(), e);
        }
        return orders;
    }

    @Override
    public List<Order> findBySellerId(Long sellerId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT DISTINCT o.id, o.buyer_id, o.total_amount, o.status, o.created_at " +
                     "FROM orders o JOIN order_items oi ON o.id = oi.order_id WHERE oi.seller_id = ? ORDER BY o.id DESC";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, sellerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Order order = mapResultSetToOrder(rs);
                    order.setItems(fetchOrderItemsForSeller(order.getId(), sellerId));
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching seller incoming orders: " + e.getMessage(), e);
        }
        return orders;
    }

    @Override
    public List<Order> findAll() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT id, buyer_id, total_amount, status, created_at FROM orders ORDER BY id DESC";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Order order = mapResultSetToOrder(rs);
                order.setItems(fetchOrderItems(order.getId()));
                orders.add(order);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all orders: " + e.getMessage(), e);
        }
        return orders;
    }

    @Override
    public boolean updateStatus(Long orderId, String newStatus) {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newStatus);
            pstmt.setLong(2, orderId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating order status: " + e.getMessage(), e);
        }
    }

    private List<OrderItem> fetchOrderItems(Long orderId) {
        List<OrderItem> items = new ArrayList<>();
        String sql = "SELECT oi.id, oi.order_id, oi.product_id, oi.seller_id, oi.quantity, oi.unit_price, oi.created_at, " +
                     "p.name, p.description, p.category, p.image_url " +
                     "FROM order_items oi JOIN products p ON oi.product_id = p.id WHERE oi.order_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, orderId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    OrderItem item = new OrderItem(
                        rs.getLong("id"),
                        rs.getLong("order_id"),
                        rs.getLong("product_id"),
                        rs.getLong("seller_id"),
                        rs.getInt("quantity"),
                        rs.getBigDecimal("unit_price"),
                        rs.getTimestamp("created_at")
                    );

                    Product product = new Product(
                        rs.getLong("product_id"),
                        rs.getLong("seller_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getBigDecimal("unit_price"),
                        0,
                        rs.getString("category"),
                        rs.getString("image_url"),
                        null
                    );
                    item.setProduct(product);
                    items.add(item);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching order items: " + e.getMessage(), e);
        }
        return items;
    }

    private List<OrderItem> fetchOrderItemsForSeller(Long orderId, Long sellerId) {
        List<OrderItem> items = new ArrayList<>();
        String sql = "SELECT oi.id, oi.order_id, oi.product_id, oi.seller_id, oi.quantity, oi.unit_price, oi.created_at, " +
                     "p.name, p.description, p.category, p.image_url " +
                     "FROM order_items oi JOIN products p ON oi.product_id = p.id WHERE oi.order_id = ? AND oi.seller_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, orderId);
            pstmt.setLong(2, sellerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    OrderItem item = new OrderItem(
                        rs.getLong("id"),
                        rs.getLong("order_id"),
                        rs.getLong("product_id"),
                        rs.getLong("seller_id"),
                        rs.getInt("quantity"),
                        rs.getBigDecimal("unit_price"),
                        rs.getTimestamp("created_at")
                    );

                    Product product = new Product(
                        rs.getLong("product_id"),
                        rs.getLong("seller_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getBigDecimal("unit_price"),
                        0,
                        rs.getString("category"),
                        rs.getString("image_url"),
                        null
                    );
                    item.setProduct(product);
                    items.add(item);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching seller order items: " + e.getMessage(), e);
        }
        return items;
    }

    private Order mapResultSetToOrder(ResultSet rs) throws SQLException {
        return new Order(
            rs.getLong("id"),
            rs.getLong("buyer_id"),
            rs.getBigDecimal("total_amount"),
            rs.getString("status"),
            rs.getTimestamp("created_at")
        );
    }
}
