package com.eventsourcing.commerce.product.event;

import com.eventsourcing.commerce.eventStore.DomainEvent;

public record StockAdded() implements DomainEvent {
}
