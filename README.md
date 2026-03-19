# E-Commerce TDD Lab — Java

A Test Driven Development (TDD) exercise building a simple e-commerce platform in Java using JUnit 5 and Mockito.  
Each requirement follows the **Red → Green → Refactor** cycle strictly.

---

## 🛠️ Tech Stack

| Tool | Purpose |
|---|---|
| Java 17 | Programming language |
| Maven | Build tool |
| JUnit 5 | Unit testing framework |
| Mockito | Mocking framework for external dependencies |

---

## 📁 Project Structure

```
ecommerce-tdd/
├── src/
│   ├── main/java/com/ecommerce/
│   │   ├── Product.java
│   │   ├── Catalog.java
│   │   ├── LineItem.java
│   │   ├── Cart.java
│   │   ├── InventoryService.java
│   │   ├── FakeInventoryService.java
│   │   ├── DiscountRule.java
│   │   ├── BulkDiscountRule.java
│   │   ├── OrderDiscountRule.java
│   │   ├── DiscountEngine.java
│   │   ├── PaymentGateway.java
│   │   ├── CheckoutResult.java
│   │   ├── CheckoutService.java
│   │   ├── Order.java
│   │   ├── OrderRepository.java
│   │   └── FakeOrderRepository.java
│   └── test/java/com/ecommerce/
│       ├── ProductCatalogTest.java
│       ├── CartTest.java
│       ├── InventoryTest.java
│       ├── DiscountTest.java
│       ├── CheckoutTest.java
│       └── OrderTest.java
└── pom.xml
```

---

## ✅ Requirements & TDD Results

### Requirement A — Product Model & Catalog
Build a `Product` model and a `Catalog` that can add and search products by SKU.

| Stage | Result |
|---|---|
| 🔴 RED | `Product cannot be resolved to a type` — BUILD FAILURE (5 errors) |
| 🟢 GREEN | `Tests run: 5, Failures: 0` — BUILD SUCCESS |
| 🔵 REFACTOR | Extracted validation into private helper methods — BUILD SUCCESS |

**Tests covered:**
- Product created with valid SKU, name, and price
- Product rejects negative price
- Product rejects null SKU
- Catalog finds product by SKU
- Catalog returns null for missing SKU

---

### Requirement B — Shopping Cart
Implement a `Cart` with add, remove, and total calculation.

| Stage | Result |
|---|---|
| 🔴 RED | `ClassNotFoundException: Cart` — BUILD FAILURE |
| 🟢 GREEN | `Tests run: 6, Failures: 0` — BUILD SUCCESS |
| 🔵 REFACTOR | Extracted `validateQuantity()` and `findProductOrThrow()` helpers — BUILD SUCCESS |

**Tests covered:**
- Add item to cart
- Remove item from cart
- Cart total calculated correctly
- Adding product not in catalog throws exception
- Zero quantity throws exception
- Negative quantity throws exception

---

### Requirement C — Inventory Reservation
Add inventory checks when adding items to the cart using a mocked `InventoryService`.

| Stage | Result |
|---|---|
| 🔴 RED | `ClassNotFoundException: InventoryService` — BUILD FAILURE |
| 🟢 GREEN | `Tests run: 5, Failures: 0` — BUILD SUCCESS |
| 🔵 REFACTOR | Created `FakeInventoryService` for reuse across tests — BUILD SUCCESS |

**Tests covered:**
- Adding item within available stock succeeds
- Adding more than available stock throws `IllegalStateException`
- Adding exact available quantity succeeds
- Low inventory (1 left, requesting 2) fails
- Zero inventory throws exception

**Key concept:** Used `Mockito.mock()` to simulate the inventory service without a real backend.

---

### Requirement D — Discount Rules
Introduce a pluggable `DiscountEngine` with bulk and order discount rules.

| Stage | Result |
|---|---|
| 🔴 RED | `DiscountEngine cannot be resolved to a type` — BUILD FAILURE (6 errors) |
| 🟢 GREEN | `Tests run: 6, Failures: 0` — BUILD SUCCESS |
| 🔵 REFACTOR | Made thresholds and percentages configurable via constructor — BUILD SUCCESS |

**Tests covered:**
- No discount below quantity threshold
- Bulk discount (10% off) when quantity >= 10
- Bulk discount applies only to qualifying SKU
- Order discount (5% off) when total >= 1000
- Both bulk and order discounts combine correctly
- No discounts on small orders

**Key concept:** Used the **Strategy Pattern** — each rule is a separate class implementing `DiscountRule`, making new rules easy to add without changing existing code.

---

### Requirement E — Checkout Validation & Payment
Implement checkout flow that validates the cart and charges a payment gateway.

| Stage | Result |
|---|---|
| 🔴 RED | `ClassNotFoundException: PaymentGateway` — BUILD FAILURE |
| 🟢 GREEN | `Tests run: 6, Failures: 0` — BUILD SUCCESS |
| 🔵 REFACTOR | Split into `validateCart()`, `calculateTotal()`, `processPayment()` — BUILD SUCCESS |

**Tests covered:**
- Successful checkout returns success result
- Payment failure returns error result
- Empty cart throws `IllegalStateException`
- Payment gateway called with correct amount
- Payment not called when cart is empty
- Discount applied before charging payment

**Key concept:** Used `mock(PaymentGateway.class)` to simulate payment success and failure — no real money ever charged during tests.

---

### Requirement F — Order History & Simple Persistence
When checkout succeeds, create and save an `Order` record using a repository interface.

| Stage | Result |
|---|---|
| 🔴 RED | `ClassNotFoundException: OrderRepository` — BUILD FAILURE |
| 🟢 GREEN | `Tests run: 6, Failures: 0` — BUILD SUCCESS |
| 🔵 REFACTOR | Used stream-based `saveOrder()` with `getProductBySku()` helper — BUILD SUCCESS |

**Tests covered:**
- Successful checkout creates and saves an order
- Failed payment does NOT create an order
- Order contains correct total
- Order contains correct number of line items
- Order has a timestamp
- `FakeOrderRepository` stores and retrieves orders correctly

**Key concept:** Used the **Repository Pattern** — `FakeOrderRepository` stores orders in memory during tests, hiding the persistence layer behind an interface.

---

## 🧪 Running All Tests

```bash
mvn clean test
```

Expected output:
```
Tests run: 29, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

---

## 📊 Test Summary

| Requirement | Test Class | Tests |
|---|---|---|
| A — Product & Catalog | `ProductCatalogTest` | 5 |
| B — Shopping Cart | `CartTest` | 6 |
| C — Inventory | `InventoryTest` | 5 |
| D — Discounts | `DiscountTest` | 6 |
| E — Checkout | `CheckoutTest` | 6 |
| F — Order History | `OrderTest` | 6 |
| **Total** | | **34** |

---

## 💡 Key TDD Concepts Learned

| Concept | Where Used |
|---|---|
| Red → Green → Refactor | All 6 requirements |
| Mocking with Mockito | Requirements C, E, F |
| Dependency Injection | Cart, CheckoutService |
| Strategy Pattern | DiscountEngine with pluggable rules |
| Repository Pattern | OrderRepository with FakeOrderRepository |
| Interface-based design | InventoryService, PaymentGateway, OrderRepository |

---

## 👩‍💻 Author

**it23596566-Jenitha**  
TDD Lab Assignment — E-Commerce Platform  
