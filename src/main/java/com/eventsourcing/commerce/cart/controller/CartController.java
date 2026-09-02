package com.eventsourcing.commerce.cart.controller;

import com.eventsourcing.commerce.cart.CartService;
import com.eventsourcing.commerce.cart.command.*;
import com.eventsourcing.commerce.cart.controller.dto.CreateCartRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<Void> createCart(@RequestBody CreateCartRequest createCartRequest){
        cartService.createCart(createCartRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("{cartId}/products")
    public ResponseEntity<Void> addProductInCart(@PathVariable UUID cartId, @RequestBody AddProductInCart addProductInCart){
        cartService.addProductInCart(addProductInCart, cartId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{cartId}/products")
    public ResponseEntity<Void> deleteProductInCart(@PathVariable UUID cartId, @RequestBody DeleteProductInCart deleteProductInCart){
        cartService.deleteProductInCart(deleteProductInCart, cartId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("{cartId}/products/expire")
    public ResponseEntity<Void> expireCartItem(@PathVariable UUID cartId, @RequestBody ExpireCartItem expireCartItem){
        cartService.expireCartItem(expireCartItem, cartId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("{cartId}/products/remove")
    public ResponseEntity<Void> removeProductFromCart(@PathVariable UUID cartId, @RequestBody RemoveProductFromCart removeProductFromCart){
        cartService.removeProductFromCart(removeProductFromCart, cartId);
        return ResponseEntity.ok().build();
    }
}
