package com.eventsourcing.commerce.client.event;

import com.eventsourcing.commerce.eventStore.DomainEvent;

import java.util.UUID;

public record ClientUpdated(UUID clientId, String firstName, String lastName, String email, String password) implements DomainEvent {
}
