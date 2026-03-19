package com.ecommerce;

public class BulkDiscountRule implements DiscountRule {

    private final int    threshold;
    private final double discountPct;

    // Default: qty >= 10 gets 10% off
    public BulkDiscountRule() {
        this(10, 0.10);
    }

    // Custom threshold and discount
    public BulkDiscountRule(int threshold, double discountPct) {
        this.threshold   = threshold;
        this.discountPct = discountPct;
    }

    @Override
    public double apply(Cart cart, double currentTotal) {
        double discount = 0.0;
        for (String sku : cart.getSkus()) {
            if (cart.getQuantity(sku) >= threshold) {
                discount += cart.getLineTotal(sku) * discountPct;
            }
        }
        return currentTotal - discount;
    }
}