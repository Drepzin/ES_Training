package com.eventsourcing.commerce.order.representation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepresentationRepository extends JpaRepository<OrderRepresentation, UUID> {
}
