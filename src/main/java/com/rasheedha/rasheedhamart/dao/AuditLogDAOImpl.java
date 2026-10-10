package com.rasheedha.rasheedhamart.dao;

import com.rasheedha.rasheedhamart.model.AuditLog;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuditLogDAOImpl implements AuditLogDAO {

    @Override
    public void logAction(Long adminId, String action, String details) {
        String sql = "INSERT INTO audit_logs (admin_id, action, details) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, adminId != null ? adminId : 1L);
            stmt.setString(2, action);
            stmt.setString(3, details);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Failed to write audit log: " + e.getMessage());
        }
    }

    @Override
    public List<AuditLog> findAll() {
        List<AuditLog> logs = new ArrayList<>();
        String sql = "SELECT * FROM audit_logs ORDER BY created_at DESC";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                logs.add(new AuditLog(
                    rs.getLong("id"),
                    rs.getLong("admin_id"),
                    rs.getString("action"),
                    rs.getString("details"),
                    rs.getTimestamp("created_at")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logs;
    }
}
