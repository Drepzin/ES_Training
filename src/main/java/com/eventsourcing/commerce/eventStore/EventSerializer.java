package com.eventsourcing.commerce.eventStore;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

@Component
@RequiredArgsConstructor
public class EventSerializer {

    private final JsonMapper jsonMapper;
    private final EventTypeRegistry eventTypeRegistry;

    public String serializeEvent(DomainEvent domainEvent){
        try {
            return jsonMapper.writeValueAsString(domainEvent);
        } catch (JacksonException e) {
            throw new RuntimeException(e);
        }
    }

    public DomainEvent deserializeEvent(EventType eventType, String event){
        Class<? extends DomainEvent> clazz = eventTypeRegistry.getEventClass(eventType);
        if(clazz == null){
            throw new RuntimeException("Invalid event type");
        }
        return jsonMapper.readValue(event, clazz);
    }
}
