package com.eventsourcing.commerce.client;

import com.eventsourcing.commerce.eventStore.DomainEvent;
import com.eventsourcing.commerce.eventStore.EventStore;
import com.eventsourcing.commerce.eventStore.EventType;
import com.eventsourcing.commerce.eventStore.utils.AggregatorReconstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final EventStore eventStore;
    private final AggregatorReconstructor aggregatorReconstructor;

    private void persist(String streamId, int sequence, EventType eventType, DomainEvent payload){
        eventStore.saveEvent(streamId, sequence, UUID.randomUUID(), UUID.randomUUID(), eventType, payload);
    }
}
