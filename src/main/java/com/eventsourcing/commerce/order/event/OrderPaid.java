package com.eventsourcing.commerce.order.event;

import com.eventsourcing.commerce.eventStore.DomainEvent;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrderPaid(UUID orderId, String paymentMethod) implements DomainEvent {
}
