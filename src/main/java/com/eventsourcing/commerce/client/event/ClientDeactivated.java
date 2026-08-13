package com.eventsourcing.commerce.client.event;

import com.eventsourcing.commerce.eventStore.DomainEvent;

import java.util.UUID;

public record ClientDeactivated(UUID clientId) implements DomainEvent {
}
