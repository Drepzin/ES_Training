package com.eventsourcing.commerce.aggregates;

import com.eventsourcing.commerce.eventStore.DomainEvent;
import com.eventsourcing.commerce.eventStore.exception.InvalidAggregateValue;
import com.eventsourcing.commerce.product.Product;
import com.eventsourcing.commerce.product.command.*;
import com.eventsourcing.commerce.product.event.ProductRegistered;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ProductCommandTest {

    UUID productId;
    String name;
    BigDecimal value;
    String supplier;
    String productType;
    Product product;

    @BeforeEach
    void init(){
        productId = UUID.randomUUID();
        name = "Molho-tartaro-ingles";
        value = BigDecimal.valueOf(15.69);
        supplier = "Molhos-tech";
        productType = "Alimenticios";

        RegisterProduct registerProduct = new RegisterProduct(productId, name, value, supplier, productType);
        product = Product.reconstruct(List.of());
        product.handle(registerProduct);
    }

    @Test
    @DisplayName("should not register the product again")
    void shouldNotRegisterProductAgain(){
        RegisterProduct otherProduct = new RegisterProduct(UUID.randomUUID(), "Sabao macaco", BigDecimal.valueOf(69.67), "saboees brasil", "limpeza");

        assertThrows(InvalidAggregateValue.class, () -> product.handle(otherProduct));
        assertEquals(name, product.getName()); // estado não mudou
    }

    @Test
    @DisplayName("should update the product")
    void shouldUpdateProduct(){
        UpdateProduct updateProduct = new UpdateProduct("Molho ingles", BigDecimal.valueOf(13.50));

        product.handle(updateProduct);

        assertEquals("Molho ingles", product.getName());
        assertEquals(BigDecimal.valueOf(13.50), product.getUnityValue());
    }

    @Test
    @DisplayName("should not update the product with blank name")
    void shouldNotUpdateProductWithBlankName(){
        UpdateProduct updateProduct = new UpdateProduct("", value);

        assertThrows(InvalidAggregateValue.class, () -> product.handle(updateProduct));
        assertEquals(name, product.getName()); // estado não mudou
    }

    @Test
    @DisplayName("should not update the product with negative price")
    void shouldNotUpdateProductWithNegativeValue(){
        UpdateProduct updateProduct = new UpdateProduct(name, BigDecimal.valueOf(-10.50));

        assertThrows(InvalidAggregateValue.class, () -> product.handle(updateProduct));
        assertEquals(value, product.getUnityValue()); // estado não mudou
    }

    @Test
    @DisplayName("should add product on the stock")
    void shouldAddProductInStock(){
        AddStock addStock = new AddStock(5);

        product.handle(addStock);

        assertEquals(5, product.getStock());
    }

    @Test
    @DisplayName("should not add product on the stock cause value is negative")
    void shouldNotAddProductInStock(){
        AddStock addStock = new AddStock(-5);

        assertThrows(InvalidAggregateValue.class, () -> product.handle(addStock));
    }

    @Test
    @DisplayName("should deduct product in stock")
    void shouldDeductProductInStock(){
        AddStock addStock = new AddStock(5);
        ReserveStock reserveStock = new ReserveStock(3);
        DeductStock deductStock = new DeductStock(3);
        product.handle(addStock);
        product.handle(reserveStock);
        product.handle(deductStock);

        assertEquals(2, product.getStock());
    }

    @Test
    @DisplayName("should not deduct product in stock")
    void shouldNotDeductProductWhenStockIsEmpty(){
        DeductStock deductStock = new DeductStock(3);

        assertThrows(InvalidAggregateValue.class, () -> product.handle(deductStock));
    }

    @Test
    @DisplayName("should reserve stock correctly")
    void shouldReserveStock(){
        AddStock addStock = new AddStock(4);
        ReserveStock reserveStock = new ReserveStock(3);

        product.handle(addStock);
        product.handle(reserveStock);

        assertEquals(3, product.getReserved());
    }

    @Test
    @DisplayName("should not reserve the stock when reserve is greater than stock")
    void shouldNotReserveStockWhenReserveIsGreaterThanStock(){
        AddStock addStock = new AddStock(4);
        ReserveStock reserveStock = new ReserveStock(8);

        product.handle(addStock);

        assertThrows(InvalidAggregateValue.class, () -> product.handle(reserveStock));
    }

    @Test
    @DisplayName("should correctly discontinue the product")
    void shouldDiscontinueTheProduct(){
        DiscontinueProduct discontinueProduct = new DiscontinueProduct();

        product.handle(discontinueProduct);

        assertEquals(0, product.getStock());
    }
}