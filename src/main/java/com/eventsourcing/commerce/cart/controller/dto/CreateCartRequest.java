package com.eventsourcing.commerce.cart.controller.dto;

import java.util.UUID;

public record CreateCartRequest(UUID clientId) {
}
