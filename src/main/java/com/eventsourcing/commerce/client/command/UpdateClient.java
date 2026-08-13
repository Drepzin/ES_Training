package com.eventsourcing.commerce.client.command;

public record UpdateClient(String firstName, String lastName, String email, String password) {
}
