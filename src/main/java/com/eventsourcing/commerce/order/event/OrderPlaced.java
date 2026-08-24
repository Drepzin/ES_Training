package com.eventsourcing.commerce.order.event;

import com.eventsourcing.commerce.eventStore.DomainEvent;
import com.eventsourcing.commerce.order.vo.CartItem;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record OrderPlaced(UUID orderId, UUID clientId, List<CartItem> cartItems, BigDecimal total) implements DomainEvent {
}
