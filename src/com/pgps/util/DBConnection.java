package com.pgps.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection 
{

    private static final String DB_URL =
        "jdbc:sqlite:/Users/divyanshkverma/Documents/Coding/Projects/PublicGrievanceSystem/db/grievance_system.db";
    static 
    {
        try 
        {
            Class.forName("org.sqlite.JDBC");
        } 
        catch (ClassNotFoundException e) 
        {
            throw new RuntimeException("SQLite JDBC Driver not found", e);
        }
    }

    public static Connection getConnection() throws SQLException 
    {
        return DriverManager.getConnection(DB_URL);
    }
}