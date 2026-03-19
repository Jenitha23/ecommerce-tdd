package com.ecommerce;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CheckoutTest {

    private Catalog catalog;
    private Cart cart;
    private InventoryService inventoryService;
    private PaymentGateway paymentGateway;
    private CheckoutService checkoutService;

    @BeforeEach
    void setUp() {
        catalog = new Catalog();
        catalog.addProduct(new Product("SKU001", "Laptop", 999.99));
        catalog.addProduct(new Product("SKU002", "Mouse", 29.99));

        inventoryService = mock(InventoryService.class);
        paymentGateway   = mock(PaymentGateway.class);

        cart = new Cart(catalog, inventoryService);
        checkoutService = new CheckoutService(
            inventoryService,
            paymentGateway,
            new DiscountEngine()
        );
    }

    // Test 1: Successful checkout returns success result
    @Test
    void testSuccessfulCheckout() {
        when(inventoryService.getAvailable("SKU001")).thenReturn(10);
        when(paymentGateway.charge(anyDouble(), anyString()))
            .thenReturn(true);

        cart.addItem("SKU001", 1);
        CheckoutResult result = checkoutService.checkout(cart, "valid-token");

        assertTrue(result.isSuccess());
        assertNotNull(result.getMessage());
    }

    // Test 2: Payment failure returns error result
    @Test
    void testPaymentFailureReturnsError() {
        when(inventoryService.getAvailable("SKU001")).thenReturn(10);
        when(paymentGateway.charge(anyDouble(), anyString()))
            .thenReturn(false);

        cart.addItem("SKU001", 1);
        CheckoutResult result = checkoutService.checkout(cart, "bad-token");

        assertFalse(result.isSuccess());
        assertNotNull(result.getMessage());
    }

    // Test 3: Cannot checkout with empty cart
    @Test
    void testEmptyCartThrowsException() {
        assertThrows(IllegalStateException.class, () -> {
            checkoutService.checkout(cart, "valid-token");
        });
    }

    // Test 4: Payment gateway is called with correct amount
    @Test
    void testPaymentGatewayCalledWithCorrectAmount() {
        when(inventoryService.getAvailable("SKU001")).thenReturn(10);
        when(paymentGateway.charge(anyDouble(), anyString()))
            .thenReturn(true);

        cart.addItem("SKU001", 1);  // 999.99
        checkoutService.checkout(cart, "valid-token");

        verify(paymentGateway).charge(999.99, "valid-token");
    }

    // Test 5: Payment not called when cart is empty
    @Test
    void testPaymentNotCalledWhenCartEmpty() {
        assertThrows(IllegalStateException.class, () -> {
            checkoutService.checkout(cart, "valid-token");
        });
        verifyNoInteractions(paymentGateway);
    }

    // Test 6: Discount is applied before charging payment
    @Test
    void testDiscountAppliedBeforePayment() {
        when(inventoryService.getAvailable("SKU001")).thenReturn(20);
        when(paymentGateway.charge(anyDouble(), anyString()))
            .thenReturn(true);

        cart.addItem("SKU001", 10); // 999.99 x 10 = 9999.9, bulk 10% off
        double expectedAmount = 9999.9 * 0.90 * 0.95; // bulk + order discount
        checkoutService.checkout(cart, "valid-token");

        verify(paymentGateway).charge(
            doubleThat(amount -> Math.abs(amount - expectedAmount) < 0.01),
            eq("valid-token")
        );
    }
}