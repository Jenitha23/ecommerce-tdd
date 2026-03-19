package com.ecommerce;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProductCatalogTest {

    // Test 1: Product is created successfully with valid data
    @Test
    void testCreateProductWithValidData() {
        Product product = new Product("SKU001", "Laptop", 999.99);
        assertEquals("SKU001", product.getSku());
        assertEquals("Laptop", product.getName());
        assertEquals(999.99, product.getPrice());
    }

    // Test 2: Product rejects negative price
    @Test
    void testProductRejectsNegativePrice() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Product("SKU002", "Mouse", -10.00);
        });
    }

    // Test 3: Product rejects null SKU
    @Test
    void testProductRejectsNullSku() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Product(null, "Keyboard", 49.99);
        });
    }

    // Test 4: Catalog can add and find a product by SKU
    @Test
    void testCatalogFindsProductBySku() {
        Catalog catalog = new Catalog();
        Product product = new Product("SKU001", "Laptop", 999.99);
        catalog.addProduct(product);
        assertEquals(product, catalog.findBySku("SKU001"));
    }

    // Test 5: Searching missing SKU returns null
    @Test
    void testCatalogReturnNullForMissingSku() {
        Catalog catalog = new Catalog();
        assertNull(catalog.findBySku("DOESNOTEXIST"));
    }
}