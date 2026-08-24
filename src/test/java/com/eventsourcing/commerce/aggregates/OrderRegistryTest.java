package com.eventsourcing.commerce.aggregates;

import com.eventsourcing.commerce.order.Order;
import com.eventsourcing.commerce.order.command.PlaceOrder;
import com.eventsourcing.commerce.order.vo.CartItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderRegistryTest {

    @Test
    @DisplayName("should create the order")
    void shouldRegisterTheOrder(){
        UUID orderId = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();
        List<CartItem> items = new ArrayList<>();
        items.add(new CartItem(UUID.randomUUID(), 4));
        items.add(new CartItem(UUID.randomUUID(), 2));
        items.add(new CartItem(UUID.randomUUID(), 3));
        Order order = Order.reconstruct(List.of());
        PlaceOrder placeOrder = new PlaceOrder(orderId, clientId, items, BigDecimal.valueOf(200));
        order.handle(placeOrder);

        assertEquals(orderId, order.getOrderId());
        assertEquals(clientId, order.getClientId());
        assertEquals(3, order.getCartItems().size());
        assertEquals(BigDecimal.valueOf(200), order.getTotal());
    }
}
