package com.ecommerce;

import java.util.ArrayList;
import java.util.List;

public class CheckoutService {

    private final InventoryService inventoryService;
    private final PaymentGateway   paymentGateway;
    private final DiscountEngine   discountEngine;
    private final OrderRepository  orderRepository;

    public CheckoutService(
            InventoryService inventoryService,
            PaymentGateway paymentGateway,
            DiscountEngine discountEngine,
            OrderRepository orderRepository) {
        this.inventoryService = inventoryService;
        this.paymentGateway   = paymentGateway;
        this.discountEngine   = discountEngine;
        this.orderRepository  = orderRepository;
    }

    public CheckoutResult checkout(Cart cart, String paymentToken) {
        validateCart(cart);
        double finalTotal = calculateTotal(cart);
        return processPayment(cart, finalTotal, paymentToken);
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

    private CheckoutResult processPayment(
            Cart cart, double amount, String token) {
        boolean paid = paymentGateway.charge(amount, token);
        if (paid) {
            saveOrder(cart, amount);
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

    private void saveOrder(Cart cart, double total) {
        List<LineItem> snapshot = cart.getSkus().stream()
            .map(sku -> new LineItem(
                cart.getProductBySku(sku),
                cart.getQuantity(sku)
            ))
            .collect(java.util.stream.Collectors.toList());

        orderRepository.save(new Order(snapshot, total));
    }
}