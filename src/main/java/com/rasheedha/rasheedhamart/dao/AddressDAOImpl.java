package com.rasheedha.rasheedhamart.dao;

import com.rasheedha.rasheedhamart.model.Address;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AddressDAOImpl implements AddressDAO {

    @Override
    public Address create(Address address) {
        if (address.isDefault()) {
            clearDefaultAddress(address.getUserId());
        }

        String sql = "INSERT INTO addresses (user_id, full_name, phone_number, address_line1, address_line2, city, state, postal_code, country, is_default) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setLong(1, address.getUserId());
            pstmt.setString(2, address.getFullName());
            pstmt.setString(3, address.getPhoneNumber());
            pstmt.setString(4, address.getAddressLine1());
            pstmt.setString(5, address.getAddressLine2());
            pstmt.setString(6, address.getCity());
            pstmt.setString(7, address.getState());
            pstmt.setString(8, address.getPostalCode());
            pstmt.setString(9, address.getCountry());
            pstmt.setBoolean(10, address.isDefault());

            int affected = pstmt.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        address.setId(rs.getLong(1));
                    }
                }
            }
            return address;
        } catch (SQLException e) {
            throw new RuntimeException("Error creating address: " + e.getMessage(), e);
        }
    }

    @Override
    public Address update(Address address) {
        if (address.isDefault()) {
            clearDefaultAddress(address.getUserId());
        }

        String sql = "UPDATE addresses SET full_name = ?, phone_number = ?, address_line1 = ?, address_line2 = ?, city = ?, state = ?, postal_code = ?, country = ?, is_default = ? WHERE id = ? AND user_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, address.getFullName());
            pstmt.setString(2, address.getPhoneNumber());
            pstmt.setString(3, address.getAddressLine1());
            pstmt.setString(4, address.getAddressLine2());
            pstmt.setString(5, address.getCity());
            pstmt.setString(6, address.getState());
            pstmt.setString(7, address.getPostalCode());
            pstmt.setString(8, address.getCountry());
            pstmt.setBoolean(9, address.isDefault());
            pstmt.setLong(10, address.getId());
            pstmt.setLong(11, address.getUserId());

            pstmt.executeUpdate();
            return address;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating address: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(Long id, Long userId) {
        String sql = "DELETE FROM addresses WHERE id = ? AND user_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            pstmt.setLong(2, userId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting address: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Address> findById(Long id) {
        String sql = "SELECT id, user_id, full_name, phone_number, address_line1, address_line2, city, state, postal_code, country, is_default, created_at FROM addresses WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToAddress(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding address by ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Address> findByUserId(Long userId) {
        List<Address> list = new ArrayList<>();
        String sql = "SELECT id, user_id, full_name, phone_number, address_line1, address_line2, city, state, postal_code, country, is_default, created_at FROM addresses WHERE user_id = ? ORDER BY is_default DESC, id DESC";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToAddress(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching addresses for user: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public boolean setDefaultAddress(Long addressId, Long userId) {
        clearDefaultAddress(userId);
        String sql = "UPDATE addresses SET is_default = TRUE WHERE id = ? AND user_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, addressId);
            pstmt.setLong(2, userId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error setting default address: " + e.getMessage(), e);
        }
    }

    private void clearDefaultAddress(Long userId) {
        String sql = "UPDATE addresses SET is_default = FALSE WHERE user_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error clearing default address: " + e.getMessage(), e);
        }
    }

    private Address mapResultSetToAddress(ResultSet rs) throws SQLException {
        return new Address(
            rs.getLong("id"),
            rs.getLong("user_id"),
            rs.getString("full_name"),
            rs.getString("phone_number"),
            rs.getString("address_line1"),
            rs.getString("address_line2"),
            rs.getString("city"),
            rs.getString("state"),
            rs.getString("postal_code"),
            rs.getString("country"),
            rs.getBoolean("is_default"),
            rs.getTimestamp("created_at")
        );
    }
}
