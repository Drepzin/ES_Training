package com.eventsourcing.commerce.client;

import com.eventsourcing.commerce.client.command.DeactivateClient;
import com.eventsourcing.commerce.client.command.RegisterClient;
import com.eventsourcing.commerce.client.command.UpdateClient;
import com.eventsourcing.commerce.client.controller.dto.RegisterClientRequest;
import com.eventsourcing.commerce.eventStore.DomainEvent;
import com.eventsourcing.commerce.eventStore.EventStore;
import com.eventsourcing.commerce.eventStore.EventType;
import com.eventsourcing.commerce.eventStore.utils.AggregateSequence;
import com.eventsourcing.commerce.eventStore.utils.AggregatorReconstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final EventStore eventStore;
    private final AggregatorReconstructor aggregatorReconstructor;
    private final ApplicationEventPublisher applicationEventPublisher;

    public UUID registerClient(RegisterClientRequest registerClientRequest){
        UUID clientId = UUID.randomUUID();
        Client client = new Client();
        RegisterClient registerClient = new RegisterClient(clientId, registerClientRequest.firstName(),
                registerClientRequest.lastName(), registerClientRequest.email(), registerClientRequest.password());
        DomainEvent domainEvent = client.handle(registerClient);
        String streamId = "client-" + clientId;
        persist(streamId, 0, EventType.CLIENT_REGISTERED, domainEvent);
        applicationEventPublisher.publishEvent(domainEvent);
        return clientId;
    }

    public void updatedClient(UpdateClient updateClient, UUID clientId){
        LoadedClient loadedClient = load(clientId);
        DomainEvent domainEvent = loadedClient.client().handle(updateClient);
        persist(loadedClient.streamId(), loadedClient.nextSequence(), EventType.CLIENT_UPDATED, domainEvent);
        applicationEventPublisher.publishEvent(domainEvent);
    }

    public void deactivateClient(DeactivateClient deactivateClient, UUID clientId){
        LoadedClient loadedClient = load(clientId);
        DomainEvent domainEvent = loadedClient.client().handle(deactivateClient);
        persist(loadedClient.streamId(), loadedClient.nextSequence(), EventType.CLIENT_DEACTIVATED, domainEvent);
        applicationEventPublisher.publishEvent(domainEvent);
    }

    private LoadedClient load(UUID clientId){
        String streamId = "client-" + clientId;
        AggregateSequence<Client> clientAggregateSequence = aggregatorReconstructor.reconstructAggregate(streamId, Client::reconstruct);
        Client client = clientAggregateSequence.getAggregate();
        int nextSequence = clientAggregateSequence.getSequence();
        return new LoadedClient(streamId, client, nextSequence);
    }

    private void persist(String streamId, int sequence, EventType eventType, DomainEvent payload){
        eventStore.saveEvent(streamId, sequence, UUID.randomUUID(), UUID.randomUUID(), eventType, payload);
    }
}
