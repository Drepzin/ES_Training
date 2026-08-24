package com.eventsourcing.commerce.product.event;

import com.eventsourcing.commerce.eventStore.DomainEvent;

import java.util.UUID;

public record ProductDiscontinued(UUID productId) implements DomainEvent {
}
