package com.stockwise.model;

import javafx.beans.property.*;

public class Product {

    private StringProperty id = new SimpleStringProperty();
    private StringProperty category = new SimpleStringProperty();
    private StringProperty name = new SimpleStringProperty();
    private IntegerProperty price = new SimpleIntegerProperty();
    private IntegerProperty stock = new SimpleIntegerProperty();

    public Product(String id, String category, String name, int price, int stock) {
        this.id.set(id);
        this.category.set(category);
        this.name.set(name);
        this.price.set(price);
        this.stock.set(stock);
    }

    public String getId() {
        return id.get();
    }

    public String getCategory() {
        return category.get();
    }

    public String getName() {
        return name.get();
    }

    public int getPrice() {
        return price.get();
    }

    public int getStock() {
        return stock.get();
    }

    public StringProperty idProperty() {
        return id;
    }

    public StringProperty categoryProperty() {
        return category;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public IntegerProperty priceProperty() {
        return price;
    }

    public IntegerProperty stockProperty() {
        return stock;
    }

    public static void resetIdCounter() {
        nextId = 1;
    }

    public static void setNextId(int id) {
        nextId = id;
    }

    public String getFormattedId() {
        return getId();
    }

    private static int nextId = 1;

    public static String generateNextId() {
        return String.format("PR-%03d", nextId++);
    }

    public String getFormattedPrice() {
        return "Rp. " + getPrice();
    }

}
