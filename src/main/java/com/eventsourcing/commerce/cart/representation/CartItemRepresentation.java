package com.eventsourcing.commerce.cart.representation;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "cart_item_representation")
@Data
@NoArgsConstructor
public class CartItemRepresentation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, name = "cart_id")
    private UUID cartId;

    @Column(nullable = false, name = "product_id")
    private UUID productId;

    @Column(nullable = false)
    private int quantity;

    public CartItemRepresentation(UUID cartId, UUID productId, int quantity){
        this.cartId = cartId;
        this.productId = productId;
        this.quantity = quantity;
    }
}
