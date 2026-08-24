package com.eventsourcing.commerce.aggregates;

import com.eventsourcing.commerce.cart.Cart;
import com.eventsourcing.commerce.cart.command.CreateCart;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CartRegistryTest {

    @Test
    @DisplayName("should registry correctly the cart")
    void shouldRegistryTheCart(){
        UUID clientId = UUID.randomUUID();
        UUID cartId = UUID.randomUUID();
        CreateCart createCart = new CreateCart(cartId, clientId);
        Cart cart = Cart.reconstruct(List.of());
        cart.handle(createCart);
        assertEquals(cartId, cart.getCartId());
        assertEquals(clientId, cart.getClientId());
        assertTrue(cart.getProducts().isEmpty());
    }
}
