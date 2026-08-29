package com.eventsourcing.commerce.order;

import com.eventsourcing.commerce.eventStore.DomainEvent;
import com.eventsourcing.commerce.eventStore.EventStore;
import com.eventsourcing.commerce.eventStore.EventType;
import com.eventsourcing.commerce.eventStore.utils.AggregateSequence;
import com.eventsourcing.commerce.eventStore.utils.AggregatorReconstructor;
import com.eventsourcing.commerce.order.command.CancelOrder;
import com.eventsourcing.commerce.order.command.PayOrder;
import com.eventsourcing.commerce.order.command.PlaceOrder;
import com.eventsourcing.commerce.order.controller.dto.PlaceOrderRequest;
import com.eventsourcing.commerce.product.LoadedProduct;
import com.eventsourcing.commerce.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final EventStore eventStore;
    private final AggregatorReconstructor aggregatorReconstructor;

    public void placeOrder(PlaceOrderRequest placeOrderRequest){
        Order order = new Order();
        UUID orderId = UUID.randomUUID();
        PlaceOrder placeOrder = new PlaceOrder(orderId, placeOrderRequest.clientId(), placeOrderRequest.cartItems(), placeOrderRequest.total());
        DomainEvent domainEvent = order.handle(placeOrder);
        String streamId = "order-" + orderId;
        persist(streamId, 0, EventType.ORDER_PAID, domainEvent);
    }

    public void paidOrder(PayOrder payOrder, UUID orderId){
        LoadedOrder loadedOrder = load(orderId);
        DomainEvent domainEvent = loadedOrder.order().handle(payOrder);
        persist(loadedOrder.streamId(), loadedOrder.nextSequence(), EventType.ORDER_PAID, domainEvent);
    }

    public void cancelOrder(CancelOrder cancelOrder, UUID orderId){
        LoadedOrder loadedOrder = load(orderId);
        DomainEvent domainEvent = loadedOrder.order().handle(cancelOrder);
        persist(loadedOrder.streamId(), loadedOrder.nextSequence(), EventType.ORDER_CANCELED, domainEvent);
    }

    private void persist(String streamId, int sequence, EventType eventType, DomainEvent payload) {
        eventStore.saveEvent(streamId, sequence, UUID.randomUUID(), UUID.randomUUID(), eventType, payload);
    }


    private LoadedOrder load(UUID productId){
        String streamId = "product-" + productId;
        AggregateSequence<Order> productAggregateSequence = aggregatorReconstructor.reconstructAggregate(streamId, Order::reconstruct);
        Order order = productAggregateSequence.getAggregate();
        int nextSequence = productAggregateSequence.getSequence();
        return new LoadedOrder(streamId, order, nextSequence);
    }
}
