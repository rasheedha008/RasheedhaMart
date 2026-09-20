package com.rasheedha.rasheedhamart.dao;

import com.rasheedha.rasheedhamart.model.Review;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAOImpl implements ReviewDAO {

    @Override
    public Review create(Review review) {
        String sql = "INSERT INTO reviews (product_id, buyer_id, rating, comment) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setLong(1, review.getProductId());
            pstmt.setLong(2, review.getBuyerId());
            pstmt.setInt(3, review.getRating());
            pstmt.setString(4, review.getComment());

            int affected = pstmt.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        review.setId(rs.getLong(1));
                    }
                }
            }
            return review;
        } catch (SQLException e) {
            throw new RuntimeException("Error creating review: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Review> findByProductId(Long productId) {
        List<Review> list = new ArrayList<>();
        String sql = "SELECT r.id, r.product_id, r.buyer_id, r.rating, r.comment, r.created_at, u.full_name " +
                     "FROM reviews r JOIN users u ON r.buyer_id = u.id WHERE r.product_id = ? ORDER BY r.id DESC";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, productId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Review review = new Review(
                        rs.getLong("id"),
                        rs.getLong("product_id"),
                        rs.getLong("buyer_id"),
                        rs.getInt("rating"),
                        rs.getString("comment"),
                        rs.getTimestamp("created_at")
                    );
                    review.setBuyerName(rs.getString("full_name"));
                    list.add(review);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching reviews: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public double getAverageRating(Long productId) {
        String sql = "SELECT AVG(rating) FROM reviews WHERE product_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, productId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error calculating average rating: " + e.getMessage(), e);
        }
        return 0.0;
    }

    @Override
    public boolean hasPurchasedProduct(Long buyerId, Long productId) {
        String sql = "SELECT COUNT(*) FROM orders o JOIN order_items oi ON o.id = oi.order_id WHERE o.buyer_id = ? AND oi.product_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, buyerId);
            pstmt.setLong(2, productId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking purchase history: " + e.getMessage(), e);
        }
        return false;
    }
}
