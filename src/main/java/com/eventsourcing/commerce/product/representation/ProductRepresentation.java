package com.eventsourcing.commerce.product.representation;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "product_representation")
@Data
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
public class ProductRepresentation {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(name = "unity_value", nullable = false)
    private BigDecimal unityValue;

    @Column(nullable = false)
    private String supplier;

    @Column(name = "product_type", nullable = false)
    private String productType;

    public void updateInfos(String name, BigDecimal unityValue){
        this.name = name;
        this.unityValue = unityValue;
    }
}
