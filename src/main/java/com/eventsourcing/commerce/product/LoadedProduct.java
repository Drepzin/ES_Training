package com.eventsourcing.commerce.product;

public record LoadedProduct(String streamId, Product product, int nextSequence) {
}
