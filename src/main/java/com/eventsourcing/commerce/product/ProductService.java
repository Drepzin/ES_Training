package com.eventsourcing.commerce.product;

import com.eventsourcing.commerce.eventStore.EventStore;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductService {

    private final EventStore eventStore;

    public Product reconstructProduct(String streamId){
        var events = eventStore.findEvents(streamId);
        return Product.reconstruct(events);
    }

}
