package com.eventsourcing.commerce.order;

import com.eventsourcing.commerce.eventStore.EventType;
import com.eventsourcing.commerce.eventStore.EventTypeRegistry;
import com.eventsourcing.commerce.order.event.OrderCanceled;
import com.eventsourcing.commerce.order.event.OrderPaid;
import com.eventsourcing.commerce.order.event.OrderPlaced;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventRegistry {

    private final EventTypeRegistry eventTypeRegistry;

    @PostConstruct
    public void registryEvent(){
        eventTypeRegistry.register(EventType.ORDER_CANCELED, OrderCanceled.class);
        eventTypeRegistry.register(EventType.ORDER_PAID, OrderPaid.class);
        eventTypeRegistry.register(EventType.ORDER_PLACED, OrderPlaced.class);
    }
}
