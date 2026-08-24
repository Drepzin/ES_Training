package com.eventsourcing.commerce.cart;

import com.eventsourcing.commerce.cart.command.*;
import com.eventsourcing.commerce.cart.event.*;
import com.eventsourcing.commerce.eventStore.DomainEvent;
import com.eventsourcing.commerce.eventStore.exception.InvalidAggregateValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@EqualsAndHashCode(of = "cartId")
public class Cart {

    private UUID cartId;
    private UUID clientId;
    private Map<UUID, Integer> products = new ConcurrentHashMap<>();

    public Cart(){}

    public void apply(DomainEvent domainEvent){
        switch (domainEvent){
            case CartCreated e -> apply(e);
            case ProductAddedInCart e -> apply(e);
            case ProductRemovedFromCart e -> apply(e);
            case ProductDeletedFromCart e -> apply(e);
            case ProductExpiredInCart e -> apply(e);
            default -> throw new IllegalStateException("Unexpected value: " + domainEvent);
        }
    }

    public static Cart reconstruct(List<DomainEvent> events){
        Cart cart = new Cart();
        for(DomainEvent e : events){
            cart.apply(e);
        }
        return cart;
    }

    private void apply(CartCreated cartCreated){
        this.cartId = cartCreated.cartId();
        this.clientId = cartCreated.clientId();
    }

    private void apply(ProductAddedInCart productAddedInCart){
        products.merge(
                productAddedInCart.productId(),
                productAddedInCart.quantity(),
                Integer::sum
        );
    }

    private void apply(ProductRemovedFromCart productRemovedFromCart){
        UUID productId = productRemovedFromCart.productId();
        Integer oldQuantity = products.get(productId);
        if(oldQuantity == null)return;
        int newQuantity = oldQuantity - productRemovedFromCart.quantity();
        if(newQuantity <= 0){
            products.remove(productId);
        }
        else{
            products.replace(productId, oldQuantity, newQuantity);
        }
    }

    private void apply(ProductDeletedFromCart productDeletedFromCart){
        products.remove(productDeletedFromCart.productId());
    }

    private void apply(ProductExpiredInCart productExpiredInCart){
        UUID productId = productExpiredInCart.productId();
        products.remove(productId);
    }

    public Map<UUID, Integer> getProducts(){
        return Collections.unmodifiableMap(products);
    }

    public DomainEvent handle(CreateCart createCart){
        UUID cartId = createCart.cartId();
        UUID clientId = createCart.clientId();
        validateCartFields(cartId, clientId);
        CartCreated cartCreated = new CartCreated(cartId, clientId);
        this.apply(cartCreated);
        return cartCreated;
    }

    public DomainEvent handle(AddProductInCart addProductInCart){
        UUID productId = addProductInCart.productId();
        Integer quantity = addProductInCart.quantity();
        validateCartFields(this.cartId, this.clientId);
        validateProductFields(productId, quantity);
        ProductAddedInCart productAddedInCart = new ProductAddedInCart(cartId, productId, quantity);
        this.apply(productAddedInCart);
        return productAddedInCart;
    }

    public DomainEvent handle(RemoveProductFromCart removeProductFromCart){
        UUID productId = removeProductFromCart.productId();
        Integer quantity = removeProductFromCart.quantity();
        validateCartFields(this.cartId, this.clientId);
        validateProductFields(productId, quantity);
        ProductRemovedFromCart productRemovedFromCart = new ProductRemovedFromCart(cartId, productId, quantity);
        this.apply(productRemovedFromCart);
        return productRemovedFromCart;
    }

    public DomainEvent handle(DeleteProductInCart deleteProductInCart){
        UUID productId = deleteProductInCart.productId();
        validateDeleteProductFromCart(productId);
        ProductDeletedFromCart productDeletedFromCart = new ProductDeletedFromCart(this.cartId, productId, products.get(productId));
        this.apply(productDeletedFromCart);
        return productDeletedFromCart;
    }

    public DomainEvent handle(ExpireCartItem expireCartItem){
        UUID productId = expireCartItem.productId();
        validateCartFields(cartId, this.clientId);
        if(productId == null)throw new InvalidAggregateValue("Product id cannot be null");
        ProductExpiredInCart productExpiredInCart = new ProductExpiredInCart(cartId, productId, products.get(productId));
        this.apply(productExpiredInCart);
        return productExpiredInCart;
    }

    private void validateCartFields(UUID cartId, UUID clientId){
        if(cartId == null)throw new InvalidAggregateValue("Cart id cannot be null");
        if(clientId == null)throw new InvalidAggregateValue("Client id cannot be null");
    }

    private void validateProductFields(UUID productId, Integer quantity){
        if(productId == null)throw new InvalidAggregateValue("Product id cannot be null");
        if(quantity == null || quantity <= 0)throw new InvalidAggregateValue("Invalid product quantity must be higher than zero");
    }

    private void validateDeleteProductFromCart(UUID productId){
        if(this.cartId == null)throw new InvalidAggregateValue("Cart id cannot be null");
        if(productId == null)throw new InvalidAggregateValue("Product id cannot be null");
        if(!this.products.containsKey(productId))throw new InvalidAggregateValue("Product not in cart");
        ProductDeletedFromCart productDeletedFromCart = new ProductDeletedFromCart(this.cartId, productId, products.get(productId));
    }
}
