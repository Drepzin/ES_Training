package com.eventsourcing.commerce.product.event;

import com.eventsourcing.commerce.eventStore.DomainEvent;

import java.util.UUID;

public record StockReserved(UUID productId, Integer quantity) implements DomainEvent {
}
