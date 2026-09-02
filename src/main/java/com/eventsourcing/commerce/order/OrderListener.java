package com.eventsourcing.commerce.order;

import com.eventsourcing.commerce.order.event.OrderCanceled;
import com.eventsourcing.commerce.order.event.OrderPaid;
import com.eventsourcing.commerce.order.event.OrderPlaced;
import com.eventsourcing.commerce.order.representation.OrderItemRepresentation;
import com.eventsourcing.commerce.order.representation.OrderItemRepresentationRepository;
import com.eventsourcing.commerce.order.representation.OrderRepresentation;
import com.eventsourcing.commerce.order.representation.OrderRepresentationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderListener {

    private final OrderRepresentationRepository orderRepresentationRepository;
    private final OrderItemRepresentationRepository orderItemRepresentationRepository;

    @EventListener
    public void listenOrderPlaced(OrderPlaced orderPlaced){
        OrderRepresentation orderRepresentation = new OrderRepresentation(orderPlaced.orderId(), orderPlaced.clientId(),
                orderPlaced.total(), OrderStatus.PENDING, "", "");
        orderRepresentationRepository.save(orderRepresentation);
        orderPlaced.cartItems().forEach(i -> orderItemRepresentationRepository.save(new OrderItemRepresentation(i.productId(), orderPlaced.orderId(), i.quantity())));
    }

    @EventListener
    public void listenOrderPaid(OrderPaid orderPaid){
        OrderRepresentation orderRepresentation = orderRepresentationRepository.findById(orderPaid.orderId()).orElseThrow(() -> new RuntimeException("Order dont exists"));
        orderRepresentation.payOrder(orderPaid.paymentMethod());
        orderRepresentationRepository.save(orderRepresentation);
    }

    @EventListener
    public void listenOrderCanceled(OrderCanceled orderCanceled){
        OrderRepresentation orderRepresentation = orderRepresentationRepository.findById(orderCanceled.orderId()).orElseThrow(() -> new RuntimeException("Order dont exists"));
        orderRepresentation.cancelOrder(orderCanceled.reason());
        orderRepresentationRepository.save(orderRepresentation);
    }
}
