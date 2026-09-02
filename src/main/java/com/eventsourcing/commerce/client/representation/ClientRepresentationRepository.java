package com.eventsourcing.commerce.client.representation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClientRepresentationRepository extends JpaRepository<ClientRepresentation, UUID> {
}
