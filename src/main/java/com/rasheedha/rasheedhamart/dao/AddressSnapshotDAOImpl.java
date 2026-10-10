package com.rasheedha.rasheedhamart.dao;

import com.rasheedha.rasheedhamart.model.AddressSnapshot;

import java.sql.*;

public class AddressSnapshotDAOImpl implements AddressSnapshotDAO {

    @Override
    public AddressSnapshot createSnapshot(Connection conn, AddressSnapshot snapshot) throws SQLException {
        String sql = "INSERT INTO order_address_snapshots (order_id, full_name, phone_number, address_line1, address_line2, city, state, postal_code, country) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, snapshot.getOrderId());
            stmt.setString(2, snapshot.getFullName());
            stmt.setString(3, snapshot.getPhoneNumber());
            stmt.setString(4, snapshot.getAddressLine1());
            stmt.setString(5, snapshot.getAddressLine2());
            stmt.setString(6, snapshot.getCity());
            stmt.setString(7, snapshot.getState());
            stmt.setString(8, snapshot.getPostalCode());
            stmt.setString(9, snapshot.getCountry() != null ? snapshot.getCountry() : "India");
            
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    snapshot.setId(rs.getLong(1));
                }
            }
        }
        return snapshot;
    }

    @Override
    public AddressSnapshot findByOrderId(Long orderId) {
        String sql = "SELECT * FROM order_address_snapshots WHERE order_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToSnapshot(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private AddressSnapshot mapResultSetToSnapshot(ResultSet rs) throws SQLException {
        return new AddressSnapshot(
            rs.getLong("id"),
            rs.getLong("order_id"),
            rs.getString("full_name"),
            rs.getString("phone_number"),
            rs.getString("address_line1"),
            rs.getString("address_line2"),
            rs.getString("city"),
            rs.getString("state"),
            rs.getString("postal_code"),
            rs.getString("country"),
            rs.getTimestamp("created_at")
        );
    }
}
