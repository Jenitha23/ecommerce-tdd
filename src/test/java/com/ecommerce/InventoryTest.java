package com.ecommerce;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InventoryTest {

    private Catalog catalog;
    private InventoryService inventoryService;
    private Cart cart;

    @BeforeEach
    void setUp() {
        catalog = new Catalog();
        catalog.addProduct(new Product("SKU001", "Laptop", 999.99));
        catalog.addProduct(new Product("SKU002", "Mouse", 29.99));

        // Create a MOCK inventory service — no real service needed!
        inventoryService = mock(InventoryService.class);
        cart = new Cart(catalog, inventoryService);
    }

    // Test 1: Adding item within available stock succeeds
    @Test
    void testAddItemWithSufficientInventory() {
        when(inventoryService.getAvailable("SKU001")).thenReturn(10);
        cart.addItem("SKU001", 3);
        assertEquals(3, cart.getQuantity("SKU001"));
    }

    // Test 2: Adding more than available stock throws exception
    @Test
    void testAddItemExceedsInventoryThrowsException() {
        when(inventoryService.getAvailable("SKU001")).thenReturn(2);
        assertThrows(IllegalStateException.class, () -> {
            cart.addItem("SKU001", 5);
        });
    }

    // Test 3: Adding exact available quantity succeeds
    @Test
    void testAddItemWithExactAvailableQuantity() {
        when(inventoryService.getAvailable("SKU002")).thenReturn(5);
        cart.addItem("SKU002", 5);
        assertEquals(5, cart.getQuantity("SKU002"));
    }

    // Test 4: Low inventory - only 1 left, requesting 2 fails
    @Test
    void testLowInventoryFails() {
        when(inventoryService.getAvailable("SKU001")).thenReturn(1);
        assertThrows(IllegalStateException.class, () -> {
            cart.addItem("SKU001", 2);
        });
    }

    // Test 5: Zero inventory throws exception
    @Test
    void testZeroInventoryThrowsException() {
        when(inventoryService.getAvailable("SKU001")).thenReturn(0);
        assertThrows(IllegalStateException.class, () -> {
            cart.addItem("SKU001", 1);
        });
    }
}