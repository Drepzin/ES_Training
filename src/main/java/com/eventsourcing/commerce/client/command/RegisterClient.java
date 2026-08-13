package com.eventsourcing.commerce.client.command;

import java.util.UUID;

public record RegisterClient(UUID clientId, String firstName, String lastName, String email, String password) {
}
