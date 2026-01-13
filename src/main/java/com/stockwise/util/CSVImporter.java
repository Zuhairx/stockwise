package com.stockwise.util;

import com.stockwise.model.Product;
import com.stockwise.model.Transaction;
import com.stockwise.repository.ProductRepository;
import com.stockwise.repository.TransactionRepository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CSVImporter {

    public static void importTransactions(String filePath) throws Exception {
        List<Object[]> transactions = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length < 7) {
                    throw new Exception("Invalid Transaction Template CSV format");
                }
                String id = fields[1].trim();
                if (id.isEmpty()) {
                    throw new Exception("Transaction ID cannot be empty.");
                }
                if (!id.matches("^TR-\\d{3}$")) {
                    throw new Exception("Invalid transaction ID format: " + id
                            + ". Expected format: TR-XXX where XXX is a 3-digit number.");
                }

                String product_id = fields[2].trim();
                if (product_id.isEmpty()) {
                    throw new Exception("Product ID cannot be empty.");
                }
                if (!product_id.matches("^PR-\\d{3}$")) {
                    throw new Exception("Invalid product ID format: " + product_id
                            + ". Expected format: PR-XXX where XXX is a 3-digit number.");
                }
                String productName = fields[3].trim();
                if (productName.isEmpty()) {
                    throw new Exception("Product name cannot be empty.");
                }
                String type = fields[4].trim();
                if (type.isEmpty()) {
                    throw new Exception("Transaction type cannot be empty.");
                }
                if (!type.equals("IN") && !type.equals("OUT")) {
                    throw new Exception("Transaction type must be 'IN' or 'OUT'.");
                }
                String qtyStr = fields[5].trim();
                if (qtyStr.isEmpty()) {
                    throw new Exception("Transaction quantity cannot be empty.");
                }
                int qty;
                try {
                    qty = Integer.parseInt(qtyStr);
                } catch (NumberFormatException e) {
                    throw new Exception("Transaction quantity must be a valid number.");
                }
                String dateStr = fields[6].trim();
                if (dateStr.isEmpty()) {
                    throw new Exception("Transaction date cannot be empty.");
                }

                if (dateStr.startsWith("\"") && dateStr.endsWith("\"")) {
                    dateStr = dateStr.substring(1, dateStr.length() - 1);
                }

                DateTimeFormatter[] formatters = {
                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T' HH:mm:ss"),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")

                };
                LocalDateTime date = null;
                for (DateTimeFormatter formatter : formatters) {
                    try {
                        date = LocalDateTime.parse(dateStr, formatter);
                        break;
                    } catch (Exception e) {

                    }
                }
                if (date == null) {
                    throw new Exception("Unable to parse date: " + dateStr);
                }

                transactions.add(new Object[] { id, product_id, productName, type, qty, date });
            }

            TransactionRepository repo = new TransactionRepository();
            ProductRepository productRepo = new ProductRepository();

            for (Object[] transaction : transactions) {
                String id = (String) transaction[0];
                String product_id = (String) transaction[1];
                String productName = (String) transaction[2];
                String type = (String) transaction[3];
                int qty = (int) transaction[4];
                LocalDateTime date = (LocalDateTime) transaction[5];

                Product product = productRepo.findById(product_id);
                if (product != null) {

                    Transaction existingTransaction = repo.findById(id);
                    if (existingTransaction != null) {

                        if (existingTransaction.getQuantity() == qty && existingTransaction.getType().equals(type)) {
                            throw new Exception("Stock & Type is still the same for transaction ID: " + id);
                        }

                        int currentStock = product.getStock();
                        int revertStock = existingTransaction.getType().equals("IN")
                                ? currentStock - existingTransaction.getQuantity()
                                : currentStock + existingTransaction.getQuantity();
                        productRepo.updateStock(product.getId(), revertStock);

                        repo.insertOrUpdateWithId(id, product.getId(), type, qty, date);

                        currentStock = revertStock;
                        int newStock = type.equals("IN") ? currentStock + qty : currentStock - qty;
                        productRepo.updateStock(product.getId(), newStock);
                    } else {

                        repo.insertOrUpdateWithId(id, product.getId(), type, qty, date);

                        int currentStock = product.getStock();
                        int newStock = type.equals("IN") ? currentStock + qty : currentStock - qty;
                        productRepo.updateStock(product.getId(), newStock);
                    }
                } else {
                    throw new Exception("Product not found: " + productName);
                }
            }

        } catch (IOException | NumberFormatException e) {
            throw new Exception("Error importing CSV: " + e.getMessage(), e);
        }
    }

    public static void importProducts(String filePath) throws Exception {
        List<Product> products = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length < 6) {
                    throw new Exception("Invalid Product Template CSV format");
                }
                String id = fields[1].trim();
                if (id.isEmpty()) {
                    throw new Exception("Product ID cannot be empty.");
                }
                if (!id.matches("^PR-\\d{3}$")) {
                    throw new Exception("Invalid product ID format: " + id
                            + ". Expected format: PR-XXX where XXX is a 3-digit number.");
                }
                String category = fields[2].trim();
                if (category.isEmpty()) {
                    throw new Exception("Product category cannot be empty.");
                }
                String name = fields[3].trim();
                if (name.isEmpty()) {
                    throw new Exception("Product name cannot be empty.");
                }
                String priceStr = fields[4].trim();
                if (priceStr.isEmpty()) {
                    throw new Exception("Product price cannot be empty.");
                }
                int price;
                try {
                    price = Integer.parseInt(priceStr);
                } catch (NumberFormatException e) {
                    throw new Exception("Product price must be a valid number.");
                }
                String stockStr = fields[5].trim();
                if (stockStr.isEmpty()) {
                    throw new Exception("Product stock cannot be empty.");
                }
                int stock;
                try {
                    stock = Integer.parseInt(stockStr);
                } catch (NumberFormatException e) {
                    throw new Exception("Product stock must be a valid number.");
                }

                Product p = new Product(id, category, name, price, stock);
                products.add(p);
            }

            ProductRepository repo = new ProductRepository();
            for (Product p : products) {
                Product existing = repo.findById(p.getId());

                if (existing != null) {

                    repo.updateWithStock(p.getId(), p.getName(), p.getCategory(), p.getPrice(), p.getStock());
                } else {

                    repo.saveWithStock(p.getId(), p.getName(), p.getCategory(), p.getPrice(), p.getStock());
                }
            }

        } catch (IOException | NumberFormatException e) {
            throw new Exception("Error importing CSV: " + e.getMessage(), e);
        }
    }
}
