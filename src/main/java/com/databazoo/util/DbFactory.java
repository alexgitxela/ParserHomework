package com.databazoo.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbFactory {

    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    private static final String DB_CONNECTION = "jdbc:mysql://localhost:3306/parserhomework";

    private static Connection connection;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
            } catch (SQLException e) {
                throw new IllegalStateException("Connection Failed", e);
            }
        }
        return connection;
    }
}
