package com.eventsourcing.commerce.product.event;

import com.eventsourcing.commerce.eventStore.DomainEvent;

import java.util.UUID;

public record StockReleased(UUID productId, Integer quantity) implements DomainEvent {
}
