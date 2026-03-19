package com.ecommerce;

public class Product {

    private final String sku;
    private final String name;
    private final double price;

    public Product(String sku, String name, double price) {
        this.sku   = requireNonBlank(sku,   "SKU");
        this.name  = requireNonBlank(name,  "Name");
        this.price = requireNonNegative(price);
    }

    private static String requireNonBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or empty");
        }
        return value;
    }

    private static double requireNonNegative(double value) {
        if (value < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        return value;
    }

    public String getSku()   { return sku; }
    public String getName()  { return name; }
    public double getPrice() { return price; }
}