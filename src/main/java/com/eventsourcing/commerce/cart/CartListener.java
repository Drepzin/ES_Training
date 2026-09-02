package com.eventsourcing.commerce.cart;

import com.eventsourcing.commerce.cart.event.CartCreated;
import com.eventsourcing.commerce.cart.event.ProductAddedInCart;
import com.eventsourcing.commerce.cart.event.ProductExpiredInCart;
import com.eventsourcing.commerce.cart.event.ProductRemovedFromCart;
import com.eventsourcing.commerce.cart.representation.CartRepresentationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CartListener {

    private final CartRepresentationRepository cartRepresentationRepository;

    public void listenCartCreated(CartCreated cartCreated){

    }

    public void listenProductAddedInCart(ProductAddedInCart productAddedInCart){

    }

    public void listenProductDeletedFromCart(){

    }

    public void listenProductExpiredInCart(ProductExpiredInCart productExpiredInCart){

    }

    public void listenProductRemovedFromCart(ProductRemovedFromCart productRemovedFromCart){

    }

}
