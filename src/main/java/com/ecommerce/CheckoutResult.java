package com.ecommerce;

public class CheckoutResult {

    private final boolean success;
    private final String  message;

    public CheckoutResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() { return success; }
    public String  getMessage() { return message; }
}