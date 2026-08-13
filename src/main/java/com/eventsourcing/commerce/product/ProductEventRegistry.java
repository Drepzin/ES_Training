package com.eventsourcing.commerce.product;

import com.eventsourcing.commerce.eventStore.EventType;
import com.eventsourcing.commerce.eventStore.EventTypeRegistry;
import com.eventsourcing.commerce.product.event.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductEventRegistry {

    private final EventTypeRegistry eventTypeRegistry;

    @PostConstruct
    public void registryEvent(){
        eventTypeRegistry.register(EventType.PRODUCT_REGISTERED, ProductRegistered.class);
        eventTypeRegistry.register(EventType.PRODUCT_DISCONTINUED, ProductDiscontinued.class);
        eventTypeRegistry.register(EventType.PRODUCT_UPDATED, ProductUpdated.class);
        eventTypeRegistry.register(EventType.STOCK_ADDED, StockAdded.class);
        eventTypeRegistry.register(EventType.STOCK_DEDUCTED, StockDeducted.class);
        eventTypeRegistry.register(EventType.STOCK_RELEASED, StockReleased.class);
        eventTypeRegistry.register(EventType.STOCK_RESERVED, StockReserved.class);
    }
}
