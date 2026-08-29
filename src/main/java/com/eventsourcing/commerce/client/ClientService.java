package com.eventsourcing.commerce.client;

import com.eventsourcing.commerce.client.command.DeactivateClient;
import com.eventsourcing.commerce.client.command.RegisterClient;
import com.eventsourcing.commerce.client.command.UpdateClient;
import com.eventsourcing.commerce.eventStore.DomainEvent;
import com.eventsourcing.commerce.eventStore.EventStore;
import com.eventsourcing.commerce.eventStore.EventType;
import com.eventsourcing.commerce.eventStore.utils.AggregateSequence;
import com.eventsourcing.commerce.eventStore.utils.AggregatorReconstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final EventStore eventStore;
    private final AggregatorReconstructor aggregatorReconstructor;

    public void registerClient(RegisterClient registerClient, UUID clientId){
        LoadedClient loadedClient = load(clientId);
        DomainEvent domainEvent = loadedClient.client().handle(registerClient);
        persist(loadedClient.streamId(), loadedClient.nextSequence(), EventType.CLIENT_REGISTERED, domainEvent);
    }

    public void updatedClient(UpdateClient updateClient, UUID clientId){
        LoadedClient loadedClient = load(clientId);
        DomainEvent domainEvent = loadedClient.client().handle(updateClient);
        persist(loadedClient.streamId(), loadedClient.nextSequence(), EventType.CLIENT_UPDATED, domainEvent);
    }

    public void deactivateClient(DeactivateClient deactivateClient, UUID clientId){
        LoadedClient loadedClient = load(clientId);
        DomainEvent domainEvent = loadedClient.client().handle(deactivateClient);
        persist(loadedClient.streamId(), loadedClient.nextSequence(), EventType.CLIENT_DEACTIVATED, domainEvent);
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
