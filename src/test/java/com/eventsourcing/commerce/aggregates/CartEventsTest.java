package com.eventsourcing.commerce.aggregates;

import com.eventsourcing.commerce.cart.Cart;
import com.eventsourcing.commerce.cart.command.CreateCart;
import com.eventsourcing.commerce.cart.event.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CartEventsTest {

    Cart cart;

    @BeforeEach
    void init(){
        UUID cartId = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();
        CartCreated cartCreated = new CartCreated(cartId, clientId);
        cart = Cart.reconstruct(List.of(cartCreated));
    }

    @Test
    @DisplayName("should create the cart")
    void shouldCreateTheCart(){
        UUID cartId = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();
        CartCreated cartCreated = new CartCreated(cartId, clientId);
        cart = Cart.reconstruct(List.of(cartCreated));

        assertEquals(cartId, cart.getCartId());
        assertEquals(clientId, cart.getClientId());
    }

    @Test
    @DisplayName("should add product to the cart")
    void shouldAddProductToCart(){
        UUID productId = UUID.randomUUID();
        int quantity = 10;
        ProductAddedInCart productAddedInCart = new ProductAddedInCart(cart.getCartId(), productId, quantity);
        cart.apply(productAddedInCart);

        assertFalse(cart.getProducts().isEmpty());
        assertEquals(quantity, cart.getProducts().get(productId));
    }

    @Test
    @DisplayName("should delete product from the cart")
    void shouldDeleteProductFromCart(){
        UUID productId = UUID.randomUUID();
        int quantity = 10;
        ProductAddedInCart productAddedInCart = new ProductAddedInCart(cart.getCartId(), productId, quantity);
        ProductDeletedFromCart productDeletedFromCart = new ProductDeletedFromCart(cart.getCartId(), productId, cart.getProducts().get(productId));

        cart.apply(productAddedInCart);
        cart.apply(productDeletedFromCart);

        assertTrue(cart.getProducts().isEmpty());
    }

    @Test
    @DisplayName("should remove product from the cart")
    void shouldRemoveProductFromCart(){
        UUID productId = UUID.randomUUID();
        int addQuantity = 10;
        int removedQuantity = 5;
        ProductAddedInCart productAddedInCart = new ProductAddedInCart(cart.getCartId(), productId, addQuantity);
        ProductRemovedFromCart productRemovedFromCart = new ProductRemovedFromCart(cart.getCartId(), productId, removedQuantity);

        cart.apply(productAddedInCart);
        cart.apply(productRemovedFromCart);

        assertFalse(cart.getProducts().isEmpty());
        assertEquals(addQuantity - removedQuantity, cart.getProducts().get(productId));
    }

    @Test
    @DisplayName("should expire product in the cart")
    void shouldExpireProductInCart(){
        UUID productId = UUID.randomUUID();
        int quantity = 10;
        ProductAddedInCart productAddedInCart = new ProductAddedInCart(cart.getCartId(), productId, quantity);
        ProductExpiredInCart productExpiredInCart = new ProductExpiredInCart(cart.getCartId(), productId, quantity);
        cart.apply(productAddedInCart);
        cart.apply(productExpiredInCart);

        assertTrue(cart.getProducts().isEmpty());
    }
}