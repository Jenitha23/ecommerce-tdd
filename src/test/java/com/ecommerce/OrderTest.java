package com.ecommerce;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderTest {

    private Catalog catalog;
    private Cart cart;
    private InventoryService inventoryService;
    private PaymentGateway paymentGateway;
    private OrderRepository orderRepository;
    private CheckoutService checkoutService;

    @BeforeEach
    void setUp() {
        catalog = new Catalog();
        catalog.addProduct(new Product("SKU001", "Laptop", 999.99));
        catalog.addProduct(new Product("SKU002", "Mouse", 29.99));

        inventoryService = mock(InventoryService.class);
        paymentGateway   = mock(PaymentGateway.class);
        orderRepository  = mock(OrderRepository.class);

        cart = new Cart(catalog, inventoryService);
        checkoutService = new CheckoutService(
            inventoryService,
            paymentGateway,
            new DiscountEngine(),
            orderRepository
        );
    }

    // Test 1: Successful checkout creates and saves an order
    @Test
    void testSuccessfulCheckoutCreatesOrder() {
        when(inventoryService.getAvailable("SKU001")).thenReturn(10);
        when(paymentGateway.charge(anyDouble(), anyString()))
            .thenReturn(true);

        cart.addItem("SKU001", 1);
        CheckoutResult result = checkoutService.checkout(cart, "valid-token");

        assertTrue(result.isSuccess());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    // Test 2: Failed payment does NOT create an order
    @Test
    void testFailedPaymentDoesNotCreateOrder() {
        when(inventoryService.getAvailable("SKU001")).thenReturn(10);
        when(paymentGateway.charge(anyDouble(), anyString()))
            .thenReturn(false);

        cart.addItem("SKU001", 1);
        CheckoutResult result = checkoutService.checkout(cart, "bad-token");

        assertFalse(result.isSuccess());
        verify(orderRepository, never()).save(any(Order.class));
    }

    // Test 3: Order contains correct total
    @Test
    void testOrderHasCorrectTotal() {
        when(inventoryService.getAvailable("SKU001")).thenReturn(10);
        when(paymentGateway.charge(anyDouble(), anyString()))
            .thenReturn(true);

        cart.addItem("SKU001", 1); // 999.99
        checkoutService.checkout(cart, "valid-token");

        verify(orderRepository).save(argThat(order ->
            Math.abs(order.getTotal() - 999.99) < 0.01
        ));
    }

    // Test 4: Order contains correct line items
    @Test
    void testOrderHasCorrectLineItems() {
        when(inventoryService.getAvailable("SKU001")).thenReturn(10);
        when(inventoryService.getAvailable("SKU002")).thenReturn(10);
        when(paymentGateway.charge(anyDouble(), anyString()))
            .thenReturn(true);

        cart.addItem("SKU001", 2);
        cart.addItem("SKU002", 3);
        checkoutService.checkout(cart, "valid-token");

        verify(orderRepository).save(argThat(order ->
            order.getLineItems().size() == 2
        ));
    }

    // Test 5: Order has a timestamp
    @Test
    void testOrderHasTimestamp() {
        when(inventoryService.getAvailable("SKU001")).thenReturn(10);
        when(paymentGateway.charge(anyDouble(), anyString()))
            .thenReturn(true);

        cart.addItem("SKU001", 1);
        checkoutService.checkout(cart, "valid-token");

        verify(orderRepository).save(argThat(order ->
            order.getCreatedAt() != null
        ));
    }

    // Test 6: Find all orders using fake repository
    @Test
    void testFakeRepositoryStoresAndRetrievesOrders() {
        FakeOrderRepository fakeRepo = new FakeOrderRepository();
        CheckoutService svc = new CheckoutService(
            inventoryService,
            paymentGateway,
            new DiscountEngine(),
            fakeRepo
        );

        when(inventoryService.getAvailable("SKU001")).thenReturn(10);
        when(paymentGateway.charge(anyDouble(), anyString()))
            .thenReturn(true);

        cart.addItem("SKU001", 1);
        svc.checkout(cart, "valid-token");

        assertEquals(1, fakeRepo.findAll().size());
    }
}