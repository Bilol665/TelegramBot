package org.Videos.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Utils {
    private static Connection connection;
    private static final String DB_NAME = "video_bot";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "1402";
    public static Connection connection() {
        if (connection == null) {
            try {
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(
                        "jdbc:postgresql://localhost:5433/"+DB_NAME, DB_USER,DB_PASSWORD);
            } catch (ClassNotFoundException | SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return connection;
    }
}
