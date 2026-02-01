package com.pgps.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.pgps.model.EscalationRecord;
import com.pgps.util.DBConnection;

public class EscalationDAOImpl implements EscalationDAO {

    @Override
    public void save(EscalationRecord record) {

        String sql = """
            INSERT INTO escalation_records
            (grievance_id, from_authority, to_authority, reason, escalated_at)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, record.getGrievanceId());
            ps.setInt(2, record.getFromAuthorityId());
            ps.setInt(3, record.getToAuthorityId());
            ps.setString(4, record.getReason());
            ps.setString(5, record.getEscalatedAt().toString());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<EscalationRecord> findByGrievanceId(int grievanceId) {

        List<EscalationRecord> list = new ArrayList<>();

        String sql = """
            SELECT escalation_id, grievance_id, from_authority, to_authority,
                reason, escalated_at
            FROM escalation_records
            WHERE grievance_id = ?
            ORDER BY escalated_at ASC
        """;

        try (Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, grievanceId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new EscalationRecord(
                        rs.getInt("escalation_id"),
                        rs.getInt("grievance_id"),
                        rs.getInt("from_authority"),
                        rs.getInt("to_authority"),
                        rs.getString("reason"),
                        rs.getTimestamp("escalated_at").toLocalDateTime()
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}