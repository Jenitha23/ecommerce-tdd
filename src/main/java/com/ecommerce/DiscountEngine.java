package com.ecommerce;

import java.util.List;

public class DiscountEngine {

    private final List<DiscountRule> rules;

    // Default constructor loads standard rules
    public DiscountEngine() {
        this.rules = List.of(
            new BulkDiscountRule(),
            new OrderDiscountRule()
        );
    }

    // Constructor for custom rules (useful in tests)
    public DiscountEngine(List<DiscountRule> rules) {
        this.rules = rules;
    }

    public double applyDiscounts(Cart cart) {
        double total = cart.getTotal();
        for (DiscountRule rule : rules) {
            total = rule.apply(cart, total);
        }
        return total;
    }
}