package com.eventsourcing.commerce.aggregates;

import com.eventsourcing.commerce.eventStore.DomainEvent;
import com.eventsourcing.commerce.product.Product;
import com.eventsourcing.commerce.product.command.RegisterProduct;
import com.eventsourcing.commerce.product.event.ProductRegistered;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class ProuctRegistryTest {

    @Test
    @DisplayName("should correctly registry the product")
    void shouldRegistryTheProduct(){
        // essa aqui continua isolada, pois testa o PRÓPRIO registro -- não pode usar o `product` do init
        UUID newProductId = UUID.randomUUID();
        RegisterProduct registerProduct = new RegisterProduct(newProductId, "Sabao", BigDecimal.valueOf(10), "Fornecedor X", "Limpeza");

        Product freshProduct = Product.reconstruct(List.of());
        DomainEvent result = freshProduct.handle(registerProduct);

        ProductRegistered event = assertInstanceOf(ProductRegistered.class, result);
        assertEquals("Sabao", event.name());
        assertEquals(BigDecimal.valueOf(10), event.unityValue());
    }
}
