package com.pgps.dao;

import java.util.List;

import com.pgps.model.AuditLog;

public interface AuditLogDAO {
    List<AuditLog> findByGrievanceId(int grievanceId);
    void save(AuditLog log);
}