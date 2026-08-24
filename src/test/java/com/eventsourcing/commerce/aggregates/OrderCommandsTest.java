package com.eventsourcing.commerce.aggregates;

import com.eventsourcing.commerce.eventStore.exception.InvalidAggregateValue;
import com.eventsourcing.commerce.order.Order;
import com.eventsourcing.commerce.order.OrderStatus;
import com.eventsourcing.commerce.order.command.CancelOrder;
import com.eventsourcing.commerce.order.command.PayOrder;
import com.eventsourcing.commerce.order.command.PlaceOrder;
import com.eventsourcing.commerce.order.vo.CartItem;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

public class OrderCommandsTest {

    Order order;

    @BeforeEach
    void init(){
        order = Order.reconstruct(List.of());
        UUID orderId = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();
        List<CartItem> items = new ArrayList<>();
        items.add(new CartItem(UUID.randomUUID(), 4));
        items.add(new CartItem(UUID.randomUUID(), 2));
        items.add(new CartItem(UUID.randomUUID(), 3));
        PlaceOrder placeOrder = new PlaceOrder(orderId, clientId, items, BigDecimal.valueOf(200));
        order.handle(placeOrder);
    }

    @Test
    @DisplayName("should cancel order")
    void shouldCancelOrder(){
        String reason = "too much pauper";
        CancelOrder cancelOrder = new CancelOrder(reason);
        order.handle(cancelOrder);

        assertEquals(OrderStatus.CANCELED, order.getOrderStatus());
    }

    @Test
    @DisplayName("should not cancel order if order is already canceled")
    void shouldNotCancelOrderIfOrderAlreadyCanceled(){
        String reason = "too much pauper";
        CancelOrder cancelOrder = new CancelOrder(reason);
        String otherReason = "too much expansive";
        CancelOrder anotherCancel = new CancelOrder(otherReason);
        order.handle(cancelOrder);

        assertThrows(InvalidAggregateValue.class, () -> order.handle(anotherCancel));
        assertEquals(reason, order.getReason());
    }

    @Test
    @DisplayName("should not cancel order if order is paid")
    void shouldNotCancelOrderIfOrderIsPaid(){
        String paymentMethod = "PIX";
        PayOrder payOrder = new PayOrder(paymentMethod);
        order.handle(payOrder);

        String reason = "too beautiful";
        CancelOrder cancelOrder = new CancelOrder(reason);

        assertThrows(InvalidAggregateValue.class, () -> order.handle(cancelOrder));
        assertEquals(OrderStatus.PAID, order.getOrderStatus());
        assertNull(order.getReason());
    }

    @Test
    @DisplayName("should pay order")
    void shouldPayOrder(){
        String paymentMethod = "PIX";
        PayOrder payOrder = new PayOrder(paymentMethod);
        order.handle(payOrder);

        assertEquals(OrderStatus.PAID, order.getOrderStatus());
        assertEquals(paymentMethod, order.getPaymentMethod());
    }

    @Test
    @DisplayName("should not pay order if order is not initialized")
    void shouldNotPayOrderIfNotInitialized(){
        Order order = Order.reconstruct(List.of());
        String paymentMethod = "PIX";
        PayOrder payOrder = new PayOrder(paymentMethod);

        assertThrows(InvalidAggregateValue.class, () -> order.handle(payOrder));
    }

    @Test
    @DisplayName("should not pay order if is canceled")
    void shouldNotPayOrderIfIsCanceled(){
        String reason = "too much java...";
        CancelOrder cancelOrder = new CancelOrder(reason);
        String paymentMethod = "PIX";
        PayOrder payOrder = new PayOrder(paymentMethod);
        order.handle(cancelOrder);

        assertThrows(InvalidAggregateValue.class, () -> order.handle(payOrder));
    }

    @Test
    @DisplayName("should not place order if total is zero or lower")
    void shouldNotPlaceOrderIfTotalIsZeroOrLower(){
        Order order;
        order = Order.reconstruct(List.of());
        UUID orderId = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();
        List<CartItem> items = new ArrayList<>();
        items.add(new CartItem(UUID.randomUUID(), 4));
        items.add(new CartItem(UUID.randomUUID(), 2));
        items.add(new CartItem(UUID.randomUUID(), 3));
        PlaceOrder placeOrder = new PlaceOrder(orderId, clientId, items, BigDecimal.valueOf(-100));

        assertThrows(InvalidAggregateValue.class, () -> order.handle(placeOrder));
    }

    @Test
    @DisplayName("should not pay order if payment method is missing")
    void shouldNotPayOrderIfPaymentMethodIsBlank(){
        String paymentMethod = "";
        PayOrder payOrder = new PayOrder(paymentMethod);

        assertThrows(InvalidAggregateValue.class, () -> order.handle(payOrder));
    }

    @Test
    @DisplayName("should not pay order if payment method is null")
    void shouldNotPayOrderIfPaymentMethodIsNull(){
        String paymentMethod = null;
        PayOrder payOrder = new PayOrder(paymentMethod);

        assertThrows(InvalidAggregateValue.class, () -> order.handle(payOrder));
    }
}
