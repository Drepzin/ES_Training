package com.eventsourcing.commerce.aggregates;

import com.eventsourcing.commerce.client.Client;
import com.eventsourcing.commerce.client.command.RegisterClient;
import com.eventsourcing.commerce.eventStore.exception.InvalidAggregateValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ClientRegistryTest {

    Client client;

    @Test
    @DisplayName("should register the user correctly")
    void shouldRegisterTheClient(){
        UUID clientId = UUID.randomUUID();
        String firstName = "joarlequim silva";
        String lastName = "borgas";
        String email = "pregas@email.com";
        String password = "abc323f";
        RegisterClient registerClient = new RegisterClient(clientId, firstName, lastName, email, password);
        client = Client.reconstruct(List.of());
        client.handle(registerClient);
        assertEquals(firstName, client.getFirstName());
        assertEquals(lastName, client.getLastName());
        assertEquals(email, client.getEmail());
        assertEquals(password, client.getPassword());
        assertTrue(client.isActive());
    }

    @Test
    @DisplayName("should not register the user if fields are missing")
    void shouldNotRegisterTheClient(){
        UUID clientId = UUID.randomUUID();
        String firstName = "asasa";
        String lastName = "";
        String email = "pregas@email.com";
        String password = "abc323f";
        RegisterClient registerClient = new RegisterClient(clientId, firstName, lastName, email, password);
        client = Client.reconstruct(List.of());
        assertThrows(InvalidAggregateValue.class,() -> client.handle(registerClient));
    }
}
