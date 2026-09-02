package com.eventsourcing.commerce.order.representation;

import com.eventsourcing.commerce.order.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "order_representation")
@Data
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
public class OrderRepresentation {

    @Id
    private UUID id;

    @Column(name = "client_id", nullable = false)
    private UUID clientId;

    @Column(nullable = false)
    private BigDecimal total;

    @Column(nullable = false, name = "order_status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column
    private String reason;

    @Column(name = "payment_method")
    private String paymentMethod;

    public void payOrder(String paymentMethod){
        this.status = OrderStatus.PAID;
        this.paymentMethod = paymentMethod;
    }

    public void cancelOrder(String reason){
        this.status = OrderStatus.CANCELED;
        this.reason = reason;
    }
}
