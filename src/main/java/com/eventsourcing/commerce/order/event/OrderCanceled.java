package com.eventsourcing.commerce.order.event;

import com.eventsourcing.commerce.eventStore.DomainEvent;
import com.eventsourcing.commerce.order.vo.CartItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderCanceled(UUID orderId, String reason) implements DomainEvent {
}
