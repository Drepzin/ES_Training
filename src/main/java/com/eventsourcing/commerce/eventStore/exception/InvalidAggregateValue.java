package com.eventsourcing.commerce.eventStore.exception;

public class InvalidAggregateValue extends RuntimeException {


    public InvalidAggregateValue(String message) {
        super(message);
    }

}
