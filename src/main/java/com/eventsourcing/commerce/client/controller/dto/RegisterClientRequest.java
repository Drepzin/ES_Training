package com.eventsourcing.commerce.client.controller.dto;

public record RegisterClientRequest(String firstName, String lastName, String email, String password) {
}
