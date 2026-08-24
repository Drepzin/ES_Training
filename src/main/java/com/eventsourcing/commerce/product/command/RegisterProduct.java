package com.eventsourcing.commerce.product.command;

import java.math.BigDecimal;
import java.util.UUID;

public record RegisterProduct(UUID productId, String name, BigDecimal unityValue, String supplier, String productType) {
}
