package com.eventsourcing.commerce.order;

public record LoadedOrder(String streamId, Order order, int nextSequence) {
}
