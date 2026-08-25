package com.eventsourcing.commerce.eventStore.utils;

import com.eventsourcing.commerce.eventStore.DomainEvent;
import com.eventsourcing.commerce.eventStore.EventStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class AggregatorReconstructor{

    private final EventStore eventStore;

    public <T> T reconstructAggregate(String streamId, Function<List<DomainEvent>, T> reconstructor){
        if(streamId.isBlank()){
            throw new RuntimeException("Invalid stream id");
        }
        List<DomainEvent> events = eventStore.findEvents(streamId);
        return reconstructor.apply(events);
    }

}
