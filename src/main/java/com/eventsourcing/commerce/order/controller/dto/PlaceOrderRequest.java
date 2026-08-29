package com.eventsourcing.commerce.order.controller.dto;

import com.eventsourcing.commerce.order.OrderStatus;
import com.eventsourcing.commerce.order.vo.CartItem;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record PlaceOrderRequest(UUID clientId, List<CartItem> cartItems, BigDecimal total, String paymentMethod) {
}
