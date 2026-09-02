package com.eventsourcing.commerce.client;

import com.eventsourcing.commerce.client.event.ClientDeactivated;
import com.eventsourcing.commerce.client.event.ClientRegistered;
import com.eventsourcing.commerce.client.event.ClientUpdated;
import com.eventsourcing.commerce.client.representation.ClientRepresentation;
import com.eventsourcing.commerce.client.representation.ClientRepresentationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClientListener {

    private final ClientRepresentationRepository clientRepresentationRepository;

    @EventListener
    public void listenClientRegistered(ClientRegistered clientRegistered){
        ClientRepresentation clientRepresentation = new ClientRepresentation(clientRegistered.clientId(), clientRegistered.firstName(),
                clientRegistered.lastName(), clientRegistered.email(), clientRegistered.password(), true);
        clientRepresentationRepository.save(clientRepresentation);
    }

    @EventListener
    public void listenClientUpdated(ClientUpdated clientUpdated){
        ClientRepresentation clientRepresentation = clientRepresentationRepository.findById(clientUpdated.clientId())
                .orElseThrow(() -> new RuntimeException("Client dont exists"));
        clientRepresentation.updateInfos(clientUpdated.firstName(), clientUpdated.lastName(), clientUpdated.email(), clientUpdated.password());
        clientRepresentationRepository.save(clientRepresentation);
    }

    @EventListener
    public void listenClientDeactivated(ClientDeactivated clientDeactivated){
        ClientRepresentation clientRepresentation = clientRepresentationRepository.findById(clientDeactivated.clientId())
                .orElseThrow(() -> new RuntimeException("Client dont exists"));
        clientRepresentation.deactivate();
        clientRepresentationRepository.save(clientRepresentation);
    }
}
