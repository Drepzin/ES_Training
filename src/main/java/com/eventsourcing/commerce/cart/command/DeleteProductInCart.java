package com.eventsourcing.commerce.cart.command;

import java.util.UUID;

public record DeleteProductInCart(UUID cartId, UUID productId) {
}
