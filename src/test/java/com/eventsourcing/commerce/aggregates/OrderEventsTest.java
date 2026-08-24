package com.eventsourcing.commerce.aggregates;

import com.eventsourcing.commerce.order.Order;
import com.eventsourcing.commerce.order.OrderStatus;
import com.eventsourcing.commerce.order.event.OrderCanceled;
import com.eventsourcing.commerce.order.event.OrderPaid;
import com.eventsourcing.commerce.order.event.OrderPlaced;
import com.eventsourcing.commerce.order.vo.CartItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

public class OrderEventsTest {

    Order order;

    @BeforeEach
    void init(){
        UUID orderId = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();
        List<CartItem> items = new ArrayList<>();
        items.add(new CartItem(UUID.randomUUID(), 4));
        items.add(new CartItem(UUID.randomUUID(), 2));
        items.add(new CartItem(UUID.randomUUID(), 3));
        BigDecimal total = BigDecimal.valueOf(200);
        OrderPlaced orderPlaced = new OrderPlaced(orderId, clientId, items, total);
        order = Order.reconstruct(List.of(orderPlaced));
    }

    @Test
    @DisplayName("should place order")
    void shouldPlaceOrder(){
        assertNotNull(order.getOrderId());
        assertNotNull(order.getClientId());
        assertNotNull(order.getTotal());
        assertFalse(order.getCartItems().isEmpty());
    }

    @Test
    @DisplayName("should pay order")
    void shouldPayOrder(){
        UUID orderId = order.getOrderId();
        OrderPaid orderPaid = new OrderPaid(orderId, "pix");
        order.apply(orderPaid);

        assertEquals(OrderStatus.PAID, order.getOrderStatus());
    }

    @Test
    @DisplayName("should cancel order")
    void shouldCancelOrder(){
        UUID orderId = order.getOrderId();
        String reason = "too much expansive for my budget";
        OrderCanceled orderCanceled = new OrderCanceled(orderId, reason);
        order.apply(orderCanceled);

        assertEquals(OrderStatus.CANCELED, order.getOrderStatus());
    }
}
