package com.rasheedha.rasheedhamart.dao;

import com.rasheedha.rasheedhamart.model.AuditLog;
import java.util.List;

public interface AuditLogDAO {
    void logAction(Long adminId, String action, String details);
    List<AuditLog> findAll();
}
