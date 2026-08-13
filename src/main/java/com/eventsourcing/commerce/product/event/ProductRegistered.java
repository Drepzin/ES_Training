package com.eventsourcing.commerce.product.event;

import com.eventsourcing.commerce.eventStore.DomainEvent;

import java.math.BigDecimal;

public record ProductRegistered(Long id, String name, BigDecimal price, String supplier) implements DomainEvent {
}
