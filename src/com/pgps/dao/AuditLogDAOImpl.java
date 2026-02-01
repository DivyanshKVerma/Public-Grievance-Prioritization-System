package com.pgps.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.pgps.model.AuditLog;
import com.pgps.util.DBConnection;

public class AuditLogDAOImpl implements AuditLogDAO {

    @Override
    public void save(AuditLog log) {

        String sql = """
            INSERT INTO audit_logs
            (grievance_id, action, performed_by, timestamp)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, log.getGrievanceId());
            ps.setString(2, log.getAction());
            ps.setString(3, log.getPerformedBy());
            ps.setString(4, log.getTimestamp().toString());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public List<AuditLog> findByGrievanceId(int grievanceId) {

        List<AuditLog> logs = new ArrayList<>();

        String sql = """
            SELECT * FROM audit_logs
            WHERE grievance_id = ?
            ORDER BY timestamp DESC
        """;

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, grievanceId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                AuditLog log = new AuditLog(
                        rs.getInt("log_id"),
                        rs.getInt("grievance_id"),
                        rs.getString("action"),
                        rs.getString("performed_by"),
                        LocalDateTime.parse(rs.getString("timestamp"))
                );
                logs.add(log);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return logs;
    }
}