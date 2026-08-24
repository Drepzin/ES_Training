package com.eventsourcing.commerce.aggregates;

import com.eventsourcing.commerce.client.Client;
import com.eventsourcing.commerce.client.command.DeactivateClient;
import com.eventsourcing.commerce.client.command.RegisterClient;
import com.eventsourcing.commerce.client.command.UpdateClient;
import com.eventsourcing.commerce.eventStore.exception.InvalidAggregateValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

public class ClientCommandsTest {

    Client client;

    @BeforeEach
    void init(){
        UUID clientId = UUID.randomUUID();
        String firstName = "joarlequim silva";
        String lastName = "borgas";
        String email = "pregas@email.com";
        String password = "abc323f";
        RegisterClient registerClient = new RegisterClient(clientId, firstName, lastName, email, password);
        client = Client.reconstruct(List.of());
        client.handle(registerClient);
    }

    @Test
    @DisplayName("should update client correctly")
    void shouldUpdateClient(){
        String firstName = "Hal";
        String lastName = "jordan";
        String email = "lantern@gmail.com";
        String password = "abc232f";
        UpdateClient updateClient = new UpdateClient(firstName, lastName, email, password);
        client.handle(updateClient);

        assertEquals(firstName, client.getFirstName());
        assertEquals(lastName, client.getLastName());
        assertEquals(email, client.getEmail());
        assertEquals(password, client.getPassword());
    }

    @Test
    @DisplayName("should not update client if fields are wrongs")
    void shouldNotUpdateClient(){
        String firstName = "";
        String lastName = "jordan";
        String email = "lantern@gmail.com";
        String password = "abc232f";
        UpdateClient updateClient = new UpdateClient(firstName, lastName, email, password);

        assertThrows(InvalidAggregateValue.class, () -> client.handle(updateClient));
    }

    @Test
    @DisplayName("should deactivate the client from db correctly")
    void shouldDeactivateClient(){
        DeactivateClient deactivateClient = new DeactivateClient();
        client.handle(deactivateClient);

        assertFalse(client.isActive());
    }

    @Test
    @DisplayName("should not deactivate the client if already deactivate")
    void shouldNotDeactivateClient(){
        DeactivateClient deactivateClient = new DeactivateClient();
        client.handle(deactivateClient);

        assertThrows(InvalidAggregateValue.class, () -> client.handle(deactivateClient));
    }
}
