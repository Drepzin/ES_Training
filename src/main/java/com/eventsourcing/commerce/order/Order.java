package com.eventsourcing.commerce.order;

import com.eventsourcing.commerce.eventStore.DomainEvent;
import com.eventsourcing.commerce.eventStore.exception.InvalidAggregateValue;
import com.eventsourcing.commerce.order.command.CancelOrder;
import com.eventsourcing.commerce.order.command.PayOrder;
import com.eventsourcing.commerce.order.command.PlaceOrder;
import com.eventsourcing.commerce.order.event.OrderCanceled;
import com.eventsourcing.commerce.order.event.OrderPaid;
import com.eventsourcing.commerce.order.event.OrderPlaced;
import com.eventsourcing.commerce.order.vo.CartItem;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.util.*;

@Getter
@EqualsAndHashCode(of = "orderId")
public class Order {

    private UUID orderId;
    private UUID clientId;
    private List<CartItem> cartItems;
    private BigDecimal total;
    private OrderStatus orderStatus;
    private String reason;
    private String paymentMethod;

    public Order(){}

    public static Order reconstruct(List<DomainEvent> events){
        Order order = new Order();
        for(DomainEvent e : events){
            order.apply(e);
        }
        return order;
    }

    public void apply(DomainEvent event){
        switch (event){
            case OrderCanceled e -> this.apply(e);
            case OrderPaid e -> this.apply(e);
            case OrderPlaced e -> this.apply(e);
            default -> throw new IllegalArgumentException("Invalid aggregate operation");
        }
    }

    private void apply(OrderCanceled orderCanceled){
        this.orderStatus = OrderStatus.CANCELED;
        this.reason = orderCanceled.reason();
    }

    private void apply(OrderPaid orderPaid){
        this.orderStatus = OrderStatus.PAID;
        this.paymentMethod = orderPaid.paymentMethod();
    }

    private void apply(OrderPlaced orderPlaced){
        this.orderId = orderPlaced.orderId();
        this.clientId = orderPlaced.clientId();
        this.cartItems = new ArrayList<>();
        cartItems.addAll(orderPlaced.cartItems());
        this.orderStatus = OrderStatus.PENDING;
        this.total = orderPlaced.total();
    }

    public DomainEvent handle(CancelOrder cancelOrder){
        if(this.orderId == null)throw new InvalidAggregateValue("Inexistent Order!");
        if(this.orderStatus == OrderStatus.CANCELED)throw new InvalidAggregateValue("Order already canceled!");
        if(this.orderStatus == OrderStatus.PAID)throw new InvalidAggregateValue("Paid order cannot be canceled, only refunded!");
        OrderCanceled orderCanceled = new OrderCanceled(this.orderId, cancelOrder.reason());
        this.apply(orderCanceled);
        return orderCanceled;
    }

    public DomainEvent handle(PlaceOrder placeOrder){
        if(this.orderId != null)throw new InvalidAggregateValue("Order already initializated");
        UUID orderId = placeOrder.orderId();
        UUID clientId = placeOrder.clientId();
        List<CartItem> cartItems = placeOrder.cartItems();
        BigDecimal total = placeOrder.total();
        validatePlaceOrderFields(orderId, clientId, cartItems, total);
        OrderPlaced orderPlaced = new OrderPlaced(orderId, clientId, cartItems, total);
        this.apply(orderPlaced);
        return orderPlaced;
    }

    public DomainEvent handle(PayOrder payOrder){
        String paymentMethod = payOrder.paymentMethod();
        if(payOrder.paymentMethod() == null)throw new InvalidAggregateValue("payment method cannot be null");
        validatePayOrder(paymentMethod);
        OrderPaid orderPaid = new OrderPaid(this.orderId, paymentMethod);
        this.apply(orderPaid);
        return orderPaid;
    }

    private void validatePlaceOrderFields(UUID orderId, UUID clientId, List<CartItem> cartItems, BigDecimal total){
        if(orderId == null)throw new InvalidAggregateValue("Order id in Place order cannot be null");
        if(clientId == null)throw new InvalidAggregateValue("Client id in Place order cannot be null");
        if(cartItems.isEmpty())throw new InvalidAggregateValue("Order cannot have a empty item list");
        if(total.doubleValue() <= 0)throw new InvalidAggregateValue("Order total cannot be 0 or loewr");
    }

    private void validatePayOrder(String paymentMethod){
        if(this.orderId == null)throw new InvalidAggregateValue("Order already not initialized");
        if(this.orderStatus == OrderStatus.PAID)throw new InvalidAggregateValue("Order already paid");
        if(this.orderStatus == OrderStatus.CANCELED)throw new InvalidAggregateValue("Cannot pay a canceled order");
        if(this.total.doubleValue() <= 0)throw new InvalidAggregateValue("Order total cannot be 0 or lower");
        if(paymentMethod.isBlank())throw new InvalidAggregateValue("Payment method cannot be null");
    }

}
