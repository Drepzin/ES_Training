package com.eventsourcing.commerce.cart.command;

import java.util.UUID;

public record RemoveProductFromCart(UUID productId, Integer quantity) {
}
