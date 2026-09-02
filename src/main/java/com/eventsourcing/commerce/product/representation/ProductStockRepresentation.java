package com.eventsourcing.commerce.product.representation;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "product_stock_representation")
@Data
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
public class ProductStockRepresentation {

    @Id
    private UUID id;

    private int stock;

    private int reserved;

    public void addStock(int quantity){
        this.stock += quantity;
    }

    public void deductStock(int quantity){
        this.stock -= quantity;
        this.reserved -= quantity;
    }

    public void releaseStock(int quantity){
        this.reserved -= quantity;
    }

    public void reserveStock(int quantity){
        this.reserved += quantity;
    }
}
