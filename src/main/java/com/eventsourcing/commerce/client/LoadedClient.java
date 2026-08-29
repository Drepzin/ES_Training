package com.eventsourcing.commerce.client;

public record LoadedClient (String streamId, Client client, int nextSequence){
}
