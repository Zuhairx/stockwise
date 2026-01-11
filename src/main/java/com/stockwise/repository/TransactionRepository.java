package com.stockwise.repository;

import com.stockwise.model.Transaction;
import com.stockwise.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepository {
    public List<Transaction> findAll() {
        List<Transaction> list = new ArrayList<>();

        System.out.println("Loading transactions from database...");

        try (Connection conn = DBConnection.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(
                        """
                                    SELECT t.transaction_id, p.product_id, p.product_name, t.type, t.quantity, t.transaction_date
                                    FROM transactions t
                                    JOIN products p ON t.product_id = p.product_id
                                    ORDER BY t.transaction_date DESC
                                """)) {

            while (rs.next()) {
                Transaction t = new Transaction(
                        rs.getString("transaction_id"),
                        rs.getString("product_id"),
                        rs.getString("product_name"),
                        rs.getString("type"),
                        rs.getInt("quantity"),
                        rs.getTimestamp("transaction_date").toLocalDateTime());
                list.add(t);
                System.out.println("Loaded transaction: " + t.getId() + ", product: " + t.getProductId());
            }

        } catch (Exception e) {
            System.out.println("Error loading transactions: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("Total transactions loaded: " + list.size());
        return list;
    }

    public void insert(String productId, String type, int qty) {
        String transactionId = generateNextTransactionId();
        String sql = "INSERT INTO transactions (transaction_id, product_id, type, quantity, transaction_date) VALUES (?, ?, ?, ?, NOW())";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, transactionId);
            ps.setString(2, productId);
            ps.setString(3, type);
            ps.setInt(4, qty);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Transaction inserted successfully: " + transactionId + ", " + productId + ", "
                        + type + ", " + qty);
            } else {
                System.out.println("Transaction insert failed: no rows affected");
            }

        } catch (SQLException e) {
            System.out.println("Error inserting transaction: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String generateNextTransactionId() {
        String lastId = getLastTransactionId();
        if (lastId == null) {
            return "TR-001";
        }
        try {
            int numericId = Integer.parseInt(lastId.substring(3));
            return String.format("TR-%03d", numericId + 1);
        } catch (NumberFormatException e) {
            return "TR-001";
        }
    }

    public void delete(String id) {
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement("DELETE FROM transactions WHERE transaction_id=?")) {

            ps.setString(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteAll() {
        String sql = "DELETE FROM transactions";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getLastTransactionId() {
        String sql = "SELECT transaction_id FROM transactions ORDER BY transaction_id DESC LIMIT 1";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getString("transaction_id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
