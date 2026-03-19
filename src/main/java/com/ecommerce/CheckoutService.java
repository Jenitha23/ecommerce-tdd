package com.ecommerce;

public class CheckoutService {

    private final InventoryService inventoryService;
    private final PaymentGateway   paymentGateway;
    private final DiscountEngine   discountEngine;

    public CheckoutService(
            InventoryService inventoryService,
            PaymentGateway paymentGateway,
            DiscountEngine discountEngine) {
        this.inventoryService = inventoryService;
        this.paymentGateway   = paymentGateway;
        this.discountEngine   = discountEngine;
    }

    public CheckoutResult checkout(Cart cart, String paymentToken) {
        validateCart(cart);
        double finalTotal = calculateTotal(cart);
        return processPayment(finalTotal, paymentToken);
    }

    // --- private helpers ---

    private void validateCart(Cart cart) {
        if (cart.getSkus().isEmpty()) {
            throw new IllegalStateException(
                "Cannot checkout with an empty cart"
            );
        }
    }

    private double calculateTotal(Cart cart) {
        return discountEngine.applyDiscounts(cart);
    }

    private CheckoutResult processPayment(double amount, String token) {
        boolean paid = paymentGateway.charge(amount, token);
        if (paid) {
            return new CheckoutResult(
                true,
                "Payment successful. Total charged: " + amount
            );
        }
        return new CheckoutResult(
            false,
            "Payment failed. Please try again."
        );
    }
}