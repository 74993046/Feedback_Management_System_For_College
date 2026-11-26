package com.structude.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String URL = "jdbc:mysql://localhost:3306/college";
    private static final String USER = "varad";
    private static final String PASSWORD = "Varad@2008";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC Driver not found.", e);
        }
    }

    public static Connection connectDatabase() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
