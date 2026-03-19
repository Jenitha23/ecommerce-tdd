package com.ecommerce;

import java.time.LocalDateTime;
import java.util.List;

public class Order {

    private final List<LineItem>  lineItems;
    private final double          total;
    private final LocalDateTime   createdAt;

    public Order(List<LineItem> lineItems, double total) {
        this.lineItems = lineItems;
        this.total     = total;
        this.createdAt = LocalDateTime.now();
    }

    public List<LineItem>  getLineItems() { return lineItems; }
    public double          getTotal()     { return total; }
    public LocalDateTime   getCreatedAt() { return createdAt; }
}