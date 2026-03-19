package com.ecommerce;

import java.util.HashMap;
import java.util.Map;

public class Cart {

    private final Catalog catalog;
    private final InventoryService inventoryService;
    private final Map<String, LineItem> items = new HashMap<>();

    // Constructor WITH inventory service (used from Requirement C onwards)
    public Cart(Catalog catalog, InventoryService inventoryService) {
        this.catalog = catalog;
        this.inventoryService = inventoryService;
    }

    // Old constructor kept so Requirement B tests don't break
    public Cart(Catalog catalog) {
        this(catalog, null);
    }

    public void addItem(String sku, int quantity) {
        validateQuantity(quantity);
        Product product = findProductOrThrow(sku);
        checkInventory(sku, quantity);

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

    private void checkInventory(String sku, int quantity) {
        if (inventoryService == null) return;
        int available = inventoryService.getAvailable(sku);
        if (quantity > available) {
            throw new IllegalStateException(
                "Insufficient inventory for SKU: " + sku +
                ". Requested: " + quantity +
                ", Available: " + available
            );
        }
    }
}