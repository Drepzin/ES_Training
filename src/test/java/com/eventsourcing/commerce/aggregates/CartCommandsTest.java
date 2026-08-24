package com.eventsourcing.commerce.aggregates;

import com.eventsourcing.commerce.cart.Cart;
import com.eventsourcing.commerce.cart.command.*;
import com.eventsourcing.commerce.eventStore.exception.InvalidAggregateValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CartCommandsTest {

    Cart cart;

    @BeforeEach
    void init(){
        UUID clientId = UUID.randomUUID();
        UUID cartId = UUID.randomUUID();
        CreateCart createCart = new CreateCart(cartId, clientId);
        cart = Cart.reconstruct(List.of());
        cart.handle(createCart);
        assertEquals(cartId, cart.getCartId());
        assertEquals(clientId, cart.getClientId());
        assertTrue(cart.getProducts().isEmpty());
    }

    @Test
    @DisplayName("should add product in cart correctly")
    void shouldAddItemInCart(){
        UUID productId = UUID.randomUUID();
        int quantity = 2;
        AddProductInCart addProductInCart = new AddProductInCart(productId, 2);

        cart.handle(addProductInCart);
        assertFalse(cart.getProducts().isEmpty());
        assertEquals(1, cart.getProducts().size());
        assertEquals(quantity, cart.getProducts().get(productId));
    }

    @Test
    @DisplayName("should not add product in cart when the quantity is zero or lower")
    void shouldNotAddItemInCartWhenQuantityIsZeroOrLower(){
        UUID productId = UUID.randomUUID();
        int quantity = 0;
        AddProductInCart addProductInCart = new AddProductInCart(productId, quantity);

        assertThrows(InvalidAggregateValue.class, () -> cart.handle(addProductInCart));
    }

    @Test
    @DisplayName("should not add product in cart when product id is null")
    void shouldNotAddItemInCartWhenProductIdIsNull(){
        UUID productId = null;
        int quantity = 10;
        AddProductInCart addProductInCart = new AddProductInCart(productId, quantity);

        assertThrows(InvalidAggregateValue.class, () -> cart.handle(addProductInCart));
    }

    @Test
    @DisplayName("should delete product from client cart correctly")
    void shouldDeleteProductInCart(){
        UUID productId = UUID.randomUUID();
        int quantity = 10;
        AddProductInCart addProductInCart = new AddProductInCart(productId, quantity);
        DeleteProductInCart deleteProductInCart = new DeleteProductInCart(productId);

        cart.handle(addProductInCart);
        cart.handle(deleteProductInCart);

        assertTrue(cart.getProducts().isEmpty());
    }

    @Test
    @DisplayName("should not delete product from client cart if product is not there")
    void shouldNotDeleteProductInCart(){
        UUID productId = UUID.randomUUID();
        DeleteProductInCart deleteProductInCart = new DeleteProductInCart(productId);

        assertThrows(InvalidAggregateValue.class, () -> cart.handle(deleteProductInCart));
    }

    @Test
    @DisplayName("should remove a quantity of product in client cart correctly")
    void shouldRemoveAQuantityOfProductInCart(){
        UUID productId = UUID.randomUUID();
        int quantityAdded = 5;
        int quantityRemoved = 2;

        AddProductInCart addProductInCart = new AddProductInCart(productId, quantityAdded);
        RemoveProductFromCart removeProductFromCart = new RemoveProductFromCart(productId, quantityRemoved);

        cart.handle(addProductInCart);
        cart.handle(removeProductFromCart);

        assertEquals(quantityAdded - quantityRemoved, cart.getProducts().get(productId));
    }

    @Test
    @DisplayName("should expire item in cart after 24hr correctly")
    void shouldExpireCartItem(){
        UUID productId = UUID.randomUUID();
        ExpireCartItem expireCartItem = new ExpireCartItem(productId);
        int quantity = 10;
        AddProductInCart addProductInCart = new AddProductInCart(productId, quantity);

        cart.handle(addProductInCart);
        cart.handle(expireCartItem);

        assertTrue(cart.getProducts().isEmpty());
    }

    @Test
    @DisplayName("should not expire cart item when product id is null")
    void shouldNotExpireCartItemWhenProductIdIsNull(){
        UUID nullProductId = null;
        UUID productId = UUID.randomUUID();
        int quantity = 10;
        AddProductInCart addProductInCart = new AddProductInCart(productId, quantity);
        ExpireCartItem expireCartItem = new ExpireCartItem(nullProductId);
        cart.handle(addProductInCart);

        assertThrows(InvalidAggregateValue.class, () -> cart.handle(expireCartItem));
    }
}
