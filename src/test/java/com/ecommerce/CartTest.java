package com.ecommerce;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CartTest {

    private Catalog catalog;
    private Cart cart;

    @BeforeEach
    void setUp() {
        catalog = new Catalog();
        catalog.addProduct(new Product("SKU001", "Laptop", 999.99));
        catalog.addProduct(new Product("SKU002", "Mouse", 29.99));
        cart = new Cart(catalog);
    }

    // Test 1: Add a valid item to cart
    @Test
    void testAddItemToCart() {
        cart.addItem("SKU001", 2);
        assertEquals(2, cart.getQuantity("SKU001"));
    }

    // Test 2: Remove an item from cart
    @Test
    void testRemoveItemFromCart() {
        cart.addItem("SKU001", 2);
        cart.removeItem("SKU001");
        assertEquals(0, cart.getQuantity("SKU001"));
    }

    // Test 3: Cart total is calculated correctly
    @Test
    void testCartTotal() {
        cart.addItem("SKU001", 2);  // 999.99 x 2 = 1999.98
        cart.addItem("SKU002", 1);  // 29.99  x 1 = 29.99
        assertEquals(2029.97, cart.getTotal(), 0.01);
    }

    // Test 4: Adding product not in catalog throws exception
    @Test
    void testAddItemNotInCatalogThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            cart.addItem("INVALID_SKU", 1);
        });
    }

    // Test 5: Quantity must be greater than zero
    @Test
    void testAddItemWithZeroQuantityThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            cart.addItem("SKU001", 0);
        });
    }

    // Test 6: Quantity must not be negative
    @Test
    void testAddItemWithNegativeQuantityThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            cart.addItem("SKU001", -3);
        });
    }
}