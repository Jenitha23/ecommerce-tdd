package com.ecommerce;

public interface DiscountRule {
    double apply(Cart cart, double currentTotal);
}