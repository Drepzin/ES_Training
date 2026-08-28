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

    public <T> AggregateSequence<T> reconstructAggregate(String streamId, Function<List<DomainEvent>, T> reconstructor){
        if(streamId.isBlank()){
            throw new RuntimeException("Invalid stream id");
        }
        List<DomainEvent> events = eventStore.findEvents(streamId);
        T aggregate = reconstructor.apply(events);
        return new AggregateSequence<T>(aggregate, events.size());
    }

}
