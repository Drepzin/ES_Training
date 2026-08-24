package com.eventsourcing.commerce.aggregates;

import com.eventsourcing.commerce.client.Client;
import com.eventsourcing.commerce.client.command.UpdateClient;
import com.eventsourcing.commerce.client.event.ClientDeactivated;
import com.eventsourcing.commerce.client.event.ClientRegistered;
import com.eventsourcing.commerce.client.event.ClientUpdated;
import com.eventsourcing.commerce.eventStore.exception.InvalidAggregateValue;
import com.eventsourcing.commerce.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ClientEventsTest {

    ClientRegistered clientRegistered;

    @BeforeEach
    void init(){
        UUID clientId = UUID.randomUUID();
        String firstName = "Hal";
        String lastName = "jordan";
        String email = "lantern@gmail.com";
        String password = "abc232f";
        clientRegistered = new ClientRegistered(clientId, firstName, lastName, email, password);
    }

    @Test
    @DisplayName("")
    void shouldClientBeRegistered(){
        UUID clientId = UUID.randomUUID();
        String firstName = "Hal";
        String lastName = "jordan";
        String email = "lantern@gmail.com";
        String password = "abc232f";
        ClientRegistered clientRegistered = new ClientRegistered(clientId, firstName, lastName, email, password);
        Client client = Client.reconstruct(List.of(clientRegistered));

        assertEquals(firstName, client.getFirstName());
        assertEquals(lastName, client.getLastName());
        assertEquals(email, client.getEmail());
        assertEquals(password, client.getPassword());
    }

    @Test
    @DisplayName("should client be deactivated correctly")
    void shouldClientBeDeactivated(){
        UUID clientId = UUID.randomUUID();
        ClientDeactivated clientDeactivated = new ClientDeactivated(clientId);

        Client client = Client.reconstruct(List.of(clientRegistered, clientDeactivated));

        assertFalse(client.isActive());
    }

    @Test
    @DisplayName("should client be updated correctly")
    void shouldClientBeUpdated(){
        UUID clientId = UUID.randomUUID();
        String firstName = "Pedro";
        String lastName = "Ribeiro";
        String email = "P.ribeirodev@gmail.com";
        String password = "abc2212";
        ClientUpdated clientUpdated = new ClientUpdated(clientId, firstName, lastName, email, password);

        Client client = Client.reconstruct(List.of(clientRegistered, clientUpdated));
        assertEquals(firstName, client.getFirstName());
        assertEquals(lastName, client.getLastName());
        assertEquals(email, client.getEmail());
        assertEquals(password, client.getPassword());
    }
}
