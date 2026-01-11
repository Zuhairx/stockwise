package com.stockwise.util;

import java.sql.Connection;

public class DBTest {

    public static void main(String[] args) {
        Connection conn = DBConnection.getConnection();

        if (conn != null) {
            System.out.println("✅ Database connected successfully!");
        } else {
            System.out.println("❌ Database connection failed!");
        }
    }
}
