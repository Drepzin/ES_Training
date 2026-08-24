package com.eventsourcing.commerce.client;

import com.eventsourcing.commerce.client.command.DeactivateClient;
import com.eventsourcing.commerce.client.command.RegisterClient;
import com.eventsourcing.commerce.client.command.UpdateClient;
import com.eventsourcing.commerce.client.event.ClientDeactivated;
import com.eventsourcing.commerce.client.event.ClientRegistered;
import com.eventsourcing.commerce.client.event.ClientUpdated;
import com.eventsourcing.commerce.eventStore.DomainEvent;
import com.eventsourcing.commerce.eventStore.exception.InvalidAggregateValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@EqualsAndHashCode(of = "clientId")
public class Client {

    private UUID clientId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private boolean active;

    public Client(){}

    public static Client reconstruct(List<DomainEvent> events){
        Client client = new Client();
        for(DomainEvent e : events){
            client.apply(e);
        }
        return client;
    }

    public void apply(DomainEvent event){
        switch (event){
            case ClientDeactivated e -> apply(e);
            case ClientRegistered e -> apply(e);
            case ClientUpdated e -> apply(e);
            default -> throw new IllegalStateException("Unexpected value: " + event);
        }
    }

    private void apply(ClientDeactivated clientDeactivated){
        this.active = false;
    }

    private void apply(ClientRegistered clientRegistered){
        this.clientId = clientRegistered.clientId();
        this.firstName = clientRegistered.firstName();
        this.lastName = clientRegistered.lastName();
        this.email = clientRegistered.email();
        this.password = clientRegistered.password();
        this.active = true;
    }

    private void apply(ClientUpdated clientUpdated){
        this.firstName = clientUpdated.firstName();
        this.lastName = clientUpdated.lastName();
        this.email = clientUpdated.email();
        this.password = clientUpdated.password();
    }

    public DomainEvent handle(DeactivateClient deactivateClient){
        if(this.clientId == null)throw new InvalidAggregateValue("Client does not exists");
        if(!this.active){
            throw new InvalidAggregateValue("Client is already deactivated");
        }
        ClientDeactivated clientDeactivated = new ClientDeactivated(this.clientId);
        this.apply(clientDeactivated);
        return clientDeactivated;
    }

    public DomainEvent handle(RegisterClient registerClient){
        if(this.clientId != null)throw new InvalidAggregateValue("This client is already registered");
        UUID clientId = registerClient.clientId();
        String firstName = registerClient.firstName();
        String lastName = registerClient.lastName();
        String email = registerClient.email();
        String password = registerClient.password();
        validateClientFields(clientId, firstName, lastName, email, password);
        ClientRegistered clientRegistered = new ClientRegistered(clientId, firstName, lastName, email, password);
        this.apply(clientRegistered);
        return clientRegistered;
    }

    public DomainEvent handle(UpdateClient updateClient){
        String firstName = updateClient.firstName();
        String lastName = updateClient.lastName();
        String email = updateClient.email();
        String password = updateClient.password();
        validateClientFields(this.clientId, firstName, lastName, email, password);
        ClientUpdated clientUpdated = new ClientUpdated(this.clientId, firstName, lastName, email, password);
        this.apply(clientUpdated);
        return clientUpdated;
    }

    private void validateClientFields(UUID clientId, String firstName, String lastName, String email, String password){
        if(clientId == null)throw new InvalidAggregateValue("Client id cannot be null");
        if(firstName == null || firstName.isBlank())throw new InvalidAggregateValue("First name cannot be null or empty");
        if(lastName == null || lastName.isBlank())throw new InvalidAggregateValue("Second name cannot be null or empty");
        if(email == null || email.isBlank())throw new InvalidAggregateValue("Email cannot be null or empty");
        if(password == null || password.isBlank())throw new InvalidAggregateValue("Password cannot be null or empty");
    }
}
