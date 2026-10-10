package com.rasheedha.rasheedhamart.model;

import java.sql.Timestamp;

public class AuditLog {
    private Long id;
    private Long adminId;
    private String action;
    private String details;
    private Timestamp createdAt;

    public AuditLog() {}

    public AuditLog(Long id, Long adminId, String action, String details, Timestamp createdAt) {
        this.id = id;
        this.adminId = adminId;
        this.action = action;
        this.details = details;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getAdminId() { return adminId; }
    public void setAdminId(Long adminId) { this.adminId = adminId; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
