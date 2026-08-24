package com.eventsourcing.commerce.aggregates;

import com.eventsourcing.commerce.product.Product;
import com.eventsourcing.commerce.product.event.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

public class ProductEventsTest {

    ProductRegistered productRegistered;
    ProductUpdated productUpdated;
    StockAdded stockAdded;
    StockDeducted stockDeducted;
    StockReleased stockReleased;
    StockReserved stockReserved;

    @Test
    @DisplayName("should correctly register the product")
    void shouldRegisterProduct(){
        //given
        UUID productId = UUID.randomUUID();
        productRegistered = new ProductRegistered(productId, "pingo-doce", BigDecimal.valueOf(12.50), "vendas-sol", "alimenticio");
        //when
        Product product = Product.reconstruct(List.of(productRegistered));
        String name = product.getName();
        BigDecimal value = product.getUnityValue();
        String supplier = product.getSupplier();
        String productType = product.getProductType();
        //then
        assertEquals(productRegistered.name(), name);
        assertEquals(productRegistered.unityValue(), value);
        assertEquals(productRegistered.supplier() ,supplier);
        assertEquals(productRegistered.productType() ,productType);
    }

    @Test
    @DisplayName("should update the product")
    void shouldUpdateProduct(){
        //given
        UUID productId = UUID.randomUUID();
        productRegistered = new ProductRegistered(productId, "pingo-doce", BigDecimal.valueOf(12.50), "vendas-sol", "alimenticio");
        productUpdated = new ProductUpdated(productId, "pinguinho", BigDecimal.valueOf(10.50));
        //when
        Product product = Product.reconstruct(List.of(productRegistered, productUpdated));
        String name = product.getName();
        BigDecimal value = product.getUnityValue();
        //then
        assertEquals(productUpdated.name(), product.getName());
        assertEquals(productUpdated.unityValue(), product.getUnityValue());
        assertNotEquals(productRegistered.name(), product.getName());
        assertNotEquals(productRegistered.unityValue(), product.getUnityValue());
    }

    @Test
    @DisplayName("should add stock of the product")
    void shouldAddStock(){
        //given
        UUID productId = UUID.randomUUID();
        productRegistered = new ProductRegistered(productId, "pingo-doce", BigDecimal.valueOf(12.50), "vendas-sol", "alimenticio");
        stockAdded = new StockAdded(productId, 5);
        //when
        Product product = Product.reconstruct(List.of(productRegistered, stockAdded));
        //then
        assertTrue(product.getStock() > 0, "Stock is greater than zero");
        assertEquals(5, product.getStock());
    }

    @Test
    @DisplayName("should deduct product of the stock")
    void shouldDeductStock(){
        //given
        UUID productId = UUID.randomUUID();
        productRegistered = new ProductRegistered(productId, "pingo-doce", BigDecimal.valueOf(12.50), "vendas-sol", "alimenticio");
        stockAdded = new StockAdded(productId, 5);
        stockDeducted = new StockDeducted(productId, 2);
        //when
        Product product = Product.reconstruct(List.of(productRegistered, stockAdded, stockDeducted));
        //then
        assertTrue(product.getStock() > 0);
        assertEquals(3, product.getStock());
    }

    @Test
    @DisplayName("should reserve product of the stock")
    void shouldReserveProduct(){
        //given
        UUID productId = UUID.randomUUID();
        productRegistered = new ProductRegistered(productId, "pingo-doce", BigDecimal.valueOf(12.50), "vendas-sol", "alimenticio");
        stockAdded = new StockAdded(productId, 5);
        stockReserved = new StockReserved(productId, 4);
        //when
        Product product = Product.reconstruct(List.of(productRegistered, stockAdded, stockReserved));
        //then
        assertTrue(product.getReserved() > 0);
        assertEquals(4, product.getReserved());
    }

    @Test
    @DisplayName("should release the stock")
    void shouldReleaseStock(){
        //given
        UUID productId = UUID.randomUUID();
        productRegistered = new ProductRegistered(productId, "pingo-doce", BigDecimal.valueOf(12.50), "vendas-sol", "alimenticio");
        stockAdded = new StockAdded(productId, 5);
        stockReserved = new StockReserved(productId, 4);
        stockReleased = new StockReleased(productId, 3);
        //when
        Product product = Product.reconstruct(List.of(productRegistered, stockAdded, stockReserved, stockReleased));
        //then
        assertEquals(1, product.getReserved());
    }

    @Test
    @DisplayName("should release the stock of product after a successfully buy")
    void shouldDoFullBuyFlux(){
        //given
        UUID productId = UUID.randomUUID();
        productRegistered = new ProductRegistered(productId, "pingo-doce", BigDecimal.valueOf(12.50), "vendas-sol", "alimenticio");
        stockAdded = new StockAdded(productId, 5);
        stockReserved = new StockReserved(productId, 4);
        stockDeducted = new StockDeducted(productId, 2);
        //when
        Product product = Product.reconstruct(List.of(productRegistered, stockAdded, stockReserved, stockDeducted));
        //then
        assertEquals(2, product.getReserved());
        assertEquals(3, product.getStock());
    }
}
