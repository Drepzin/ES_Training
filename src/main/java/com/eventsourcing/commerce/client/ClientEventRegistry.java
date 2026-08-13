package com.eventsourcing.commerce.client;

import com.eventsourcing.commerce.client.event.ClientDeactivated;
import com.eventsourcing.commerce.client.event.ClientRegistered;
import com.eventsourcing.commerce.client.event.ClientUpdated;
import com.eventsourcing.commerce.eventStore.EventType;
import com.eventsourcing.commerce.eventStore.EventTypeRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClientEventRegistry {

    private final EventTypeRegistry eventTypeRegistry;

    @PostConstruct
    public void registryEvents(){
        eventTypeRegistry.register(EventType.CLIENT_DEACTIVATED, ClientDeactivated.class);
        eventTypeRegistry.register(EventType.CLIENT_REGISTERED, ClientRegistered.class);
        eventTypeRegistry.register(EventType.CLIENT_UPDATED, ClientUpdated.class);
    }
}
