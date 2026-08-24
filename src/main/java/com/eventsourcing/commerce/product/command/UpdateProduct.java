package com.eventsourcing.commerce.product.command;

import java.math.BigDecimal;
import java.util.UUID;

public record UpdateProduct(String name, BigDecimal unityValue) {
}
