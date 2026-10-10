package com.rasheedha.rasheedhamart.dao;

import com.rasheedha.rasheedhamart.model.AddressSnapshot;
import java.sql.Connection;
import java.sql.SQLException;

public interface AddressSnapshotDAO {
    AddressSnapshot createSnapshot(Connection conn, AddressSnapshot snapshot) throws SQLException;
    AddressSnapshot findByOrderId(Long orderId);
}
