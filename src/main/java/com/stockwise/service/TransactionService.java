package com.stockwise.service;

import com.stockwise.model.Product;
import com.stockwise.model.Transaction;
import com.stockwise.repository.ProductRepository;
import com.stockwise.repository.TransactionRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

public class TransactionService {

    private final TransactionRepository transactionRepo = new TransactionRepository();
    private final ProductRepository productRepo = new ProductRepository();

    public List<Transaction> getAllTransactions() {
        return transactionRepo.findAll();
    }

    public List<Transaction> searchTransactions(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllTransactions();
        }
        return transactionRepo.searchTransactions(query.trim());
    }

    public boolean process(Product product, String type, int qty) {

        int stock = product.getStock();

        if (type.equals("OUT") && qty > stock) {
            return false;
        }

        int newStock = type.equals("IN")
                ? stock + qty
                : stock - qty;

        System.out.println("Processing transaction: product=" + product.getId() + ", type=" + type + ", qty=" + qty);
        transactionRepo.insert(product.getId(), type, qty);
        productRepo.updateStock(product.getId(), newStock);
        System.out.println("Transaction processed successfully");

        return true;
    }

    public void deleteTransaction(String id) {

        Transaction transaction = transactionRepo.findById(id);
        if (transaction != null) {

            Product product = productRepo.findById(transaction.getProductId());
            if (product != null) {
                int currentStock = product.getStock();
                int quantity = transaction.getQuantity();
                int newStock;

                if ("IN".equals(transaction.getType())) {
                    newStock = currentStock - quantity;
                } else if ("OUT".equals(transaction.getType())) {
                    newStock = currentStock + quantity;
                } else {

                    newStock = currentStock;
                }

                productRepo.updateStock(transaction.getProductId(), newStock);
            }
        }

        transactionRepo.delete(id);
    }

    public void deleteAllTransactions() {
        transactionRepo.deleteAll();
        productRepo.resetAllStocksToZero();
    }

    public boolean updateTransaction(String transactionId, String newProductId, String newType, int newQty) {

        Transaction oldTransaction = transactionRepo.findById(transactionId);
        if (oldTransaction == null) {
            return false;
        }

        Product oldProduct = productRepo.findById(oldTransaction.getProductId());
        if (oldProduct == null) {
            return false;
        }

        Product newProduct = productRepo.findById(newProductId);
        if (newProduct == null) {
            return false;
        }

        int oldStockAdjustment = oldTransaction.getType().equals("IN") ? -oldTransaction.getQuantity()
                : oldTransaction.getQuantity();
        int reversedStock = oldProduct.getStock() + oldStockAdjustment;
        productRepo.updateStock(oldProduct.getId(), reversedStock);

        int currentStockForNew = oldProduct.getId().equals(newProductId) ? reversedStock : newProduct.getStock();

        if (newType.equals("OUT") && newQty > currentStockForNew) {

            productRepo.updateStock(oldProduct.getId(), oldProduct.getStock());
            return false;
        }

        int newStockAdjustment = newType.equals("IN") ? newQty : -newQty;
        int finalNewStock = currentStockForNew + newStockAdjustment;
        productRepo.updateStock(newProduct.getId(), finalNewStock);

        ZonedDateTime jakartaNow = ZonedDateTime.now(ZoneId.of("Asia/Jakarta"));
        LocalDateTime jakartaDate = jakartaNow.toLocalDateTime();
        transactionRepo.insertOrUpdateWithId(transactionId, newProductId, newType, newQty, jakartaDate);

        return true;
    }

}
