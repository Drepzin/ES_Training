package com.eventsourcing.commerce.cart.event;

import com.eventsourcing.commerce.eventStore.DomainEvent;

import java.util.UUID;

public record ProductExpiredInCart(UUID cartId, UUID productId, Integer quantity) implements DomainEvent {
}
