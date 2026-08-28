package com.eventsourcing.commerce.product;

import com.eventsourcing.commerce.eventStore.*;
import com.eventsourcing.commerce.eventStore.utils.AggregateSequence;
import com.eventsourcing.commerce.eventStore.utils.AggregatorReconstructor;
import com.eventsourcing.commerce.product.command.*;
import com.eventsourcing.commerce.product.controller.dto.RegisterProductRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final EventStore eventStore;
    private final AggregatorReconstructor aggregatorReconstructor;

    public void registerProduct(RegisterProductRequest registerProductRequest){
        UUID productId = UUID.randomUUID();
        String productName = registerProductRequest.productName();
        BigDecimal unityValue = registerProductRequest.unityValue();
        String supplier = registerProductRequest.supplier();
        String productType = registerProductRequest.productType();
        RegisterProduct registerProduct = new RegisterProduct(productId, productName, unityValue, supplier, productType);
        Product product = new Product();
        DomainEvent domainEvent = product.handle(registerProduct);
        String streamId = "product-" + productId;
        persist(streamId, 0, EventType.PRODUCT_REGISTERED, domainEvent);
    }

    public void addStock(AddStock addStock, UUID productId){
        LoadedProduct loadedProduct = load(productId);
        DomainEvent domainEvent = loadedProduct.product().handle(addStock);
        persist(loadedProduct.streamId(), loadedProduct.nextSequence(), EventType.STOCK_ADDED, domainEvent);
    }

    public void deductStock(DeductStock deductStock, UUID productId){
        LoadedProduct loadedProduct = load(productId);
        DomainEvent domainEvent = loadedProduct.product().handle(deductStock);
        persist(loadedProduct.streamId(), loadedProduct.nextSequence(), EventType.STOCK_DEDUCTED, domainEvent);
    }

    public void discontinueProduct(DiscontinueProduct discontinueProduct, UUID productId){
        LoadedProduct loadedProduct = load(productId);
        DomainEvent domainEvent = loadedProduct.product().handle(discontinueProduct);
        persist(loadedProduct.streamId(), loadedProduct.nextSequence(), EventType.PRODUCT_DISCONTINUED, domainEvent);
    }

    public void releaseStock(ReleaseStock releaseStock, UUID productId){
        LoadedProduct loadedProduct = load(productId);
        DomainEvent domainEvent = loadedProduct.product().handle(releaseStock);
        persist(loadedProduct.streamId(), loadedProduct.nextSequence(), EventType.STOCK_RELEASED, domainEvent);
    }

    public void reserveStock(ReserveStock reserveStock, UUID productId){
        LoadedProduct loadedProduct = load(productId);
        DomainEvent domainEvent = loadedProduct.product().handle(reserveStock);
        persist(loadedProduct.streamId(), loadedProduct.nextSequence(), EventType.STOCK_RESERVED, domainEvent);
    }

    public void updateProduct(UpdateProduct updateProduct, UUID productId){
        LoadedProduct loadedProduct = load(productId);
        DomainEvent domainEvent = loadedProduct.product().handle(updateProduct);
        persist(loadedProduct.streamId(), loadedProduct.nextSequence(), EventType.PRODUCT_UPDATED, domainEvent);
    }

    private void persist(String streamId, int sequence,  EventType eventType, DomainEvent payload) {
        eventStore.saveEvent(streamId, sequence, UUID.randomUUID(), UUID.randomUUID(), eventType, payload);
    }

    private LoadedProduct load(UUID productId){
        String streamId = "product-" + productId;
        AggregateSequence<Product> productAggregateSequence = aggregatorReconstructor.reconstructAggregate(streamId, Product::reconstruct);
        Product product = productAggregateSequence.getAggregate();
        int nextSequence = productAggregateSequence.getSequence();
        return new LoadedProduct(streamId, product, nextSequence);
    }
}
