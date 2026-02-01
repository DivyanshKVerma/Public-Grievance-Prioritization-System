package com.pgps.app;

import java.sql.Connection;
import java.sql.Statement;
import com.pgps.util.DBConnection;

public class DBInitializer 
{

    public static void main(String[] args) 
    {

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) 
             {

            String usersTable = """
                CREATE TABLE IF NOT EXISTS users (
                    user_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    email TEXT UNIQUE NOT NULL,
                    role TEXT
                );
            """;
            String escalationTable = """
                CREATE TABLE IF NOT EXISTS escalation_records (
                    escalation_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    grievance_id INTEGER,
                    from_authority INTEGER,
                    to_authority INTEGER,
                    reason TEXT,
                    escalated_at TEXT
                );
            """;

            String grievancesTable = """
                CREATE TABLE IF NOT EXISTS grievances (
                    grievance_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    title TEXT,
                    description TEXT,
                    category TEXT,
                    severity TEXT,
                    status TEXT,
                    priority_score INTEGER,
                    raised_by INTEGER,
                    assigned_authority INTEGER,
                    created_at TEXT,
                    updated_at TEXT
                );
            """;

            String auditLogTable = """
                CREATE TABLE IF NOT EXISTS audit_logs (
                    log_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    grievance_id INTEGER,
                    action TEXT,
                    performed_by TEXT,
                    timestamp TEXT
                );
            """;

            

            stmt.execute(usersTable);
            stmt.execute(grievancesTable);
            stmt.execute(escalationTable);
            stmt.execute(auditLogTable);
            System.out.println("Database tables created successfully.");

        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
}