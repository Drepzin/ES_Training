package com.eventsourcing.commerce.order.command;

import com.eventsourcing.commerce.order.vo.CartItem;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record PlaceOrder(UUID orderId, UUID clientId, List<CartItem> cartItems, BigDecimal total) {
}