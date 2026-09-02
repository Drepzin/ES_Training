package com.eventsourcing.commerce.product.representation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepresentationRepository extends JpaRepository<ProductRepresentation, UUID> {
}
