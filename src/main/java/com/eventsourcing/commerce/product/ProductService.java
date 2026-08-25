package com.eventsourcing.commerce.product;

import com.eventsourcing.commerce.eventStore.EventStore;
import com.eventsourcing.commerce.eventStore.utils.AggregatorReconstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final EventStore eventStore;
    private final AggregatorReconstructor aggregatorReconstructor;

    public Product reconstructProduct(String streamId){
        var events = eventStore.findEvents(streamId);
        return Product.reconstruct(events);
    }

}
