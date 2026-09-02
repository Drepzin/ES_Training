package com.eventsourcing.commerce.order.controller;

import com.eventsourcing.commerce.order.OrderService;
import com.eventsourcing.commerce.order.command.CancelOrder;
import com.eventsourcing.commerce.order.command.PayOrder;
import com.eventsourcing.commerce.order.controller.dto.PlaceOrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Void> placeOrder(@RequestBody PlaceOrderRequest placeOrderRequest){
        orderService.placeOrder(placeOrderRequest);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("{orderId}/pay")
    public ResponseEntity<Void> paidOrder(@PathVariable UUID orderId, @RequestBody PayOrder payOrder){
        orderService.paidOrder(payOrder, orderId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable UUID orderId, @RequestBody CancelOrder cancelOrder){
        orderService.cancelOrder(cancelOrder, orderId);
        return ResponseEntity.ok().build();
    }
}
