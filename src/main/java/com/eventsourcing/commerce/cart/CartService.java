package com.eventsourcing.commerce.cart;

import com.eventsourcing.commerce.cart.command.*;
import com.eventsourcing.commerce.cart.controller.dto.CreateCartRequest;
import com.eventsourcing.commerce.eventStore.DomainEvent;
import com.eventsourcing.commerce.eventStore.EventStore;
import com.eventsourcing.commerce.eventStore.EventType;
import com.eventsourcing.commerce.eventStore.utils.AggregateSequence;
import com.eventsourcing.commerce.eventStore.utils.AggregatorReconstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {

    private final EventStore eventStore;
    private final AggregatorReconstructor aggregatorReconstructor;
    private final ApplicationEventPublisher applicationEventPublisher;

    public void createCart(CreateCartRequest createCartRequest){
        Cart cart = new Cart();
        UUID cartId = UUID.randomUUID();
        CreateCart createCart = new CreateCart(cartId, createCartRequest.clientId());
        DomainEvent domainEvent = cart.handle(createCart);
        String streamId = "cart-" + cartId;
        persist(streamId, 0, EventType.CART_CREATED, domainEvent);
        applicationEventPublisher.publishEvent(domainEvent);
    }

    public void addProductInCart(AddProductInCart addProductInCart, UUID cartId){
        LoadedCart loadedCart = load(cartId);
        DomainEvent domainEvent = loadedCart.cart().handle(addProductInCart);
        persist(loadedCart.streamId(), loadedCart.nextSequence(), EventType.PRODUCT_ADDED_IN_CART, domainEvent);
        applicationEventPublisher.publishEvent(domainEvent);
    }

    public void deleteProductInCart(DeleteProductInCart deleteProductInCart, UUID cartId){
        LoadedCart loadedCart = load(cartId);
        DomainEvent domainEvent = loadedCart.cart().handle(deleteProductInCart);
        persist(loadedCart.streamId(), loadedCart.nextSequence(), EventType.PRODUCT_DELETED_FROM_CART, domainEvent);
        applicationEventPublisher.publishEvent(domainEvent);
    }

    public void expireCartItem(ExpireCartItem expireCartItem, UUID cartId){
        LoadedCart loadedCart = load(cartId);
        DomainEvent domainEvent = loadedCart.cart().handle(expireCartItem);
        persist(loadedCart.streamId(), loadedCart.nextSequence(), EventType.CART_ITEM_EXPIRED, domainEvent);
        applicationEventPublisher.publishEvent(domainEvent);
    }

    public void removeProductFromCart(RemoveProductFromCart removeProductFromCart, UUID cartId){
        LoadedCart loadedCart = load(cartId);
        DomainEvent domainEvent = loadedCart.cart().handle(removeProductFromCart);
        persist(loadedCart.streamId(), loadedCart.nextSequence(), EventType.PRODUCT_REMOVED_FROM_CART, domainEvent);
        applicationEventPublisher.publishEvent(domainEvent);
    }

    private LoadedCart load(UUID cartId){
        String streamId = "cart-" + cartId;
        AggregateSequence<Cart> cartAggregateSequence = aggregatorReconstructor.reconstructAggregate(streamId, Cart::reconstruct);
        Cart cart = cartAggregateSequence.getAggregate();
        int nextSequence = cartAggregateSequence.getSequence();
        return new LoadedCart(streamId, cart, nextSequence);
    }

    private void persist(String streamId, int sequence, EventType eventType, DomainEvent payload){
        eventStore.saveEvent(streamId, sequence, UUID.randomUUID(), UUID.randomUUID(), eventType, payload);
    }
}
