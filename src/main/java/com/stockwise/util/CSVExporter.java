package com.stockwise.util;

import com.stockwise.model.Product;
import com.stockwise.model.Transaction;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class CSVExporter {

    public static void exportTransactions(List<Transaction> list, File file) {
        try (FileWriter writer = new FileWriter(file)) {

            writer.write(" No,transaction_id,product_id,product_name,type,quantity,transaction_date\n");

            int no = 1;
            for (Transaction t : list) {
                writer.write(String.format(
                        "%d,%s,%s,%s,%s,%d,%s\n",
                        no,
                        t.getId(),
                        t.getProductId(),
                        t.getProductName(),
                        t.getType(),
                        t.getQuantity(),
                        t.getFormattedDate()));
                no++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void exportProducts(List<Product> list, File file) {
        try (FileWriter writer = new FileWriter(file)) {

            writer.write("No,product_id,product_category,product_name,price,stock\n");

            int no = 1;
            for (Product p : list) {
                writer.write(String.format(
                        "%d,%s,%s,%s,%d,%d\n",
                        no,
                        p.getId(),
                        p.getCategory(),
                        p.getName(),
                        p.getPrice(),
                        p.getStock()));
                no++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
