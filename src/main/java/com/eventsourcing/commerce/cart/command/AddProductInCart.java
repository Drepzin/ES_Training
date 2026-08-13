package com.eventsourcing.commerce.cart.command;

import java.util.UUID;

public record AddProductInCart(UUID productId, Integer quantity) {
}
