package com.pgps.dao;

import com.pgps.enums.GrievanceCategory;
import com.pgps.enums.GrievanceStatus;
import com.pgps.enums.SeverityLevel;
import com.pgps.model.Grievance;
import com.pgps.util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GrievanceDAOImpl implements GrievanceDAO {
    

    // ===================== ESCALATION UPDATE =====================
    @Override
    public void updateEscalation(int grievanceId,
                                 GrievanceStatus status,
                                 int newAuthorityId) {

        String sql = """
            UPDATE grievances
            SET status = ?, assigned_authority = ?, updated_at = ?
            WHERE grievance_id = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status.name());   // enum → String
            ps.setInt(2, newAuthorityId);
            ps.setString(3, LocalDateTime.now().toString());
            ps.setInt(4, grievanceId);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===================== INSERT =====================
    @Override
    public void save(Grievance g) {

        String sql = """
            INSERT INTO grievances
            (title, description, category, severity, status,
             priority_score, raised_by, assigned_authority,
             created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            LocalDateTime now = LocalDateTime.now();

            ps.setString(1, g.getTitle());
            ps.setString(2, g.getDescription());
            ps.setString(3, g.getCategory().name());
            ps.setString(4, g.getSeverity().name());
            ps.setString(5, g.getStatus().name());
            ps.setInt(6, g.getPriorityScore());
            ps.setInt(7, g.getRaisedByUserId());
            ps.setInt(8, g.getAssignedAuthorityId());
            ps.setString(9, now.toString());
            ps.setString(10, now.toString());

            g.setCreatedAt(now);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===================== FIND BY ID =====================
    @Override
    public Grievance findById(int id) {

        String sql = "SELECT * FROM grievances WHERE grievance_id = ?";
        Grievance g = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                g = mapResultSetToGrievance(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return g;
    }

    // ===================== FIND ALL =====================
    @Override
    public List<Grievance> findAll() {

        List<Grievance> grievances = new ArrayList<>();
        String sql = "SELECT * FROM grievances";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                grievances.add(mapResultSetToGrievance(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return grievances;
    }

    // ===================== UPDATE STATUS =====================
    @Override
    public void updateStatus(int grievanceId, String status) {

        String sql = "UPDATE grievances SET status = ?, updated_at = ? WHERE grievance_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setString(2, LocalDateTime.now().toString());
            ps.setInt(3, grievanceId);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Grievance> findByAuthorityId(int authorityId) {

        List<Grievance> list = new ArrayList<>();

        String sql = """
            SELECT * FROM grievances
            WHERE assigned_authority = ?
            ORDER BY priority_score DESC
        """;

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, authorityId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapRow(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ===================== MAPPER (CLEAN DESIGN) =====================
    private Grievance mapResultSetToGrievance(ResultSet rs) throws SQLException {

        Grievance g = new Grievance();

        g.setGrievanceId(rs.getInt("grievance_id"));
        g.setTitle(rs.getString("title"));
        g.setDescription(rs.getString("description"));
        g.setCategory(GrievanceCategory.valueOf(rs.getString("category")));
        g.setSeverity(SeverityLevel.valueOf(rs.getString("severity")));
        g.setStatus(GrievanceStatus.valueOf(rs.getString("status")));
        g.setPriorityScore(rs.getInt("priority_score"));
        g.setRaisedByUserId(rs.getInt("raised_by"));
        g.setAssignedAuthorityId(rs.getInt("assigned_authority"));
        g.setCreatedAt(LocalDateTime.parse(rs.getString("created_at")));
        g.setUpdatedAt(LocalDateTime.parse(rs.getString("updated_at")));


        return g;
    }

    private Grievance mapRow(ResultSet rs) throws SQLException {

        Grievance g = new Grievance();

        g.setGrievanceId(rs.getInt("grievance_id"));
        g.setTitle(rs.getString("title"));
        g.setDescription(rs.getString("description"));

        g.setCategory(
                GrievanceCategory.valueOf(rs.getString("category"))
        );

        g.setSeverity(
                SeverityLevel.valueOf(rs.getString("severity"))
        );

        g.setStatus(
                GrievanceStatus.valueOf(rs.getString("status"))
        );

        g.setPriorityScore(rs.getInt("priority_score"));
        g.setRaisedByUserId(rs.getInt("raised_by"));
        g.setAssignedAuthorityId(rs.getInt("assigned_authority"));

        if (rs.getString("created_at") != null) {
            g.setCreatedAt(
                    java.time.LocalDateTime.parse(rs.getString("created_at"))
            );
        }

        if (rs.getString("updated_at") != null) {
            g.setUpdatedAt(
                    java.time.LocalDateTime.parse(rs.getString("updated_at"))
            );
        }

        return g;
    }
    
    @Override
    public List<Grievance> findByAuthority(int authorityId) {

        List<Grievance> list = new ArrayList<>();

        String sql = """
            SELECT * FROM grievances
            WHERE assigned_authority = ?
            ORDER BY priority_score DESC
        """;

        try (Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, authorityId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    
}
