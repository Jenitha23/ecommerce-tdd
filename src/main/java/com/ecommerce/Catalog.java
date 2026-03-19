package com.ecommerce;

import java.util.HashMap;
import java.util.Map;

public class Catalog {

    private final Map<String, Product> products = new HashMap<>();

    public void addProduct(Product product) {
        products.put(product.getSku(), product);
    }

    public Product findBySku(String sku) {
        return products.get(sku);   // returns null if not found
    }
}