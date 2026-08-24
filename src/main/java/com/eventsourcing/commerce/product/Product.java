package com.eventsourcing.commerce.product;

import com.eventsourcing.commerce.eventStore.DomainEvent;
import com.eventsourcing.commerce.eventStore.exception.InvalidAggregateValue;
import com.eventsourcing.commerce.product.command.*;
import com.eventsourcing.commerce.product.event.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@EqualsAndHashCode(of = "productId")
public class Product {

    private UUID productId;

    private String name;

    private BigDecimal unityValue;

    private String supplier;

    private String productType;

    private int stock;

    private int reserved;

    public Product(){}

    public static Product reconstruct(List<DomainEvent> events){
        Product product = new Product();
        for(DomainEvent e : events){
            product.apply(e);
        }
        return product;
    }

    public void apply(DomainEvent event){
        switch (event){
            case ProductDiscontinued e -> apply(e);
            case ProductRegistered e -> apply(e);
            case ProductUpdated e -> apply(e);
            case StockAdded e -> apply(e);
            case StockReleased e -> apply(e);
            case StockReserved e -> apply(e);
            case StockDeducted e -> apply(e);
            default -> throw new RuntimeException("Invalid product event");
        }
    }

    private void apply(ProductDiscontinued productDiscontinued){
        this.stock = 0;
        this.reserved = 0;
    }

    private void apply(ProductRegistered productRegistered){
        this.productId = productRegistered.productId();
        this.name = productRegistered.name();
        this.unityValue = productRegistered.unityValue();
        this.supplier = productRegistered.supplier();
        this.productType = productRegistered.productType();
    }

    private void apply(ProductUpdated productUpdated){
        this.name = productUpdated.name();
        this.unityValue = productUpdated.unityValue();
    }

    private void apply(StockAdded stockAdded){
        this.stock += stockAdded.quantity();
    }

    private void apply(StockReleased stockReleased){
        this.reserved -= stockReleased.quantity();
    }

    private void apply(StockReserved stockReserved){
        this.reserved += stockReserved.quantity();
    }

    private void apply(StockDeducted stockDeducted){this.stock -= stockDeducted.quantity();
    this.reserved -= stockDeducted.quantity();}

    public DomainEvent handle(DiscontinueProduct discontinueProduct){
        if(this.productId == null)throw new InvalidAggregateValue("Invalid product!");
        ProductDiscontinued productDiscontinued = new ProductDiscontinued(this.productId);
        this.apply(productDiscontinued);
        return productDiscontinued;
    }

    public DomainEvent handle(RegisterProduct registerProduct){
        if(this.productId != null)throw new InvalidAggregateValue("Product existent already");
        UUID productId = registerProduct.productId();
        String name = registerProduct.name();
        BigDecimal unityValue = registerProduct.unityValue();
        String supplier = registerProduct.supplier();
        String productType = registerProduct.productType();
        validateProductRegister(productId, name, unityValue, supplier, productType);
        ProductRegistered productRegistered = new ProductRegistered(productId, name, unityValue, supplier, productType);
        this.apply(productRegistered);
        return productRegistered;
    }

    public DomainEvent handle(UpdateProduct updateProduct){
        if(this.productId == null)throw new InvalidAggregateValue("Inexistent product");
        String name = updateProduct.name();
        BigDecimal unityValue = updateProduct.unityValue();
        if(name.isBlank())throw new InvalidAggregateValue("Product name cannot be blank");
        if(unityValue.doubleValue() <= 0)throw new InvalidAggregateValue("Product value had to be higher than 0");
        ProductUpdated productUpdated = new ProductUpdated(this.productId, name, unityValue);
        this.apply(productUpdated);
        return productUpdated;
    }

    public DomainEvent handle(AddStock addStock) {
        Integer quantity = addStock.quantity();
        if (this.productId == null) throw new InvalidAggregateValue("Inexistent product");
        if (quantity <= 0) throw new InvalidAggregateValue("Cannot add zero or lower quantities of product in stock");
        StockAdded stockAdded = new StockAdded(this.productId, quantity);
        this.apply(stockAdded);
        return stockAdded;
    }

    public DomainEvent handle(ReleaseStock releaseStock){
        Integer quantity = releaseStock.quantity();
        if(this.productId == null)throw new InvalidAggregateValue("Inexistent product");
        if(quantity <= 0) throw new InvalidAggregateValue("Cannot release zero or lower quantities of product from stock");
        if(quantity > reserved)throw new InvalidAggregateValue("Insufficient product available");
        StockReleased stockReleased = new StockReleased(this.productId, quantity);
        this.apply(stockReleased);
        return stockReleased;
    }

    public DomainEvent handle(ReserveStock reserveStock){
        int stockAvailable = stock - reserved;
        Integer quantity = reserveStock.quantity();
        if(this.productId == null)throw new InvalidAggregateValue("Inexistent product");
        if(quantity<=0)throw new InvalidAggregateValue("Cannot reserve zero or lower quantities of product from stock");
        if(quantity > stockAvailable)throw new InvalidAggregateValue("Insufficient product available");
        StockReserved stockReserved = new StockReserved(this.productId, quantity);
        this.apply(stockReserved);
        return stockReserved;
    }

    public DomainEvent handle(DeductStock deductStock){
        Integer quantity = deductStock.quantity();
        if(this.productId == null)throw new InvalidAggregateValue("Inexistent product");
        if(quantity<=0)throw new InvalidAggregateValue("Cannot reserve zero or lower quantities of product from stock");
        if(quantity > reserved)throw new InvalidAggregateValue("Value great than the reserved");
        StockDeducted stockDeducted = new StockDeducted(this.productId, quantity);
        this.apply(stockDeducted);
        return stockDeducted;
    }

    private void validateProductRegister(UUID productId, String name, BigDecimal unityValue, String supplier, String productType){
        if(productId == null) throw new InvalidAggregateValue("Product id cannot be null");
        if(name == null || name.isBlank()) throw new InvalidAggregateValue("Product name cannot be blank");
        if(unityValue == null || unityValue.doubleValue() <= 0) throw new InvalidAggregateValue("Product value had to be higher than 0");
        if(supplier == null || supplier.isBlank()) throw new InvalidAggregateValue("Product supplier cannot be blank");
        if(productType == null || productType.isBlank()) throw new InvalidAggregateValue("Product type cannot be blank");
    }
}