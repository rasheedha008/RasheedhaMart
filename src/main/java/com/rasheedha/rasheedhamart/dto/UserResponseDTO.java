package com.rasheedha.rasheedhamart.dto;

import com.rasheedha.rasheedhamart.model.User;
import java.sql.Timestamp;

public class UserResponseDTO {
    private Long id;
    private String email;
    private String fullName;
    private String role;
    private Timestamp createdAt;

    public UserResponseDTO() {}

    public UserResponseDTO(Long id, String email, String fullName, String role, Timestamp createdAt) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
        this.createdAt = createdAt;
    }

    public static UserResponseDTO fromUser(User user) {
        if (user == null) return null;
        return new UserResponseDTO(user.getId(), user.getEmail(), user.getFullName(), user.getRole(), user.getCreatedAt());
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
