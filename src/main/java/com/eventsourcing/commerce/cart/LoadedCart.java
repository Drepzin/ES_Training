package com.eventsourcing.commerce.cart;

public record LoadedCart(String streamId, Cart cart, int nextSequence) {
}
