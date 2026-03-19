package com.ecommerce;

import java.util.HashMap;
import java.util.Map;

public class Cart {

    private final Catalog catalog;
    private final Map<String, LineItem> items = new HashMap<>();

    public Cart(Catalog catalog) {
        this.catalog = catalog;
    }

    public void addItem(String sku, int quantity) {
        validateQuantity(quantity);
        Product product = findProductOrThrow(sku);

        if (items.containsKey(sku)) {
            LineItem existing = items.get(sku);
            existing.setQuantity(existing.getQuantity() + quantity);
        } else {
            items.put(sku, new LineItem(product, quantity));
        }
    }

    public void removeItem(String sku) {
        items.remove(sku);
    }

    public int getQuantity(String sku) {
        LineItem item = items.get(sku);
        return item == null ? 0 : item.getQuantity();
    }

    public double getTotal() {
        return items.values().stream()
                .mapToDouble(LineItem::getSubtotal)
                .sum();
    }

    // --- private helpers ---

    private void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
    }

    private Product findProductOrThrow(String sku) {
        Product product = catalog.findBySku(sku);
        if (product == null) {
            throw new IllegalArgumentException("Product not found in catalog: " + sku);
        }
        return product;
    }
}