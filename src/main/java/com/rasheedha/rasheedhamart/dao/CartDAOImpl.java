package com.rasheedha.rasheedhamart.dao;

import com.rasheedha.rasheedhamart.model.CartItem;
import com.rasheedha.rasheedhamart.model.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDAOImpl implements CartDAO {

    @Override
    public CartItem addItem(Long buyerId, Long productId, int quantity) {
        // Check if item already exists in cart
        String checkSql = "SELECT id, quantity FROM cart_items WHERE buyer_id = ? AND product_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setLong(1, buyerId);
            checkStmt.setLong(2, productId);

            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    int existingQty = rs.getInt("quantity");
                    return updateQuantity(buyerId, productId, existingQty + quantity);
                }
            }

            String insertSql = "INSERT INTO cart_items (buyer_id, product_id, quantity) VALUES (?, ?, ?)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                insertStmt.setLong(1, buyerId);
                insertStmt.setLong(2, productId);
                insertStmt.setInt(3, quantity);

                insertStmt.executeUpdate();
                try (ResultSet keys = insertStmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        CartItem item = new CartItem(keys.getLong(1), buyerId, productId, quantity, new Timestamp(System.currentTimeMillis()));
                        return item;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error adding item to cart: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public CartItem updateQuantity(Long buyerId, Long productId, int quantity) {
        if (quantity <= 0) {
            removeItem(buyerId, productId);
            return null;
        }

        String sql = "UPDATE cart_items SET quantity = ? WHERE buyer_id = ? AND product_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, quantity);
            pstmt.setLong(2, buyerId);
            pstmt.setLong(3, productId);

            pstmt.executeUpdate();
            return new CartItem(null, buyerId, productId, quantity, new Timestamp(System.currentTimeMillis()));
        } catch (SQLException e) {
            throw new RuntimeException("Error updating cart quantity: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean removeItem(Long buyerId, Long productId) {
        String sql = "DELETE FROM cart_items WHERE buyer_id = ? AND product_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, buyerId);
            pstmt.setLong(2, productId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error removing item from cart: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean clearCart(Long buyerId) {
        String sql = "DELETE FROM cart_items WHERE buyer_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, buyerId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error clearing cart: " + e.getMessage(), e);
        }
    }

    @Override
    public List<CartItem> getCartItems(Long buyerId) {
        List<CartItem> list = new ArrayList<>();
        String sql = "SELECT c.id, c.buyer_id, c.product_id, c.quantity, c.created_at, " +
                     "p.name, p.description, p.price, p.stock_quantity, p.category, p.image_url, p.seller_id " +
                     "FROM cart_items c JOIN products p ON c.product_id = p.id WHERE c.buyer_id = ? ORDER BY c.id ASC";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, buyerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    CartItem item = new CartItem(
                        rs.getLong("id"),
                        rs.getLong("buyer_id"),
                        rs.getLong("product_id"),
                        rs.getInt("quantity"),
                        rs.getTimestamp("created_at")
                    );

                    Product product = new Product(
                        rs.getLong("product_id"),
                        rs.getLong("seller_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getBigDecimal("price"),
                        rs.getInt("stock_quantity"),
                        rs.getString("category"),
                        rs.getString("image_url"),
                        null
                    );

                    item.setProduct(product);
                    list.add(item);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching cart items: " + e.getMessage(), e);
        }
        return list;
    }
}
