package com.eventsourcing.commerce.product.event;

import com.eventsourcing.commerce.eventStore.DomainEvent;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductRegistered(UUID productId, String name, BigDecimal unityValue, String supplier, String productType) implements DomainEvent {
}
