package com.eventsourcing.commerce.order.vo;


import java.util.UUID;

public record CartItem(UUID productId, Integer quantity) {
}
