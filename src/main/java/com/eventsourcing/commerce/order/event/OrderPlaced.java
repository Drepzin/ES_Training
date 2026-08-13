package com.eventsourcing.commerce.order.event;

import com.eventsourcing.commerce.eventStore.DomainEvent;

public record OrderPlaced() implements DomainEvent {
}
