package com.eventsourcing.commerce.order;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@EqualsAndHashCode(of = "orderId")
public class Order {

    private UUID orderId;
    private Map<UUID, Integer> products;
    private BigDecimal total;


}
