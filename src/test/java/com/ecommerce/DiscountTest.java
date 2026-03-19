package com.ecommerce;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DiscountTest {

    private Catalog catalog;
    private Cart cart;

    @BeforeEach
    void setUp() {
        catalog = new Catalog();
        catalog.addProduct(new Product("SKU001", "Laptop", 100.00));
        catalog.addProduct(new Product("SKU002", "Mouse", 50.00));
        cart = new Cart(catalog);
    }

    // Test 1: No discount applied when quantity below 10
    @Test
    void testNoBulkDiscountBelowThreshold() {
        cart.addItem("SKU001", 5);
        DiscountEngine engine = new DiscountEngine();
        double total = engine.applyDiscounts(cart);
        assertEquals(500.00, total, 0.01);
    }

    // Test 2: Bulk discount 10% off when quantity >= 10
    @Test
    void testBulkDiscountAppliedWhenQuantityTen() {
        cart.addItem("SKU001", 10);  // 100 x 10 = 1000, 10% off = 900
        DiscountEngine engine = new DiscountEngine();
        double total = engine.applyDiscounts(cart);
        assertEquals(900.00, total, 0.01);
    }

   // Test 3: Bulk discount applies to qualifying SKU only
@Test
void testBulkDiscountOnlyOnQualifyingSku() {
    cart.addItem("SKU001", 10);  // 100 x 10 = 1000, bulk 10% off = 900
    cart.addItem("SKU002", 2);   // 50  x 2  = 100,  no bulk discount
    // subtotal after bulk = 1000, order discount 5% also applies = 950
    DiscountEngine engine = new DiscountEngine();
    double total = engine.applyDiscounts(cart);
    assertEquals(950.00, total, 0.01); // fixed: 900 + 100 = 1000, then 5% off = 950
}

    // Test 4: Order discount 5% off when total >= 1000
    @Test
    void testOrderDiscountAppliedWhenTotalOverThousand() {
        cart.addItem("SKU001", 5);   // 100 x 5 = 500
        cart.addItem("SKU002", 10);  // 50 x 10 = 500
        // subtotal = 1000, 5% off = 950
        DiscountEngine engine = new DiscountEngine();
        double total = engine.applyDiscounts(cart);
        assertEquals(950.00, total, 0.01);
    }

    // Test 5: Both bulk AND order discounts apply together
    @Test
    void testBulkAndOrderDiscountCombined() {
        cart.addItem("SKU001", 10);  // 100 x 10 = 1000, bulk 10% off = 900
        cart.addItem("SKU002", 4);   // 50  x 4  = 200,  no bulk discount
        // subtotal after bulk = 1100, order discount 5% off = 1045
        DiscountEngine engine = new DiscountEngine();
        double total = engine.applyDiscounts(cart);
        assertEquals(1045.00, total, 0.01);
    }

    // Test 6: No discounts on small order
    @Test
    void testNoDiscountsOnSmallOrder() {
        cart.addItem("SKU001", 2);   // 100 x 2 = 200
        cart.addItem("SKU002", 1);   // 50  x 1 = 50
        // total = 250, no discounts
        DiscountEngine engine = new DiscountEngine();
        double total = engine.applyDiscounts(cart);
        assertEquals(250.00, total, 0.01);
    }
}