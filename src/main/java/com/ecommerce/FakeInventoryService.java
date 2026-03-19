package com.ecommerce;

import java.util.HashMap;
import java.util.Map;

public class FakeInventoryService implements InventoryService {

    private final Map<String, Integer> stock = new HashMap<>();

    public void setStock(String sku, int quantity) {
        stock.put(sku, quantity);
    }

    @Override
    public int getAvailable(String sku) {
        return stock.getOrDefault(sku, 0);
    }
}