package com.eventsourcing.commerce.product;

import com.eventsourcing.commerce.product.event.*;
import com.eventsourcing.commerce.product.representation.ProductRepresentation;
import com.eventsourcing.commerce.product.representation.ProductRepresentationRepository;
import com.eventsourcing.commerce.product.representation.ProductStockRepresentation;
import com.eventsourcing.commerce.product.representation.ProductStockRepresentationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductListener {

    private final ProductRepresentationRepository productRepresentationRepository;
    private final ProductStockRepresentationRepository productStockRepresentationRepository;

    @EventListener
    public void listenProductRegistered(ProductRegistered productRegistered){
        ProductRepresentation productRepresentation = new ProductRepresentation(productRegistered.productId(),
                productRegistered.name(), productRegistered.unityValue(), productRegistered.supplier(), productRegistered.productType());
        productRepresentationRepository.save(productRepresentation);
        ProductStockRepresentation productStockRepresentation = new ProductStockRepresentation(productRegistered.productId(), 0, 0);
        productStockRepresentationRepository.save(productStockRepresentation);
    }

    @EventListener
    public void listenStockAdded(StockAdded stockAdded){
        ProductStockRepresentation productStockRepresentation = productStockRepresentationRepository.findById(stockAdded.productId())
                .orElseThrow(() -> new RuntimeException("Product stock dont exists"));
        productStockRepresentation.addStock(stockAdded.quantity());
        productStockRepresentationRepository.save(productStockRepresentation);
    }

    @EventListener
    public void listenProductUpdated(ProductUpdated productUpdated){
        ProductRepresentation productRepresentation = productRepresentationRepository.findById(productUpdated.productId())
                .orElseThrow(() -> new RuntimeException("Product dont exists"));
        productRepresentation.updateInfos(productUpdated.name(), productUpdated.unityValue());
        productRepresentationRepository.save(productRepresentation);
    }

    @EventListener
    public void listenStockDeducted(StockDeducted stockDeducted){
        ProductStockRepresentation productStockRepresentation = productStockRepresentationRepository.findById(stockDeducted.productId())
                .orElseThrow(() -> new RuntimeException("Product stock dont exists"));
        productStockRepresentation.deductStock(stockDeducted.quantity());
        productStockRepresentationRepository.save(productStockRepresentation);
    }

    @EventListener
    public void listenStockReleased(StockReleased stockReleased){
        ProductStockRepresentation productStockRepresentation = productStockRepresentationRepository.findById(stockReleased.productId())
                .orElseThrow(() -> new RuntimeException("Product stock dont exists"));
        productStockRepresentation.releaseStock(stockReleased.quantity());
        productStockRepresentationRepository.save(productStockRepresentation);
    }

    @EventListener
    public void listenStockReserved(StockReserved stockReserved){
        ProductStockRepresentation productStockRepresentation = productStockRepresentationRepository.findById(stockReserved.productId())
                .orElseThrow(() -> new RuntimeException("Product stock dont exists"));
        productStockRepresentation.reserveStock(stockReserved.quantity());
        productStockRepresentationRepository.save(productStockRepresentation);
    }
}
