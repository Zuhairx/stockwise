package com.stockwise.util;

import com.stockwise.model.Transaction;
import com.stockwise.repository.TransactionRepository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CSVImporter {

    public static void importTransactions(String filePath) {
        List<Transaction> transactions = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length == 5) {
                    String id = fields[0].trim();
                    String productName = fields[1].trim();
                    String type = fields[2].trim();
                    int quantity = Integer.parseInt(fields[3].trim());
                    LocalDateTime date = LocalDateTime.parse(fields[4].trim(), formatter);

                    // Assuming product_id is derived or set to a default; adjust as needed
                    String productId = "P-001"; // Placeholder, you may need to map productName to productId

                    Transaction t = new Transaction(id, productId, productName, type, quantity, date);
                    transactions.add(t);
                }
            }

            // Insert into database
            TransactionRepository repo = new TransactionRepository();
            for (Transaction t : transactions) {
                // Assuming insert method exists; adjust if needed
                repo.insert(t.getProductId(), t.getType(), t.getQuantity());
            }

        } catch (IOException e) {
            System.out.println("Error importing CSV: " + e.getMessage());
        }
    }
}
