package com.eventsourcing.commerce.product.controller.dto;

import java.math.BigDecimal;

public record RegisterProductRequest(String productName, BigDecimal unityValue, String supplier, String productType) {
}
