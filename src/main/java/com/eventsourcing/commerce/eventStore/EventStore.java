package com.eventsourcing.commerce.eventStore;

import com.eventsourcing.commerce.eventStore.exception.ConcurrencyWriteException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class EventStore {

    private final AppEventRepository appEventRepository;
    private final EventSerializer eventSerializer;


    public void saveEvent(String streamId, int sequence, UUID correlation, UUID eventId, EventType eventType, DomainEvent payload){
        if(appEventRepository.existsByEventId(eventId)){
            throw new ConcurrencyWriteException("Event already registered!");
        }
        StreamSequenceId streamSequenceId = new StreamSequenceId(streamId, sequence);
        String eventPayload = eventSerializer.serializeEvent(payload);
        AppEvent event = AppEvent.register(streamSequenceId, eventId, correlation, eventType, eventPayload);
        try {
            appEventRepository.save(event);
        } catch (DataIntegrityViolationException e) {
            throw new ConcurrencyWriteException("Concurrency conflict on StreamId " + streamId + " " + e);
        }
    }

    public List<DomainEvent> findEvents(String streamId){
        List<AppEvent> events = appEventRepository.findEventByStreamId(streamId);
        return events.stream()
                .map(e -> eventSerializer.deserializeEvent(e.getEventType(), e.getPayload())).toList();
    }
}
