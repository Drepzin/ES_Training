package com.eventsourcing.commerce.client.controller;

import com.eventsourcing.commerce.client.ClientService;
import com.eventsourcing.commerce.client.command.DeactivateClient;
import com.eventsourcing.commerce.client.command.RegisterClient;
import com.eventsourcing.commerce.client.command.UpdateClient;
import com.eventsourcing.commerce.client.controller.dto.RegisterClientRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<Void> registerClient(@RequestBody RegisterClientRequest registerClient){
        UUID clientId = clientService.registerClient(registerClient);
        return ResponseEntity.created(URI.create("/clients/" + clientId)).build();
    }

    @PatchMapping("{clientId}")
    public ResponseEntity<Void> updatedClient(@PathVariable UUID clientId, @RequestBody UpdateClient updateClient){
        clientService.updatedClient(updateClient, clientId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("{clientId}/deactivate")
    public ResponseEntity<Void> deactivateClient(@PathVariable UUID clientId, @RequestBody DeactivateClient deactivateClient){
        clientService.deactivateClient(deactivateClient, clientId);
        return ResponseEntity.ok().build();
    }
}
