package com.ecommerce;

public class OrderDiscountRule implements DiscountRule {

    private final double threshold;
    private final double discountPct;

    // Default: total >= 1000 gets 5% off
    public OrderDiscountRule() {
        this(1000.00, 0.05);
    }

    // Custom threshold and discount
    public OrderDiscountRule(double threshold, double discountPct) {
        this.threshold   = threshold;
        this.discountPct = discountPct;
    }

    @Override
    public double apply(Cart cart, double currentTotal) {
        if (currentTotal >= threshold) {
            return currentTotal - (currentTotal * discountPct);
        }
        return currentTotal;
    }
}