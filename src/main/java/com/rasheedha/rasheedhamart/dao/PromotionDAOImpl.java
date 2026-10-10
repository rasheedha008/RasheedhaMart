package com.rasheedha.rasheedhamart.dao;

import com.rasheedha.rasheedhamart.model.Promotion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PromotionDAOImpl implements PromotionDAO {

    @Override
    public Promotion create(Promotion promotion) {
        String sql = "INSERT INTO promotions (title, description, banner_url, discount_percentage, target_category, is_active) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, promotion.getTitle());
            pstmt.setString(2, promotion.getDescription());
            pstmt.setString(3, promotion.getBannerUrl());
            pstmt.setInt(4, promotion.getDiscountPercentage());
            pstmt.setString(5, promotion.getTargetCategory());
            pstmt.setBoolean(6, promotion.isActive());

            int affected = pstmt.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        promotion.setId(rs.getLong(1));
                    }
                }
            }
            return promotion;
        } catch (SQLException e) {
            throw new RuntimeException("Error creating promotion: " + e.getMessage(), e);
        }
    }

    @Override
    public Promotion update(Promotion promotion) {
        String sql = "UPDATE promotions SET title = ?, description = ?, banner_url = ?, discount_percentage = ?, target_category = ?, is_active = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, promotion.getTitle());
            pstmt.setString(2, promotion.getDescription());
            pstmt.setString(3, promotion.getBannerUrl());
            pstmt.setInt(4, promotion.getDiscountPercentage());
            pstmt.setString(5, promotion.getTargetCategory());
            pstmt.setBoolean(6, promotion.isActive());
            pstmt.setLong(7, promotion.getId());

            pstmt.executeUpdate();
            return promotion;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating promotion: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(Long id) {
        String sql = "DELETE FROM promotions WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting promotion: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Promotion> findAll() {
        List<Promotion> list = new ArrayList<>();
        String sql = "SELECT id, title, description, banner_url, discount_percentage, target_category, is_active, created_at FROM promotions ORDER BY id DESC";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToPromotion(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching promotions: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public List<Promotion> findActivePromotions() {
        List<Promotion> list = new ArrayList<>();
        String sql = "SELECT id, title, description, banner_url, discount_percentage, target_category, is_active, created_at FROM promotions WHERE is_active = TRUE ORDER BY id DESC";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToPromotion(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching active promotions: " + e.getMessage(), e);
        }
        return list;
    }

    private Promotion mapResultSetToPromotion(ResultSet rs) throws SQLException {
        return new Promotion(
            rs.getLong("id"),
            rs.getString("title"),
            rs.getString("description"),
            rs.getString("banner_url"),
            rs.getInt("discount_percentage"),
            rs.getString("target_category"),
            rs.getBoolean("is_active"),
            rs.getTimestamp("created_at")
        );
    }
}
