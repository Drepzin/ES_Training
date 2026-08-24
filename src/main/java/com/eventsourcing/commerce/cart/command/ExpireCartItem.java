package com.eventsourcing.commerce.cart.command;

import java.util.UUID;

public record ExpireCartItem(UUID productId) {
}
