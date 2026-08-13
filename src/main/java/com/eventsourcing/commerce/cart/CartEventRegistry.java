package com.eventsourcing.commerce.cart;

import com.eventsourcing.commerce.cart.event.*;
import com.eventsourcing.commerce.eventStore.EventType;
import com.eventsourcing.commerce.eventStore.EventTypeRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CartEventRegistry {

    private final EventTypeRegistry eventTypeRegistry;

    @PostConstruct
    public void registryEvents(){
        eventTypeRegistry.register(EventType.CART_CREATED, CartCreated.class);
        eventTypeRegistry.register(EventType.PRODUCT_ADDED_IN_CART, ProductAddedInCart.class);
        eventTypeRegistry.register(EventType.PRODUCT_REMOVED_FROM_CART, ProductRemovedFromCart.class);
        eventTypeRegistry.register(EventType.PRODUCT_UPDATED_IN_CART, ProductDeletedFromCart.class);
    }
}
