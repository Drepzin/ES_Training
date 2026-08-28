package com.eventsourcing.commerce.eventStore.utils;

import lombok.*;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class AggregateSequence <T>{

    private T aggregate;
    private int sequence;
}
