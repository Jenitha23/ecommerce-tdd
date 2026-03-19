package com.ecommerce;

public interface PaymentGateway {
    boolean charge(double amount, String token);
}