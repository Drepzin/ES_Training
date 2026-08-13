package com.eventsourcing.commerce.cart.command;

import java.util.UUID;

public record CreateCart(UUID cartId, UUID clientId) {
}
