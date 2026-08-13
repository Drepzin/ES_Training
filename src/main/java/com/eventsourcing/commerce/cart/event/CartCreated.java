package com.eventsourcing.commerce.cart.event;

import com.eventsourcing.commerce.eventStore.DomainEvent;

import java.util.UUID;

public record CartCreated(UUID cartId, UUID clientId) implements DomainEvent {
}
