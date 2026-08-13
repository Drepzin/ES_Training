package com.eventsourcing.commerce.eventStore;

import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class EventTypeRegistry {

    private final HashMap<EventType, Class<? extends DomainEvent>> registry = new HashMap<>();

    public void register(EventType eventType, Class<? extends DomainEvent> event){
        registry.put(eventType, event);
    }

    public Class<? extends DomainEvent> getEventClass(EventType eventType){
        var objClass = registry.get(eventType);
        if(objClass == null){
            throw new RuntimeException("Invalid event");
        }
        return objClass;
    }
}
