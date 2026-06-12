package com.example.service;

import com.example.model.Checkout;
import com.example.model.CheckoutStatus;
import com.example.model.LineItem;
import com.example.model.Order;
import com.example.model.OrderStatus;
import com.example.model.Payment;
import com.example.model.PaymentMethod;
import com.example.model.PaymentStatus;
import com.example.model.Product;
import com.example.model.Shipping;
import com.example.model.ShippingStatus;
import com.example.model.ShoppingCart;
import io.quarkus.runtime.LaunchMode;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.time.Instant;

@ApplicationScoped
public class TestDataSeeder {

    @Transactional
    void onStart(@Observes StartupEvent event) {
        if (LaunchMode.current() == LaunchMode.NORMAL || Product.count() > 0) {
            return;
        }
        seed();
    }

    private void seed() {
        Product mug = product("Coffee Mug", "Ceramic mug, 350ml", "12.99", "MUG-001", 120);
        Product notebook = product("Notebook", "A5 ruled notebook, 200 pages", "8.50", "NOTE-001", 250);
        Product mouse = product("Wireless Mouse", "Ergonomic wireless mouse", "29.99", "MOUSE-001", 75);
        Product keyboard = product("Mechanical Keyboard", "RGB mechanical keyboard", "89.99", "KB-001", 40);

        ShoppingCart aliceCart = cart("cust-alice");
        cartItem(aliceCart, mug, 2);
        cartItem(aliceCart, notebook, 1);

        ShoppingCart bobCart = cart("cust-bob");
        cartItem(bobCart, mouse, 1);
        cartItem(bobCart, keyboard, 1);

        Order completedOrder = order("ORD-2026-0001", OrderStatus.CONFIRMED, "34.48");
        orderItem(completedOrder, mug, 2);
        orderItem(completedOrder, notebook, 1);

        Checkout completedCheckout = checkout(aliceCart, completedOrder, CheckoutStatus.COMPLETED, Instant.now());
        shipping(completedOrder, "Alice Smith", "123 Main St", "Springfield", "62701", "US", "FedEx", "FX123456789");
        payment(completedOrder, completedCheckout, "34.48", PaymentMethod.CREDIT_CARD, PaymentStatus.CAPTURED, "txn-001");

        Order pendingOrder = order("ORD-2026-0002", OrderStatus.PENDING, "119.98");
        orderItem(pendingOrder, mouse, 1);
        orderItem(pendingOrder, keyboard, 1);

        checkout(bobCart, pendingOrder, CheckoutStatus.IN_PROGRESS, null);
    }

    private Product product(String name, String description, String price, String sku, int stock) {
        Product product = new Product();
        product.name = name;
        product.description = description;
        product.price = new BigDecimal(price);
        product.sku = sku;
        product.stockQuantity = stock;
        product.createdAt = Instant.now();
        product.persist();
        return product;
    }

    private ShoppingCart cart(String customerId) {
        ShoppingCart cart = new ShoppingCart();
        cart.customerId = customerId;
        Instant now = Instant.now();
        cart.createdAt = now;
        cart.updatedAt = now;
        cart.persist();
        return cart;
    }

    private void cartItem(ShoppingCart cart, Product product, int quantity) {
        LineItem lineItem = new LineItem();
        lineItem.product = product;
        lineItem.quantity = quantity;
        lineItem.unitPrice = product.price;
        lineItem.shoppingCart = cart;
        lineItem.persist();
        cart.lineItems.add(lineItem);
    }

    private Order order(String orderNumber, OrderStatus status, String totalAmount) {
        Order order = new Order();
        order.orderNumber = orderNumber;
        order.status = status;
        order.totalAmount = new BigDecimal(totalAmount);
        order.createdAt = Instant.now();
        order.persist();
        return order;
    }

    private void orderItem(Order order, Product product, int quantity) {
        LineItem lineItem = new LineItem();
        lineItem.product = product;
        lineItem.quantity = quantity;
        lineItem.unitPrice = product.price;
        lineItem.order = order;
        lineItem.persist();
        order.lineItems.add(lineItem);
    }

    private Checkout checkout(ShoppingCart cart, Order order, CheckoutStatus status, Instant completedAt) {
        Checkout checkout = new Checkout();
        checkout.shoppingCart = cart;
        checkout.order = order;
        checkout.status = status;
        checkout.createdAt = Instant.now();
        checkout.completedAt = completedAt;
        checkout.persist();
        if (order != null) {
            order.checkout = checkout;
        }
        return checkout;
    }

    private void shipping(
            Order order,
            String recipientName,
            String addressLine1,
            String city,
            String postalCode,
            String country,
            String carrier,
            String trackingNumber
    ) {
        Shipping shipping = new Shipping();
        shipping.order = order;
        shipping.recipientName = recipientName;
        shipping.addressLine1 = addressLine1;
        shipping.city = city;
        shipping.postalCode = postalCode;
        shipping.country = country;
        shipping.carrier = carrier;
        shipping.trackingNumber = trackingNumber;
        shipping.status = ShippingStatus.IN_TRANSIT;
        shipping.persist();
        order.shipping = shipping;
    }

    private void payment(
            Order order,
            Checkout checkout,
            String amount,
            PaymentMethod method,
            PaymentStatus status,
            String transactionId
    ) {
        Payment payment = new Payment();
        payment.order = order;
        payment.checkout = checkout;
        payment.amount = new BigDecimal(amount);
        payment.method = method;
        payment.status = status;
        payment.transactionId = transactionId;
        payment.paidAt = status == PaymentStatus.CAPTURED ? Instant.now() : null;
        payment.persist();
        order.payment = payment;
    }
}
