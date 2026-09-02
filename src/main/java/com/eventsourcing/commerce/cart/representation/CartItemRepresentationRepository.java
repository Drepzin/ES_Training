package com.eventsourcing.commerce.cart.representation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CartItemRepresentationRepository extends JpaRepository<CartItemRepresentation, UUID> {
}
