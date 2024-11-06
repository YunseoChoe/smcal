package org.zerock.smcal.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public enum DBConnectionUtil {
    INSTANCE;

    private static final String URL = "jdbc:mysql://pilab.smu.ac.kr:3306/sky09508_db?useUnicode=true&characterEncoding=utf-8";
    private static final String USER = "sky09508";
    private static final String PASSWORD = "yW7bD2rPjM!";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC Driver not found", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
