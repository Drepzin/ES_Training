package com.eventsourcing.commerce.product.event;

import com.eventsourcing.commerce.eventStore.DomainEvent;

public record ProductDiscontinued() implements DomainEvent {
}
